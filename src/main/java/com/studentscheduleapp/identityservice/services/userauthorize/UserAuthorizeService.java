package com.studentscheduleapp.identityservice.services.userauthorize;

import com.studentscheduleapp.identityservice.models.Role;
import com.studentscheduleapp.identityservice.repos.*;
import com.studentscheduleapp.identityservice.security.JwtProvider;
import org.springframework.stereotype.Service;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


@Service
public class UserAuthorizeService extends Authorized {

    private static final Logger log = LogManager.getLogger(UserAuthorizeService.class);
    public UserAuthorizeService(UserRepository ur, JwtProvider jwtProvider) {
        super(ur, jwtProvider);
    }

    @Override
    protected boolean authorizeDelete() {
        try {
            return user.getRoles().contains(Role.ULTIMATE);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected boolean authorizePatch() {
        try {
            if(params.contains("id") || params.contains("email") || params.contains("password"))
                return false;
            if((params.contains("firstName") || params.contains("lastName")
                    || params.contains("avaUrl")) &&
                    !(ids.size() == 1 || ids.contains(user.getId()) || user.getRoles().contains(Role.ADMIN)))
                return false;
            if(params.contains("banned") && (!user.getRoles().contains(Role.ADMIN) || ids.contains(user.getId())))
                return false;
            if (params.contains("roles") && !user.getRoles().contains(Role.ULTIMATE))
                return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected boolean authorizeCreate() {
        try {
            if (!user.getRoles().contains(Role.ULTIMATE))
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected boolean authorizeGet() {
        try {
            if (!user.getRoles().contains(Role.USER))
                return false;
            if (!(ids.size() == 1 &&
                    ids.contains(user.getId())
                    || user.getRoles().contains(Role.ULTIMATE))
                    && (params.contains("email") || params.contains("password")))
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
