package com.studentscheduleapp.identityservice.api.models;

import com.studentscheduleapp.identityservice.domain.models.Authorize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppAuthorizeRequest {

    private String appToken;

}
