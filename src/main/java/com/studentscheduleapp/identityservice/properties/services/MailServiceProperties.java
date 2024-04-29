package com.studentscheduleapp.identityservice.properties.services;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class MailServiceProperties {

    @Value("${mailservice.uri}")
    private String uri;
    @Value("${mailservice.path.send}")
    private String sendPath;


}
