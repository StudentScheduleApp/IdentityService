package com.studentscheduleapp.identityservice.repos;

import com.studentscheduleapp.identityservice.models.LessonTemplate;
import com.studentscheduleapp.identityservice.models.OutlineMediaComment;
import com.studentscheduleapp.identityservice.properties.services.DatabaseServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Repository
public class OutlineMediaCommentRepository {



    @Autowired
    private DatabaseServiceProperties databaseServiceProperties;

    @Autowired
    private RestTemplate restTemplate;

    public OutlineMediaComment getById(long id) throws Exception {
        ResponseEntity<OutlineMediaComment> r = restTemplate.getForEntity(databaseServiceProperties.getUri() + databaseServiceProperties.getGetOutlineMediaCommentByIdPath() + "/" + id, OutlineMediaComment.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        throw new Exception("request to " + databaseServiceProperties.getUri() + " return code " + r.getStatusCode());
    }
    public List<OutlineMediaComment> getByOutlineMediaId(long id) throws Exception {
        ResponseEntity<OutlineMediaComment[]> r = restTemplate.getForEntity(databaseServiceProperties.getUri() + databaseServiceProperties.getGetOutlineMediaCommentByOutlineMediaIdPath() + "/" + id, OutlineMediaComment[].class);
        if(r.getStatusCode().is2xxSuccessful())
            return Arrays.asList(r.getBody());
        throw new Exception("request to " + databaseServiceProperties.getUri() + " return code " + r.getStatusCode());
    }
    public OutlineMediaComment save(OutlineMediaComment customLesson) throws Exception {
        ResponseEntity<OutlineMediaComment> r = restTemplate.postForEntity(databaseServiceProperties.getUri() + databaseServiceProperties.getSaveOutlineMediaCommentPath(), customLesson, OutlineMediaComment.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        throw new Exception("request to " + databaseServiceProperties.getUri() + " return code " + r.getStatusCode());
    }
    public boolean delete(long id) throws Exception {
        ResponseEntity<Void> r = restTemplate.getForEntity(databaseServiceProperties.getUri() + databaseServiceProperties.getDeleteOutlineMediaCommentPath() + "/" + id, Void.class);
        if(r.getStatusCode().is2xxSuccessful())
            return true;
        throw new Exception("request to " + databaseServiceProperties.getUri() + " return code " + r.getStatusCode());
    }
}
