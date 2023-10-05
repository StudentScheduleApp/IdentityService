package com.studentscheduleapp.identityservice.services;

import com.studentscheduleapp.identityservice.api.models.SendMailRequest;
import com.studentscheduleapp.identityservice.api.models.VerifyEmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class VerifyEmailService {


    private Map<String, Long> emailCodes = new HashMap<>();

    @Value("${ip.mailservice}")
    private String mailService;

    @Autowired
    private RestTemplate restTemplate;


    public void sendCode(String email) throws Exception {
        long code = Math.round(Math.random() * 100000);
        ResponseEntity<Void> r = restTemplate.postForEntity(mailService + "/api/send", new SendMailRequest(email, "Verify email", String.valueOf(code)), Void.class);
        if(r.getStatusCode().is2xxSuccessful())
            emailCodes.put(email, code);
        if(r.getStatusCode().isError())
            throw new Exception("request to " + mailService + " return code " + r.getStatusCode());
        throw new Exception();
    }

    public boolean verify(VerifyEmailRequest verifyEmailRequest){
        if(emailCodes.get(verifyEmailRequest.getEmail()) != null && emailCodes.get(verifyEmailRequest.getEmail()).equals(verifyEmailRequest.getCode())){
            emailCodes.remove(verifyEmailRequest.getEmail());
            return true;
        }
        return false;
    }

}
