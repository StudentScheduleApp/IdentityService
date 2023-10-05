package com.studentscheduleapp.identityservice.api.models;

import com.studentscheduleapp.identityservice.domain.models.AuthorizeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizeUserRequest {

    private String userToken;
    private List<AuthorizeEntity> authorizeEntities;

}