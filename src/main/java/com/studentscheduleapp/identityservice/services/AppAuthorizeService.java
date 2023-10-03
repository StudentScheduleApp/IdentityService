package com.studentscheduleapp.identityservice.services;

import com.studentscheduleapp.identityservice.domain.models.Authorize;
import com.studentscheduleapp.identityservice.repos.AppTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppAuthorizeService {
    @Autowired
    private AppTokenRepo appTokenRepo;

    public boolean authorize(String token){
        return appTokenRepo.existsByAppToken(token);
    }

}
