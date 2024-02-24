package com.studentscheduleapp.identityservice.repos;

import com.studentscheduleapp.identityservice.models.Group;
import com.studentscheduleapp.identityservice.properties.services.DatabaseServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class GroupRepository {



    @Autowired
    private DatabaseServiceProperties databaseServiceProperties;

    @Autowired
    private RestTemplate restTemplate;

    public Group getById(long id) throws Exception {
        ResponseEntity<Group> r = restTemplate.getForEntity(databaseServiceProperties.getUri() + databaseServiceProperties.getGetGroupByIdPath() + "/" + id, Group.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        throw new Exception("request to " + databaseServiceProperties.getUri() + " return code " + r.getStatusCode());
    }
    public Group save(Group group) throws Exception {
        ResponseEntity<Group> r = restTemplate.postForEntity(databaseServiceProperties.getUri() + databaseServiceProperties.getSaveGroupPath(), group, Group.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        throw new Exception("request to " + databaseServiceProperties.getUri() + " return code " + r.getStatusCode());
    }
    public void delete(long id) throws Exception {
        ResponseEntity<Void> r = restTemplate.getForEntity(databaseServiceProperties.getUri() + databaseServiceProperties.getDeleteGroupPath() + "/" + id, Void.class);
        if(!r.getStatusCode().is2xxSuccessful())
            throw new Exception("request to " + databaseServiceProperties.getUri() + " return code " + r.getStatusCode());
    }
}
