package com.studentscheduleapp.identityservice.services;


import com.studentscheduleapp.identityservice.domain.models.User;
import com.studentscheduleapp.identityservice.http.HeaderRequestInterceptor;
import com.studentscheduleapp.identityservice.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getByEmail(String email) throws Exception {
        return userRepository.getByEmail(email);
    }
    public User getById(long id) throws Exception {
        return userRepository.getById(id);
    }
    public User create(User user) throws Exception {
        return userRepository.create(user);
    }

}