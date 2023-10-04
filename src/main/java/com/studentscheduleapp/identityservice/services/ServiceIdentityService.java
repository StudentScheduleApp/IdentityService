package com.studentscheduleapp.identityservice.services;

import com.studentscheduleapp.identityservice.domain.models.ServiceToken;
import com.studentscheduleapp.identityservice.repos.ServiceTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceIdentityService {
    @Autowired
    private ServiceTokenRepo serviceTokenRepo;

    public boolean authorize(String token){
        return serviceTokenRepo.existsByServiceToken(token);
    }

    public ServiceToken getByToken(String token) {
        return serviceTokenRepo.findByServiceToken(token);
    }
}
