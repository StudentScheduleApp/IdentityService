package com.studentscheduleapp.identityservice.security;


import com.studentscheduleapp.identityservice.domain.models.ServiceToken;
import com.studentscheduleapp.identityservice.domain.models.Role;
import com.studentscheduleapp.identityservice.services.ServiceIdentityService;
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
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class ServiceTokenFilter extends GenericFilterBean {

    private static final String AUTHORIZATION = "Service-Token";
    @Autowired
    private ServiceIdentityService serviceIdentityService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws IOException, ServletException {
        final String token = getTokenFromRequest((HttpServletRequest) request);
        if (token != null && serviceIdentityService.authorize(token)) {
            final ServiceToken claims = serviceIdentityService.getByToken(token);
            final ServiceAuthentication appInfoToken = new ServiceAuthentication();
            appInfoToken.setAuthenticated(true);
            appInfoToken.setServiceName(claims.getServiceName());
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