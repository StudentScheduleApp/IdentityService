package com.studentscheduleapp.identityservice.api;

import com.studentscheduleapp.identityservice.models.Role;
import com.studentscheduleapp.identityservice.models.User;
import com.studentscheduleapp.identityservice.models.api.*;
import com.studentscheduleapp.identityservice.services.*;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
    private static final Logger log = LogManager.getLogger(IdentityController.class);
    private final Map<String, User> verifyUserCache = new HashMap<>();

    @PostMapping("${mapping.user.login}")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtLoginRequest authRequest) {
        if(authRequest.getEmail() == null || authRequest.getEmail().isEmpty()) {
            log.warn("bad request: JwtLoginRequest email is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(authRequest.getPassword() == null || authRequest.getPassword().isEmpty()) {
            log.warn("bad request: JwtLoginRequest password is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            final JwtResponse token = userTokenService.login(authRequest);
            log.info("login for " + token.getId() + " successful");
            return ResponseEntity.ok(token);
        } catch (AuthException e){
            log.warn("login fo " + authRequest.getEmail() + " failed: unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            log.error("login fo " + authRequest.getEmail() + " failed:" + errors);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("${mapping.user.refresh}")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        if(request.getRefreshToken() == null || request.getRefreshToken().isEmpty()) {
            log.warn("bad request: RefreshJwtRequest refreshToken is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            final JwtResponse token = userTokenService.refresh(request.getRefreshToken());
            log.info("refresh for " + token.getId() + " successful");
            return ResponseEntity.ok(token);
        } catch (AuthException e){
            log.warn("refresh failed: unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            log.error("refresh failed:" + errors);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("${mapping.user.register}")
    public ResponseEntity<Void> register(@RequestBody JwtRegisterRequest authRequest) {
        if(authRequest.getEmail() == null || authRequest.getEmail().isEmpty()) {
            log.warn("bad request: JwtRegisterRequest email is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(authRequest.getPassword() == null || authRequest.getPassword().isEmpty()) {
            log.warn("bad request: JwtRegisterRequest password is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(authRequest.getFirstName() == null || authRequest.getFirstName().isEmpty()) {
            log.warn("bad request: JwtRegisterRequest firstName is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(authRequest.getLastName() == null || authRequest.getLastName().isEmpty()) {
            log.warn("bad request: JwtRegisterRequest lastName is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(authRequest.getEmail().length() > 255) {
            log.warn("bad request: JwtRegisterRequest email length > 255");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(authRequest.getPassword().length() > 255) {
            log.warn("bad request: JwtRegisterRequest password length > 255");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(authRequest.getFirstName().length() > 255) {
            log.warn("bad request: JwtRegisterRequest firstName length > 255");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(authRequest.getLastName().length() > 255) {
            log.warn("bad request: JwtRegisterRequest lastName length > 255");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        User usr;
        try {
            usr = userService.getByEmail(authRequest.getEmail());
        } catch (Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            log.error("register failed:" + errors);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        if(usr == null){
            User u = new User(0L, authRequest.getEmail(), authRequest.getPassword(), authRequest.getFirstName(), authRequest.getLastName(), false, null, Collections.singletonList(Role.USER));
            verifyUserCache.put(u.getEmail(), u);
            try {
                verifyEmailService.sendCode(u.getEmail());
                log.info("verify code for email " + authRequest.getEmail() + " send successful");
            } catch (Exception e) {
                StringWriter errors = new StringWriter();
                e.printStackTrace(new PrintWriter(errors));
                log.error("register failed: email " + authRequest.getEmail() + " not send successful: " + errors);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            return ResponseEntity.ok().build();
        }
        else {
            log.info("register failed: email " + authRequest.getEmail() + " is busy");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

    }

    @PostMapping("${mapping.user.verify}")
    public ResponseEntity<JwtResponse> verify(@RequestBody VerifyEmailRequest verifyEmailRequest){
        if(verifyEmailRequest.getEmail() == null || verifyEmailRequest.getEmail().isEmpty()) {
            log.warn("bad request: verify email is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(verifyEmailRequest.getCode() == 0) {
            log.warn("bad request: verify code is 0");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (verifyUserCache.get(verifyEmailRequest.getEmail()) != null){
            if(verifyEmailService.verify(verifyEmailRequest)){
                User u = verifyUserCache.get(verifyEmailRequest.getEmail());
                try {
                    if (userService.create(u) == null)
                        return ResponseEntity.status(HttpStatus.CONFLICT).build();
                } catch (Exception e) {
                    StringWriter errors = new StringWriter();
                    e.printStackTrace(new PrintWriter(errors));
                    log.error("verify failed: " + errors);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
                try {
                    final JwtResponse token = userTokenService.login(new JwtLoginRequest(u.getEmail(), u.getPassword()));
                    log.info("verify and register successful");
                    return ResponseEntity.ok(token);
                } catch (AuthException e){
                    log.error("verify failed: unauthorized");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                } catch (Exception e) {
                    StringWriter errors = new StringWriter();
                    e.printStackTrace(new PrintWriter(errors));
                    log.error("verify failed: " + errors);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }
            log.warn("verify failed: code for email " + verifyEmailRequest.getEmail() + " not match with code in cache");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.warn("verify failed: email " + verifyEmailRequest.getEmail() + " not found in cache");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }

    @PostMapping("${mapping.user.authorize}")
    public ResponseEntity<Boolean> authorizeUser(@RequestBody AuthorizeUserRequest authorizeUserRequest){
        if(authorizeUserRequest.getUserToken() == null || authorizeUserRequest.getUserToken().isEmpty()) {
            log.warn("bad request: AuthorizeUserRequest userToken is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(authorizeUserRequest.getAuthorizeEntity() == null) {
            log.warn("bad request: AuthorizeUserRequest authorizeEntity is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(authorizeUserRequest.getAuthorizeEntity().getEntity() == null) {
            log.warn("bad request: AuthorizeUserRequest AuthorizeEntity entity is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(authorizeUserRequest.getAuthorizeEntity().getType() == null) {
            log.warn("bad request: AuthorizeUserRequest type is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(!authorizeUserService.authorize(authorizeUserRequest.getUserToken(), authorizeUserRequest.getAuthorizeEntity()))
            return ResponseEntity.ok(false);
        return ResponseEntity.ok(true);
    }

    @PostMapping("${mapping.user.getIdByToken}")
    public ResponseEntity<Long> getByToken(@RequestBody String token){
        if(token == null || token.isEmpty()) {
            log.warn("bad request: user token is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        User user;
        try {
            user = userTokenService.getUserByToken(token);
        } catch (Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            log.error("get user by token failed: " + errors);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        if (user == null) {
            log.error("get user by token failed: user not found");
            return ResponseEntity.ok(0L);
        }
        log.info("get user by token success: user id: " + user.getId());
        return ResponseEntity.ok(user.getId());
    }

}