package com.studentscheduleapp.identityservice.repos;

import com.studentscheduleapp.identityservice.models.api.SendMailRequest;
import com.studentscheduleapp.identityservice.properties.services.MailServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class MailRepository {


    @Autowired
    private MailServiceProperties mailServiceProperties;
    @Autowired
    private RestTemplate restTemplate;


    public boolean send(SendMailRequest mail) throws Exception {
        ResponseEntity<Void> r = restTemplate.postForEntity(mailServiceProperties.getUri()+ mailServiceProperties.getSendPath(), mail, Void.class);
        if(r.getStatusCode().is2xxSuccessful())
            return true;
        if(r.getStatusCode().equals(HttpStatus.NOT_FOUND))
            return false;
        throw new Exception("request to " + mailServiceProperties.getUri() + " return code " + r.getStatusCode());
    }
}
