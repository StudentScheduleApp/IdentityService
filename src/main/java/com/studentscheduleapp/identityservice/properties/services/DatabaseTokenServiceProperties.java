package com.studentscheduleapp.identityservice.properties.services;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class DatabaseTokenServiceProperties {

    @Value("${databasetokenservice.uri}")
    private String uri;
    @Value("${databasetokenservice.path.refresh.save}")
    private String savePath;
    @Value("${databasetokenservice.path.refresh.delete}")
    private String deletePath;
    @Value("${databasetokenservice.path.refresh.getByEmail}")
    private String getPath;


}
