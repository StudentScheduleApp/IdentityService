package com.studentscheduleapp.identityservice.repos;

import com.studentscheduleapp.identityservice.models.api.SendMailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class MailRepository {


    @Value("${ip.mailservice}")
    private String mailService;

    @Autowired
    private RestTemplate restTemplate;


    public boolean send(SendMailRequest mail) throws Exception {
        ResponseEntity<Void> r = restTemplate.postForEntity(mailService + "/api/send", mail, Void.class);
        if(r.getStatusCode().is2xxSuccessful())
            return true;
        if(r.getStatusCode().equals(HttpStatus.NOT_FOUND))
            return false;
        if(r.getStatusCode().isError())
            throw new Exception("request to " + mailService + " return code " + r.getStatusCode());
        return false;
    }
}
