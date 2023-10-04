package com.studentscheduleapp.identityservice.jwt;


import com.studentscheduleapp.identityservice.domain.models.AppToken;
import com.studentscheduleapp.identityservice.domain.models.Role;
import com.studentscheduleapp.identityservice.domain.models.User;
import com.studentscheduleapp.identityservice.services.AppAuthorizeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppTokenFilter extends GenericFilterBean {

    private static final String AUTHORIZATION = "App-Token";
    @Autowired
    private AppAuthorizeService appAuthorizeService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws IOException, ServletException {
        final String token = getTokenFromRequest((HttpServletRequest) request);
        if (token != null && appAuthorizeService.authorize(token)) {
            final AppToken claims = appAuthorizeService.getByToken(token);
            final AppAuthentication appInfoToken = new AppAuthentication();
            appInfoToken.setAuthenticated(true);
            appInfoToken.setAppName(claims.getAppName());
            Set<Role> roles = new HashSet<>();
            SecurityContextHolder.getContext().setAuthentication(appInfoToken);
        }
        fc.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String token = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(token)) {
            return token;
        }
        return null;
    }

}