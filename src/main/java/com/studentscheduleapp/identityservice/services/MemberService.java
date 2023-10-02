package com.studentscheduleapp.identityservice.services;


import com.studentscheduleapp.identityservice.api.models.MailRequest;
import com.studentscheduleapp.identityservice.domain.models.Member;
import com.studentscheduleapp.identityservice.domain.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberService {

    @Value("${ip.groupservice}")
    private String groupService;

    public Member getById(long id) {
        ResponseEntity<Member> r = new RestTemplate().getForEntity(groupService + "/api/member/" + id, Member.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        return null;
    }
    public List<Member> getByUser(long id) {
        ResponseEntity<List<Member>> r = new RestTemplate().getForEntity(groupService + "/api/member/user" + id, (Class<List<Member>>) new ArrayList<Member>().getClass());
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        return null;
    }

}