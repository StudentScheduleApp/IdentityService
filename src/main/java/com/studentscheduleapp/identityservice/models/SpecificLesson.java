package com.studentscheduleapp.identityservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecificLesson {
    private long id;
    private long groupId;
    private long lessonId;
    private long time;
    private Boolean canceled;
    private String comment;

}
