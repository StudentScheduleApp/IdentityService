package com.studentscheduleapp.identityservice.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Authorize {
    private List<Long> ids;
    private String entity;
    private List<String> params;
}
