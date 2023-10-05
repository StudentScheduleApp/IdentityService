package com.studentscheduleapp.identityservice.api;

import com.studentscheduleapp.identityservice.api.models.AuthorizeServiceRequest;
import com.studentscheduleapp.identityservice.api.models.AuthorizeUserRequest;
import com.studentscheduleapp.identityservice.api.models.VerifyEmailRequest;
import com.studentscheduleapp.identityservice.domain.models.AuthorizeEntity;
import com.studentscheduleapp.identityservice.domain.models.Role;
import com.studentscheduleapp.identityservice.domain.models.User;
import com.studentscheduleapp.identityservice.security.models.JwtLoginRequest;
import com.studentscheduleapp.identityservice.security.models.JwtRegisterRequest;
import com.studentscheduleapp.identityservice.security.models.JwtResponse;
import com.studentscheduleapp.identityservice.security.models.RefreshJwtRequest;
import com.studentscheduleapp.identityservice.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.message.AuthException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/")
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

    @PostMapping("login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtLoginRequest authRequest) throws AuthException {
        if(authRequest.getEmail() == null || authRequest.getEmail().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if(authRequest.getPassword() == null || authRequest.getPassword().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        try {
            final JwtResponse token = userTokenService.login(authRequest);
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
            final JwtResponse token = userTokenService.refresh(request.getRefreshToken());
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
                verifyEmailService.sendCode(u.getEmail());
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
    public ResponseEntity<JwtResponse> verify(@RequestBody VerifyEmailRequest verifyEmailRequest){
        if (verifyUserCache.get(verifyEmailRequest.getEmail()) != null){
            if(verifyEmailService.verify(verifyEmailRequest)){
                User u = verifyUserCache.get(verifyEmailRequest.getEmail());
                try {
                    u = userService.create(u);
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
                try {
                    final JwtResponse token = userTokenService.login(new JwtLoginRequest(u.getEmail(), u.getPassword()));
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
    public ResponseEntity<Void> authorize(@RequestBody AuthorizeUserRequest authorizeUserRequest){
        for(AuthorizeEntity a : authorizeUserRequest.getAuthorizeEntities()){
            if(!authorizeUserService.authorize(authorizeUserRequest.getUserToken(), a))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok().build();
    }
    @PostMapping("service/authorize")
    public ResponseEntity<Void> authorizeApp(@RequestBody AuthorizeServiceRequest authorizeServiceRequest){
        if(authorizeServiceService.authorize(authorizeServiceRequest.getServiceToken()))
            return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}