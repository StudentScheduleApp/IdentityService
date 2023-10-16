package com.studentscheduleapp.identityservice.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleTemplate {
    private long id;
    private long groupId;
    private String name;
    private long timeStart;
    private long timeStop;
    private String comment;

}
