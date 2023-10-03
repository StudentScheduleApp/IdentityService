package com.studentscheduleapp.identityservice.services;

import com.studentscheduleapp.identityservice.api.models.MailRequest;
import com.studentscheduleapp.identityservice.api.models.VerifyRequest;
import com.studentscheduleapp.identityservice.domain.models.Authorize;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthorizeService {

    public boolean authorize(String token, Authorize authorize){
        return true;
    }

}
