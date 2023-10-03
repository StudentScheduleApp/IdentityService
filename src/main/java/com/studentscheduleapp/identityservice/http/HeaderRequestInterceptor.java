package com.studentscheduleapp.identityservice.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class HeaderRequestInterceptor implements ClientHttpRequestInterceptor {


    @Value("${app.token}")
    private String appTokenValue;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String headerName = "App-Token";
        request.getHeaders().set(headerName, appTokenValue);
        return execution.execute(request, body);
    }
}