package com.studentscheduleapp.identityservice.services.userauthorize;

import com.studentscheduleapp.identityservice.models.AuthorizeType;
import com.studentscheduleapp.identityservice.models.Role;
import com.studentscheduleapp.identityservice.models.User;
import com.studentscheduleapp.identityservice.repos.UserRepository;
import com.studentscheduleapp.identityservice.security.JwtProvider;

import java.util.List;

public abstract class Authorized {

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
            e.printStackTrace();
        }
        if (user == null || user.getBanned())
            return false;
        switch (type){
            case GET:
                return authorizeGet();
            case CREATE:
                return authorizeCreate();
            case PATCH:
                return authorizePatch();
            case DELETE:
                return authorizeDelete();
            default:
                return false;
        }
    }

    protected abstract boolean authorizeDelete();
    protected abstract boolean authorizePatch();
    protected abstract boolean authorizeCreate();
    protected abstract boolean authorizeGet();
}

