package com.studentscheduleapp.identityservice.repos;

import com.studentscheduleapp.identityservice.models.Member;
import com.studentscheduleapp.identityservice.properties.services.DatabaseServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Repository
public class MemberRepository {



    @Autowired
    private DatabaseServiceProperties databaseServiceProperties;

    @Autowired
    private RestTemplate restTemplate;

    public Member getById(long id) throws Exception {
        ResponseEntity<Member> r = restTemplate.getForEntity(databaseServiceProperties.getUri() + databaseServiceProperties.getGetMemberByIdPath() + "/" + id, Member.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        if(r.getStatusCode().equals(HttpStatus.NOT_FOUND))
            return null;
        throw new Exception("request to " + databaseServiceProperties.getUri() + " return code " + r.getStatusCode());
    }
    public List<Member> getByGroupId(long id) throws Exception {
        ResponseEntity<List> r = restTemplate.getForEntity(databaseServiceProperties.getUri() + databaseServiceProperties.getGetMemberByGroupIdPath() + "/" + id, List.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        if(r.getStatusCode().equals(HttpStatus.NOT_FOUND))
            return null;
        throw new Exception("request to " + databaseServiceProperties.getUri() + " return code " + r.getStatusCode());
    }
    public List<Member> getByUserId(long id) throws Exception {
        ResponseEntity<List> r = restTemplate.getForEntity(databaseServiceProperties.getUri() + databaseServiceProperties.getGetMemberByUserIdPath() + "/" + id, List.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        if(r.getStatusCode().equals(HttpStatus.NOT_FOUND))
            return null;
        throw new Exception("request to " + databaseServiceProperties.getUri() + " return code " + r.getStatusCode());
    }
    public Member save(Member member) throws Exception {
        ResponseEntity<Member> r = restTemplate.postForEntity(databaseServiceProperties.getUri() + databaseServiceProperties.getSaveMemberPath(), member, Member.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        if (r.getStatusCode().equals(HttpStatus.CONFLICT))
            return null;
        throw new Exception("request to " + databaseServiceProperties.getUri() + " return code " + r.getStatusCode());
    }
    public boolean delete(long id) throws Exception {
        ResponseEntity<Void> r = restTemplate.getForEntity(databaseServiceProperties.getUri() + databaseServiceProperties.getDeleteMemberPath() + "/" + id, Void.class);
        if(r.getStatusCode().is2xxSuccessful())
            return true;
        throw new Exception("request to " + databaseServiceProperties.getUri() + " return code " + r.getStatusCode());
    }
}
