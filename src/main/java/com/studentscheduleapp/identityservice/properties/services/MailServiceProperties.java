package com.studentscheduleapp.identityservice.properties.services;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Getter
public class MailServiceProperties {

    @Value("${mailservice.uri}")
    private String uri;
    @Value("${mailservice.path.send}")
    private String sendPath;


}
