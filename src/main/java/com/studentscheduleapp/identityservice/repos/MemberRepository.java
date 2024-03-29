package com.studentscheduleapp.identityservice.repos;

import com.studentscheduleapp.identityservice.models.Member;
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
public class MemberRepository {



    @Autowired
    private DatabaseServiceProperties databaseServiceProperties;

    @Autowired
    private RestTemplate restTemplate;

    public Member getById(long id) throws Exception {
        ResponseEntity<Member> r = restTemplate.getForEntity(databaseServiceProperties.getUri() + databaseServiceProperties.getGetMemberByIdPath() + "/" + id, Member.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        throw new Exception("request to " + databaseServiceProperties.getUri() + " return code " + r.getStatusCode());
    }
    public List<Member> getByGroupId(long id) throws Exception {
        ResponseEntity<Member[]> r = restTemplate.getForEntity(databaseServiceProperties.getUri() + databaseServiceProperties.getGetMemberByGroupIdPath() + "/" + id, Member[].class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody() == null ? null : new ArrayList<>(Arrays.asList(r.getBody()));
        throw new Exception("request to " + databaseServiceProperties.getUri() + " return code " + r.getStatusCode());
    }
    public List<Member> getByUserId(long id) throws Exception {
        ResponseEntity<Member[]> r = restTemplate.getForEntity(databaseServiceProperties.getUri() + databaseServiceProperties.getGetMemberByUserIdPath() + "/" + id, Member[].class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody() == null ? null : new ArrayList<>(Arrays.asList(r.getBody()));
        throw new Exception("request to " + databaseServiceProperties.getUri() + " return code " + r.getStatusCode());
    }
    public Member save(Member member) throws Exception {
        ResponseEntity<Member> r = restTemplate.postForEntity(databaseServiceProperties.getUri() + databaseServiceProperties.getSaveMemberPath(), member, Member.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        throw new Exception("request to " + databaseServiceProperties.getUri() + " return code " + r.getStatusCode());
    }
    public void delete(long id) throws Exception {
        ResponseEntity<Void> r = restTemplate.getForEntity(databaseServiceProperties.getUri() + databaseServiceProperties.getDeleteMemberPath() + "/" + id, Void.class);
        if(!r.getStatusCode().is2xxSuccessful())
            throw new Exception("request to " + databaseServiceProperties.getUri() + " return code " + r.getStatusCode());
    }
}
