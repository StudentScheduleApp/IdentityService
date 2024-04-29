package com.studentscheduleapp.identityservice.services;


import com.studentscheduleapp.identityservice.models.User;
import com.studentscheduleapp.identityservice.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return userRepository.save(user);
    }

}