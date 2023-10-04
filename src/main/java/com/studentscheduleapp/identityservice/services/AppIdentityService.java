package com.studentscheduleapp.identityservice.services;

import com.studentscheduleapp.identityservice.domain.models.AppToken;
import com.studentscheduleapp.identityservice.repos.AppTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppIdentityService {
    @Autowired
    private AppTokenRepo appTokenRepo;

    public boolean authorize(String token){
        return appTokenRepo.existsByAppToken(token);
    }

    public AppToken getByToken(String token) {
        return appTokenRepo.findByAppToken(token);
    }
}
