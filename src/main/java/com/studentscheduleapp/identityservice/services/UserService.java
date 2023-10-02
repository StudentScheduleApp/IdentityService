package com.studentscheduleapp.identityservice.services;


import com.studentscheduleapp.identityservice.domain.models.Member;
import com.studentscheduleapp.identityservice.domain.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {

    @Value("${ip.userservice}")
    private String userService;

    public User getByEmail(String email) {
        ResponseEntity<User> r = new RestTemplate().getForEntity(userService + "/api/user/" + email, User.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        return null;
    }
    public User getById(long id) {
        ResponseEntity<User> r = new RestTemplate().getForEntity(userService + "/api/user/" + id, User.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        return null;
    }
    public User create(User user) {
        ResponseEntity<User> r = new RestTemplate().postForEntity(userService + "/api/user/create", user, User.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        return null;
    }

}