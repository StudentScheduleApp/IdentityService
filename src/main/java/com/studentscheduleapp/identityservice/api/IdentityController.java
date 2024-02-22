package com.studentscheduleapp.identityservice.api;

import com.studentscheduleapp.identityservice.models.Role;
import com.studentscheduleapp.identityservice.models.User;
import com.studentscheduleapp.identityservice.models.api.*;
import com.studentscheduleapp.identityservice.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequiredArgsConstructor
public class IdentityController {

    @Autowired
    private VerifyEmailService verifyEmailService;
    @Autowired
    private UserTokenService userTokenService;
    @Autowired
    private AuthorizeUserService authorizeUserService;
    @Autowired
    private AuthorizeServiceService authorizeServiceService;
    @Autowired
    private UserService userService;
    private final Map<String, User> verifyUserCache = new HashMap<>();

    @PostMapping("${mapping.user.login}")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtLoginRequest authRequest) {
        if(authRequest.getEmail() == null || authRequest.getEmail().isEmpty()) {
            Logger.getGlobal().info("bad request: email is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(authRequest.getPassword() == null || authRequest.getPassword().isEmpty()) {
            Logger.getGlobal().info("bad request: password is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            final JwtResponse token = userTokenService.login(authRequest);
            Logger.getGlobal().info("login fo " + token.getId() + " successful");
            return ResponseEntity.ok(token);
        } catch (AuthException e){
            Logger.getGlobal().info("login fo " + authRequest.getEmail() + " failed: unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.getGlobal().info("login fo " + authRequest.getEmail() + " failed:" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("${mapping.user.refresh}")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        if(request.getRefreshToken() == null || request.getRefreshToken().isEmpty()) {
            Logger.getGlobal().info("bad request: refreshToken is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            final JwtResponse token = userTokenService.refresh(request.getRefreshToken());
            Logger.getGlobal().info("refresh fo " + token.getId() + " successful");
            return ResponseEntity.ok(token);
        } catch (AuthException e){
            Logger.getGlobal().info("refresh failed: unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.getGlobal().info("refresh failed:" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("${mapping.user.register}")
    public ResponseEntity<Void> register(@RequestBody JwtRegisterRequest authRequest) {
        if(authRequest.getEmail() == null || authRequest.getEmail().isEmpty()) {
            Logger.getGlobal().info("bad request: email is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(authRequest.getPassword() == null || authRequest.getPassword().isEmpty()) {
            Logger.getGlobal().info("bad request: password is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(authRequest.getFirstName() == null || authRequest.getFirstName().isEmpty()) {
            Logger.getGlobal().info("bad request: firstName is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(authRequest.getLastName() == null || authRequest.getLastName().isEmpty()) {
            Logger.getGlobal().info("bad request: lastName is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(authRequest.getEmail() != null && authRequest.getEmail().length() > 255) {
            Logger.getGlobal().info("bad request: email length > 255");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(authRequest.getPassword() != null && authRequest.getPassword().length() > 255) {
            Logger.getGlobal().info("bad request: password length > 255");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(authRequest.getFirstName() != null && authRequest.getFirstName().length() > 255) {
            Logger.getGlobal().info("bad request: first name length > 255");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(authRequest.getLastName() != null && authRequest.getLastName().length() > 255) {
            Logger.getGlobal().info("bad request: last name length > 255");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        User usr = null;
        try {
            usr = userService.getByEmail(authRequest.getEmail());
        }  catch (Exception e) {
            e.printStackTrace();
            Logger.getGlobal().info("register failed:" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        if(usr == null){
            ArrayList<Role> roles = new ArrayList<>();
            roles.add(Role.USER);
            User u = new User(0L, authRequest.getEmail(), authRequest.getPassword(), authRequest.getFirstName(), authRequest.getLastName(), false, null, roles);
            verifyUserCache.put(u.getEmail(), u);
            try {
                verifyEmailService.sendCode(u.getEmail());
                Logger.getGlobal().info("verify code send successful");
            } catch (Exception e) {
                e.printStackTrace();
                Logger.getGlobal().info("register failed: email not send successful");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            return ResponseEntity.ok().build();
        }
        else {
            Logger.getGlobal().info("register failed: email s busy");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

    }

    @PostMapping("${mapping.user.verify}")
    public ResponseEntity<JwtResponse> verify(@RequestBody VerifyEmailRequest verifyEmailRequest){
        if(verifyEmailRequest.getEmail() == null || verifyEmailRequest.getEmail().isEmpty()) {
            Logger.getGlobal().info("bad request: email is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(verifyEmailRequest.getCode() == 0) {
            Logger.getGlobal().info("bad request: code is 0");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (verifyUserCache.get(verifyEmailRequest.getEmail()) != null){
            if(verifyEmailService.verify(verifyEmailRequest)){
                User u = verifyUserCache.get(verifyEmailRequest.getEmail());
                try {
                    if (userService.create(u) == null)
                        return ResponseEntity.status(HttpStatus.CONFLICT).build();
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
                try {
                    final JwtResponse token = userTokenService.login(new JwtLoginRequest(u.getEmail(), u.getPassword()));
                    Logger.getGlobal().info("verify and register successful");
                    return ResponseEntity.ok(token);
                } catch (AuthException e){
                    Logger.getGlobal().info("verify failed: unauthorized");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.getGlobal().info("verify failed:" + e.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }
            Logger.getGlobal().info("verify failed: code for email " + verifyEmailRequest.getEmail() + " not match with code in cache");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Logger.getGlobal().info("verify failed: email " + verifyEmailRequest.getEmail() + " not found in cache");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }

    @PostMapping("${mapping.user.authorize}")
    public ResponseEntity<Boolean> authorizeUser(@RequestBody AuthorizeUserRequest authorizeUserRequest){
        if(authorizeUserRequest.getUserToken() == null || authorizeUserRequest.getUserToken().isEmpty()) {
            Logger.getGlobal().info("bad request: user token is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(authorizeUserRequest.getAuthorizeEntity() == null) {
            Logger.getGlobal().info("bad request: authorize data is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(authorizeUserRequest.getAuthorizeEntity().getEntity() == null) {
            Logger.getGlobal().info("bad request: entity is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(authorizeUserRequest.getAuthorizeEntity().getType() == null) {
            Logger.getGlobal().info("bad request: type is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if(!authorizeUserService.authorize(authorizeUserRequest.getUserToken(), authorizeUserRequest.getAuthorizeEntity()))
            return ResponseEntity.ok(false);
        return ResponseEntity.ok(true);
    }

    @PostMapping("${mapping.user.getByToken}")
    public ResponseEntity<User> getByToken(@RequestBody String token){
        if(token == null || token.isEmpty()) {
            Logger.getGlobal().info("bad request: user token is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        User user = null;
        try {
            user = userTokenService.getUserByToken(token);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.getGlobal().info("get user by token failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return user == null ? ResponseEntity.status(HttpStatus.NOT_FOUND).build() : ResponseEntity.ok(user);
    }

}