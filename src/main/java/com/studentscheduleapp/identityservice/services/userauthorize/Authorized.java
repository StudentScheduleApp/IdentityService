package com.studentscheduleapp.identityservice.services.userauthorize;

import com.studentscheduleapp.identityservice.models.AuthorizeType;
import com.studentscheduleapp.identityservice.models.Entity;
import com.studentscheduleapp.identityservice.models.User;
import com.studentscheduleapp.identityservice.repos.UserRepository;
import com.studentscheduleapp.identityservice.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

public class Authorized {
    private AuthorizeType type;
    private String token;
    protected User user;
    protected List<Long> ids;
    protected List<String> params;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProvider jwtProvider;

    public final void initialize(AuthorizeType type, String token, List<Long> ids, List<String> params) {
        this.type = type;
        this.token = token;
        this.ids = ids;
        this.params = params;
    }


    public final boolean authorize() {
        User user = null;
        try {
            user = userRepository.getByEmail(jwtProvider.getAccessClaims(token).getSubject());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user == null)
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

    protected boolean authorizeDelete(){
        return false;
    }
    protected boolean authorizePatch(){
        return false;
    }
    protected boolean authorizeCreate(){
        return false;
    }
    protected boolean authorizeGet(){
        return false;
    }
}

