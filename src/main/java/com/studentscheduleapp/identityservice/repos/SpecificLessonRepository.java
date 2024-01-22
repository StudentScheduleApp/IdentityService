package com.studentscheduleapp.identityservice.repos;

import com.studentscheduleapp.identityservice.models.SpecificLesson;
import com.studentscheduleapp.identityservice.properties.services.DatabaseServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Repository
public class SpecificLessonRepository {



    @Autowired
    private DatabaseServiceProperties databaseServiceProperties;

    @Autowired
    private RestTemplate restTemplate;

    public SpecificLesson getById(long id) throws Exception {
        ResponseEntity<SpecificLesson> r = restTemplate.getForEntity(databaseServiceProperties.getUri() + databaseServiceProperties.getGetSpecificLessonByIdPath() + id, SpecificLesson.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        if(r.getStatusCode().equals(HttpStatus.NOT_FOUND))
            return null;
        throw new Exception("request to " + databaseServiceProperties.getUri() + " return code " + r.getStatusCode());
    }
    public List<SpecificLesson> getByGroupId(long id) throws Exception {
        ResponseEntity<List> r = restTemplate.getForEntity(databaseServiceProperties.getUri() + databaseServiceProperties.getGetSpecificLessonByGroupIdPath() + id, List.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        if(r.getStatusCode().equals(HttpStatus.NOT_FOUND))
            return null;
        throw new Exception("request to " + databaseServiceProperties.getUri() + " return code " + r.getStatusCode());
    }
    public SpecificLesson save(SpecificLesson customLesson) throws Exception {
        ResponseEntity<SpecificLesson> r = restTemplate.postForEntity(databaseServiceProperties.getUri() + databaseServiceProperties.getSaveSpecificLessonPath(), customLesson, SpecificLesson.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        if (r.getStatusCode().equals(HttpStatus.CONFLICT))
            return null;
        throw new Exception("request to " + databaseServiceProperties.getUri() + " return code " + r.getStatusCode());
    }
    public boolean delete(long id) throws Exception {
        ResponseEntity<Void> r = restTemplate.getForEntity(databaseServiceProperties.getUri() + databaseServiceProperties.getDeleteSpecificLessonPath() + id, Void.class);
        if(r.getStatusCode().is2xxSuccessful())
            return true;
        throw new Exception("request to " + databaseServiceProperties.getUri() + " return code " + r.getStatusCode());
    }
}
