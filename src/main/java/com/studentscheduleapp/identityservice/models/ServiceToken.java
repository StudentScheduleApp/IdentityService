package com.studentscheduleapp.identityservice.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceToken {
    private long id;
    private String serviceName;
    private String serviceToken;
}
