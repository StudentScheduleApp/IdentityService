package com.studentscheduleapp.identityservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonTemplate {
    private long id;
    private long scheduleTemplateId;
    private long lessonId;
    private long time;
    private String comment;

}
