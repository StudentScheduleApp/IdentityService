package com.studentscheduleapp.identityservice.repos;

import com.studentscheduleapp.identityservice.models.RefreshToken;
import com.studentscheduleapp.identityservice.properties.services.DatabaseTokenServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class JwtRefreshTokenRepository {

    @Autowired
    private DatabaseTokenServiceProperties databaseTokenServiceProperties;

    @Autowired
    private RestTemplate restTemplate;

    public boolean save(RefreshToken token) throws Exception {
        ResponseEntity<Void> r = restTemplate.postForEntity(databaseTokenServiceProperties.getUri() + databaseTokenServiceProperties.getSavePath(), token, Void.class);
        if (r.getStatusCode().is2xxSuccessful())
            return true;
        throw new Exception("request to " + databaseTokenServiceProperties.getUri() + " return code " + r.getStatusCode());
    }

    public void delete(String email) throws Exception {
        ResponseEntity<Void> r = restTemplate.getForEntity(databaseTokenServiceProperties.getUri() + databaseTokenServiceProperties.getDeletePath() + "/" + email, Void.class);
        if (!r.getStatusCode().is2xxSuccessful())
            throw new Exception("request to " + databaseTokenServiceProperties.getUri() + " return code " + r.getStatusCode());
    }

    public String get(String email) throws Exception {
        ResponseEntity<String> r = restTemplate.getForEntity(databaseTokenServiceProperties.getUri() + databaseTokenServiceProperties.getGetPath() + "/" + email, String.class);
        if (r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        throw new Exception("request to " + databaseTokenServiceProperties.getUri() + " return code " + r.getStatusCode());
    }

}
