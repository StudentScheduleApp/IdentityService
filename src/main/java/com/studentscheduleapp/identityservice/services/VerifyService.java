package com.studentscheduleapp.identityservice.services;

import com.studentscheduleapp.identityservice.api.models.MailRequest;
import com.studentscheduleapp.identityservice.api.models.VerifyRequest;
import com.studentscheduleapp.identityservice.http.HeaderRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VerifyService {


    private Map<String, Long> emailCodes = new HashMap<>();

    @Value("${ip.mailservice}")
    private String mailService;

    @Autowired
    private RestTemplate restTemplate;


    public void sendCode(String email) throws Exception {
        long code = Math.round(Math.random() * 100000);
        ResponseEntity<Void> r = restTemplate.postForEntity(mailService + "/api/send", new MailRequest(email, "Verify email", String.valueOf(code)), Void.class);
        if(r.getStatusCode().is2xxSuccessful())
            emailCodes.put(email, code);
        if(r.getStatusCode().isError())
            throw new Exception("request to " + mailService + " return code " + r.getStatusCode());
        throw new Exception();
    }

    public boolean verify(VerifyRequest verifyRequest){
        if(emailCodes.get(verifyRequest.getEmail()) != null && emailCodes.get(verifyRequest.getEmail()).equals(verifyRequest.getCode())){
            emailCodes.remove(verifyRequest.getEmail());
            return true;
        }
        return false;
    }

}
