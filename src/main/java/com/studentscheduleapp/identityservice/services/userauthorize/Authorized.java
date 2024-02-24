package com.studentscheduleapp.identityservice.services.userauthorize;

import com.studentscheduleapp.identityservice.models.AuthorizeType;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.studentscheduleapp.identityservice.models.User;
import com.studentscheduleapp.identityservice.repos.UserRepository;
import com.studentscheduleapp.identityservice.security.JwtProvider;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

public abstract class Authorized {

    private static final Logger log = LogManager.getLogger(Authorized.class);
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private AuthorizeType type;
    private String token;

    protected User user;
    protected List<Long> ids;
    protected List<String> params;

    public Authorized(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;

    }

    public final void init(String token, AuthorizeType type, List<Long> ids, List<String> params){
        this.token = token;
        this.type = type;
        this.ids = ids;
        this.params = params;
    }


    public final boolean authorize() {
        try {
            user = userRepository.getByEmail(jwtProvider.getAccessClaims(token).getSubject());
        } catch (Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            log.error("authorize failed: " + errors);
        }
        if (user == null || user.getBanned()) {
            log.warn("authorize failed: user banned or not exists");
            return false;
        }
        switch (type){
            case GET:
                return authorizeGet();
            case CREATE:
                return authorizeCreate();
            case PATCH:
                return authorizePatch();
            case DELETE:
                return authorizeDelete();
            default: {
                log.error("authorize failed: unknown auth type " + type.name());
                return false;
            }
        }
    }

    protected abstract boolean authorizeDelete();
    protected abstract boolean authorizePatch();
    protected abstract boolean authorizeCreate();
    protected abstract boolean authorizeGet();
}

