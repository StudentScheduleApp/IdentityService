package com.studentscheduleapp.identityservice.services;


import com.studentscheduleapp.identityservice.domain.models.Member;
import com.studentscheduleapp.identityservice.domain.models.User;
import com.studentscheduleapp.identityservice.http.HeaderRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Value("${ip.userservice}")
    private String userService;

    public User getByEmail(String email) throws Exception {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new HeaderRequestInterceptor());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(interceptors);
        ResponseEntity<User> r = restTemplate.getForEntity(userService + "/api/user/" + email, User.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        if(r.getStatusCode().equals(HttpStatus.NOT_FOUND))
            return null;
        if(r.getStatusCode().isError())
            throw new Exception("request to " + userService + " return code " + r.getStatusCode());
        return null;
    }
    public User getById(long id) throws Exception {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new HeaderRequestInterceptor());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(interceptors);
        ResponseEntity<User> r = restTemplate.getForEntity(userService + "/api/user/" + id, User.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        if(r.getStatusCode().equals(HttpStatus.NOT_FOUND))
            return null;
        if(r.getStatusCode().isError())
            throw new Exception("request to " + userService + " return code " + r.getStatusCode());
        return null;
    }
    public User create(User user) throws Exception {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new HeaderRequestInterceptor());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(interceptors);
        ResponseEntity<User> r = restTemplate.postForEntity(userService + "/api/user/create", user, User.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        if(r.getStatusCode().isError())
            throw new Exception("request to " + userService + " return code " + r.getStatusCode());
        return null;
    }

}