package com.studentscheduleapp.identityservice.repos;

import com.studentscheduleapp.identityservice.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class UserRepository {


    @Value("${ip.userservice}")
    private String userService;

    @Autowired
    private RestTemplate restTemplate;

    public User getByEmail(String email) throws Exception {
        ResponseEntity<User> r = restTemplate.getForEntity(userService + "/api/user/" + email, User.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        if(r.getStatusCode().equals(HttpStatus.NOT_FOUND))
            return null;
        throw new Exception("request to " + userService + " return code " + r.getStatusCode());
    }
    public User getById(long id) throws Exception {
        ResponseEntity<User> r = restTemplate.getForEntity(userService + "/api/user/" + id, User.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        if(r.getStatusCode().equals(HttpStatus.NOT_FOUND))
            return null;
        throw new Exception("request to " + userService + " return code " + r.getStatusCode());
    }
    public User create(User user) throws Exception {
        ResponseEntity<User> r = restTemplate.postForEntity(userService + "/api/user/create", user, User.class);
        if(r.getStatusCode().is2xxSuccessful())
            return r.getBody();
        if (r.getStatusCode().equals(HttpStatus.CONFLICT))
            return null;
        throw new Exception("request to " + userService + " return code " + r.getStatusCode());
    }
}
