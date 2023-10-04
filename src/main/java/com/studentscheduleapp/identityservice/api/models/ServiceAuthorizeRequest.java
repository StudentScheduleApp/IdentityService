package com.studentscheduleapp.identityservice.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceAuthorizeRequest {

    private String serviceToken;

}
