package com.studentscheduleapp.identityservice.services;


import com.studentscheduleapp.identityservice.domain.models.Member;
import com.studentscheduleapp.identityservice.domain.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {

    @Value("${ip.userservice}")
    private String userService;

    public User getByEmail(String email) throws Exception {
        ResponseEntity<User> r = new RestTemplate().getForEntity(userService + "/api/user/" + email, User.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        if(r.getStatusCode().equals(HttpStatus.NOT_FOUND))
            return null;
        if(r.getStatusCode().isError())
            throw new Exception("request to " + userService + " return code " + r.getStatusCode());
        return null;
    }
    public User getById(long id) throws Exception {
        ResponseEntity<User> r = new RestTemplate().getForEntity(userService + "/api/user/" + id, User.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        if(r.getStatusCode().equals(HttpStatus.NOT_FOUND))
            return null;
        if(r.getStatusCode().isError())
            throw new Exception("request to " + userService + " return code " + r.getStatusCode());
        return null;
    }
    public User create(User user) throws Exception {
        ResponseEntity<User> r = new RestTemplate().postForEntity(userService + "/api/user/create", user, User.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        if(r.getStatusCode().isError())
            throw new Exception("request to " + userService + " return code " + r.getStatusCode());
        return null;
    }

}