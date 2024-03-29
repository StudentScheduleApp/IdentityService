package com.studentscheduleapp.identityservice.repos;

import com.studentscheduleapp.identityservice.models.OutlineMedia;
import com.studentscheduleapp.identityservice.models.OutlineMediaComment;
import com.studentscheduleapp.identityservice.properties.services.DatabaseServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class OutlineMediaRepository {




    @Autowired
    private DatabaseServiceProperties databaseServiceProperties;


    @Autowired
    private RestTemplate restTemplate;

    public OutlineMedia getById(long id) throws Exception {
        ResponseEntity<OutlineMedia> r = restTemplate.getForEntity(databaseServiceProperties.getUri() + databaseServiceProperties.getGetOutlineMediaByIdPath() + "/" + id, OutlineMedia.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        throw new Exception("request to " + databaseServiceProperties.getUri() + " return code " + r.getStatusCode());
    }
    public List<OutlineMedia> getByOutlineId(long id) throws Exception {
        ResponseEntity<OutlineMedia[]> r = restTemplate.getForEntity(databaseServiceProperties.getUri() + databaseServiceProperties.getGetOutlineMediaByOutlineIdPath() + "/" + id, OutlineMedia[].class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody() == null ? null : new ArrayList<>(Arrays.asList(r.getBody()));
        throw new Exception("request to " + databaseServiceProperties.getUri() + " return code " + r.getStatusCode());
    }
    public OutlineMedia save(OutlineMedia customLesson) throws Exception {
        ResponseEntity<OutlineMedia> r = restTemplate.postForEntity(databaseServiceProperties.getUri() + databaseServiceProperties.getSaveOutlineMediaPath(), customLesson, OutlineMedia.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        throw new Exception("request to " + databaseServiceProperties.getUri() + " return code " + r.getStatusCode());
    }
    public void delete(long id) throws Exception {
        ResponseEntity<Void> r = restTemplate.getForEntity(databaseServiceProperties.getUri() + databaseServiceProperties.getDeleteOutlineMediaPath() + "/" + id, Void.class);
        if(!r.getStatusCode().is2xxSuccessful())
            throw new Exception("request to " + databaseServiceProperties.getUri() + " return code " + r.getStatusCode());
    }
}
