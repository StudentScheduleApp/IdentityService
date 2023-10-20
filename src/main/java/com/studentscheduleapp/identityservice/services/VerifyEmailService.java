package com.studentscheduleapp.identityservice.services;

import com.studentscheduleapp.identityservice.models.api.SendMailRequest;
import com.studentscheduleapp.identityservice.models.api.VerifyEmailRequest;
import com.studentscheduleapp.identityservice.repos.MailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class VerifyEmailService {


    private Map<String, Long> emailCodes = new HashMap<>();

    @Autowired
    private MailRepository mailRepository;


    public void sendCode(String email) throws Exception {
        long code = Math.round(Math.random() * 100000);
        mailRepository.send(new SendMailRequest(email, "Verify email", "code: " + code));
        emailCodes.put(email, code);
    }

    public boolean verify(VerifyEmailRequest verifyEmailRequest){
        if(emailCodes.get(verifyEmailRequest.getEmail()) != null && emailCodes.get(verifyEmailRequest.getEmail()).equals(verifyEmailRequest.getCode())){
            emailCodes.remove(verifyEmailRequest.getEmail());
            return true;
        }
        return false;
    }

}
