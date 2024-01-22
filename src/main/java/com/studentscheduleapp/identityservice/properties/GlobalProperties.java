package com.studentscheduleapp.identityservice.properties;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
public class GlobalProperties {
    @Value("${global.serviceToken}")
    private String serviceToken;
    @Value("${global.serviceTokenHeader}")
    private String serviceTokenHeader;

}
