package com.studentscheduleapp.identityservice.services;

import com.studentscheduleapp.identityservice.repos.ServiceTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizeServiceService {
    @Autowired
    private ServiceTokenRepository serviceTokenRepository;

    public boolean authorize(String token){
        return serviceTokenRepository.existsByServiceToken(token);
    }

}
