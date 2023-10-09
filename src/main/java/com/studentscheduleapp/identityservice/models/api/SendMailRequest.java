package com.studentscheduleapp.identityservice.models.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMailRequest {

    private String email;
    private String title;
    private String body;

}
