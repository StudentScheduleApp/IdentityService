package com.studentscheduleapp.identityservice.services;

import com.studentscheduleapp.identityservice.models.AuthorizeEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthorizeUserService {

    public boolean authorize(String token, AuthorizeEntity authorizeEntity){

        return true;
    }

}
