package com.studentscheduleapp.identityservice.repos;

import com.studentscheduleapp.identityservice.models.Group;
import com.studentscheduleapp.identityservice.models.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class JwtRefreshTokenRepository {

    @Value("${ip.databasetokenservice}")
    private String databaseService;

    @Autowired
    private RestTemplate restTemplate;

    public boolean save(RefreshToken token) throws Exception{
        ResponseEntity<Void> r = restTemplate.postForEntity(databaseService + "/api/refresh/save", token, Void.class);
        if(r.getStatusCode().is2xxSuccessful())
            return true;
        throw new Exception("request to " + databaseService + " return code " + r.getStatusCode());
    }
    public boolean delete(String email) throws Exception{
        ResponseEntity<Void> r = restTemplate.getForEntity(databaseService + "/api/refresh/delete/" + email, Void.class);
        if(r.getStatusCode().is2xxSuccessful())
            return true;
        throw new Exception("request to " + databaseService + " return code " + r.getStatusCode());
    }
    public String get(String email) throws Exception{
        ResponseEntity<String> r = restTemplate.getForEntity(databaseService + "/api/refresh/" + email, String.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        if(r.getStatusCode().equals(HttpStatus.NOT_FOUND))
            return null;
        throw new Exception("request to " + databaseService + " return code " + r.getStatusCode());
    }

}
