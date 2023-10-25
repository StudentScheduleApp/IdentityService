package com.studentscheduleapp.identityservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    private long id;
    private long chatId;
    private long avaUrl;
    private String name;
    private String driveEmail;

}
