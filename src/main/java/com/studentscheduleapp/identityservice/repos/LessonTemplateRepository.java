package com.studentscheduleapp.identityservice.repos;

import com.studentscheduleapp.identityservice.models.CustomLesson;
import com.studentscheduleapp.identityservice.models.LessonTemplate;
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
public class LessonTemplateRepository {




    @Autowired
    private DatabaseServiceProperties databaseServiceProperties;

    @Autowired
    private RestTemplate restTemplate;

    public LessonTemplate getById(long id) throws Exception {
        ResponseEntity<LessonTemplate> r = restTemplate.getForEntity(databaseServiceProperties.getUri() + databaseServiceProperties.getGetLessonTemplateByIdPath() + "/" + id, LessonTemplate.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        if(r.getStatusCode().equals(HttpStatus.NOT_FOUND))
            return null;
        throw new Exception("request to " + databaseServiceProperties.getUri() + " return code " + r.getStatusCode());
    }
    public List<LessonTemplate> getByScheduleTemplateId(long id) throws Exception {
        ResponseEntity<LessonTemplate[]> r = restTemplate.getForEntity(databaseServiceProperties.getUri() + databaseServiceProperties.getGetLessonTemplateByScheduleTemplateIdPath() + "/" + id, LessonTemplate[].class);
        if(r.getStatusCode().is2xxSuccessful())
            return Arrays.asList(r.getBody());
        if(r.getStatusCode().equals(HttpStatus.NOT_FOUND))
            return null;
        throw new Exception("request to " + databaseServiceProperties.getUri() + " return code " + r.getStatusCode());
    }
    public LessonTemplate save(LessonTemplate lessonTemplate) throws Exception {
        ResponseEntity<LessonTemplate> r = restTemplate.postForEntity(databaseServiceProperties.getUri() + databaseServiceProperties.getSaveLessonTemplatePath(), lessonTemplate, LessonTemplate.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        if (r.getStatusCode().equals(HttpStatus.CONFLICT))
            return null;
        throw new Exception("request to " + databaseServiceProperties.getUri() + " return code " + r.getStatusCode());
    }
    public boolean delete(long id) throws Exception {
        ResponseEntity<Void> r = restTemplate.getForEntity(databaseServiceProperties.getUri() + databaseServiceProperties.getDeleteLessonTemplatePath() + "/" + id, Void.class);
        if(r.getStatusCode().is2xxSuccessful())
            return true;
        throw new Exception("request to " + databaseServiceProperties.getUri() + " return code " + r.getStatusCode());
    }
}
