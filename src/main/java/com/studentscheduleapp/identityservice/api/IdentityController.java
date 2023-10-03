package com.studentscheduleapp.identityservice.api;

import com.studentscheduleapp.identityservice.api.models.AppAuthorizeRequest;
import com.studentscheduleapp.identityservice.api.models.AuthorizeRequest;
import com.studentscheduleapp.identityservice.api.models.VerifyRequest;
import com.studentscheduleapp.identityservice.domain.models.Authorize;
import com.studentscheduleapp.identityservice.domain.models.Role;
import com.studentscheduleapp.identityservice.domain.models.User;
import com.studentscheduleapp.identityservice.jwt.models.JwtLoginRequest;
import com.studentscheduleapp.identityservice.jwt.models.JwtRegisterRequest;
import com.studentscheduleapp.identityservice.jwt.models.JwtResponse;
import com.studentscheduleapp.identityservice.jwt.models.RefreshJwtRequest;
import com.studentscheduleapp.identityservice.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/")
@RequiredArgsConstructor
public class IdentityController {

    @Autowired
    private VerifyService verifyService;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private AuthorizeService authorizeService;
    @Autowired
    private AppAuthorizeService appAuthorizeService;
    @Autowired
    private UserService userService;
    private final Map<String, User> verifyUserCache = new HashMap<>();

    @PostMapping("login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtLoginRequest authRequest) throws AuthException {
        if(authRequest.getEmail() == null || authRequest.getEmail().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if(authRequest.getPassword() == null || authRequest.getPassword().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        try {
            final JwtResponse token = identityService.login(authRequest);
            return ResponseEntity.ok(token);
        } catch (AuthException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        if(request.getRefreshToken() == null || request.getRefreshToken().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        try {
            final JwtResponse token = identityService.refresh(request.getRefreshToken());
            return ResponseEntity.ok(token);
        } catch (AuthException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("register")
    public ResponseEntity<Void> register(@RequestBody JwtRegisterRequest authRequest) throws AuthException {
        if(authRequest.getEmail() == null || authRequest.getEmail().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if(authRequest.getPassword() == null || authRequest.getPassword().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if(authRequest.getFirstName() == null || authRequest.getFirstName().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if(authRequest.getLastName() == null || authRequest.getLastName().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        User usr = null;
        try {
            usr = userService.getByEmail(authRequest.getEmail());
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        if(usr == null){
            ArrayList<Role> roles = new ArrayList<>();
            roles.add(Role.USER);
            User u = new User(0L, authRequest.getEmail(), authRequest.getPassword(), authRequest.getFirstName(), authRequest.getLastName(), false, null, roles);
            verifyUserCache.put(u.getEmail(), u);
            try {
                verifyService.sendCode(u.getEmail());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

    }

    @PostMapping("verify")
    public ResponseEntity<JwtResponse> verify(@RequestBody VerifyRequest verifyRequest){
        if (verifyUserCache.get(verifyRequest.getEmail()) != null){
            if(verifyService.verify(verifyRequest)){
                User u = verifyUserCache.get(verifyRequest.getEmail());
                try {
                    u = userService.create(u);
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
                try {
                    final JwtResponse token = identityService.login(new JwtLoginRequest(u.getEmail(), u.getPassword()));
                    return ResponseEntity.ok(token);
                } catch (AuthException e){
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }

    @PostMapping("authorize")
    public ResponseEntity<Void> authorize(@RequestBody AuthorizeRequest authorizeRequest){
        for(Authorize a : authorizeRequest.getAuthorizes()){
            if(!authorizeService.authorize(authorizeRequest.getUserToken(), a))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok().build();
    }
    @PostMapping("app/authorize")
    public ResponseEntity<Void> authorizeApp(@RequestBody AppAuthorizeRequest appAuthorizeRequest){
        if(appAuthorizeService.authorize(appAuthorizeRequest.getAppToken()))
            return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}