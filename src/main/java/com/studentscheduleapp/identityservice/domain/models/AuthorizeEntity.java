package com.studentscheduleapp.identityservice.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizeEntity {
    private List<Long> ids;
    private String entity;
    private List<String> params;
}
