package com.studentscheduleapp.identityservice.properties;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JwtProperties {

    //Secret
    @Value("${jwt.secret.access}")
    private String accessSecret;
    @Value("${jwt.secret.refresh}")
    private String refreshSecret;

}
