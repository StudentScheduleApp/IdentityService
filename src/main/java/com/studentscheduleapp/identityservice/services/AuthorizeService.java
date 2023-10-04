package com.studentscheduleapp.identityservice.services;

import com.studentscheduleapp.identityservice.domain.models.Authorize;
import org.springframework.stereotype.Service;

@Service
public class AuthorizeService {

    public boolean authorize(String token, Authorize authorize){

        return true;
    }

}
