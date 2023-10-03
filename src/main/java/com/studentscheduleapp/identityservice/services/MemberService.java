package com.studentscheduleapp.identityservice.services;


import com.studentscheduleapp.identityservice.api.models.MailRequest;
import com.studentscheduleapp.identityservice.domain.models.Member;
import com.studentscheduleapp.identityservice.domain.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerErrorException;

import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.List;

@Service
public class MemberService {

    @Value("${ip.groupservice}")
    private String groupService;

    public Member getById(long id) throws Exception {
        ResponseEntity<Member> r = new RestTemplate().getForEntity(groupService + "/api/member/" + id, Member.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        if(r.getStatusCode().equals(HttpStatus.NOT_FOUND))
            return null;
        if(r.getStatusCode().isError())
            throw new Exception("request to " + groupService + " return code " + r.getStatusCode());
        return null;
    }
    public List<Member> getByUser(long id) throws Exception {
        ResponseEntity<List> r = new RestTemplate().getForEntity(groupService + "/api/member/user" + id, List.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        if(r.getStatusCode().equals(HttpStatus.NOT_FOUND))
            return null;
        if(r.getStatusCode().isError())
            throw new Exception("request to " + groupService + " return code " + r.getStatusCode());
        return null;
    }

}