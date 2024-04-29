package com.studentscheduleapp.identityservice.services;

import com.studentscheduleapp.identityservice.models.RefreshToken;
import com.studentscheduleapp.identityservice.models.User;
import com.studentscheduleapp.identityservice.models.api.JwtLoginRequest;
import com.studentscheduleapp.identityservice.models.api.JwtResponse;
import com.studentscheduleapp.identityservice.repos.JwtRefreshTokenRepository;
import com.studentscheduleapp.identityservice.security.JwtProvider;
import com.studentscheduleapp.identityservice.security.ServiceAuthentication;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;

@Service
@RequiredArgsConstructor
public class UserTokenService {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    @Autowired
    private JwtRefreshTokenRepository jwtRefreshTokenRepository;

    public JwtResponse login(@NonNull JwtLoginRequest authRequest) throws Exception {
        final User user = userService.getByEmail(authRequest.getEmail());
        if (user == null)
            throw new AuthException();
        if (user.getPassword().equals(authRequest.getPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            jwtRefreshTokenRepository.save(new RefreshToken(user.getEmail(), refreshToken));
            return new JwtResponse(user.getId(), accessToken, refreshToken, jwtProvider.getAccessClaims(accessToken).getExpiration().getTime(), jwtProvider.getAccessClaims(refreshToken).getExpiration().getTime());
        } else {
            throw new AuthException();
        }
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) throws Exception {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = jwtRefreshTokenRepository.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.getByEmail(login);
                if (user == null)
                    throw new AuthException();
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(user.getId(), accessToken, refreshToken, jwtProvider.getAccessClaims(accessToken).getExpiration().getTime(), jwtProvider.getAccessClaims(refreshToken).getExpiration().getTime());
            }
        }
        throw new AuthException();
    }

    public User getUserByToken(@NonNull String accessToken) throws Exception {
        if (jwtProvider.validateAccessToken(accessToken)) {
            final Claims claims = jwtProvider.getAccessClaims(accessToken);
            final String login = claims.getSubject();
            return userService.getByEmail(login);
        }
        return null;
    }

    public JwtResponse refresh(@NonNull String refreshToken) throws Exception {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = jwtRefreshTokenRepository.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.getByEmail(login);
                if (user == null)
                    throw new AuthException();
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                jwtRefreshTokenRepository.save(new RefreshToken(user.getEmail(), refreshToken));
                return new JwtResponse(user.getId(), accessToken, newRefreshToken, jwtProvider.getAccessClaims(accessToken).getExpiration().getTime(), jwtProvider.getAccessClaims(newRefreshToken).getExpiration().getTime());
            }
        }
        throw new AuthException();
    }

    public ServiceAuthentication getAuthInfo() {
        return (ServiceAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

}