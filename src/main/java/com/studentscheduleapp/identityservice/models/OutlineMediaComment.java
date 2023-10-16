package com.studentscheduleapp.identityservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutlineMediaComment {
    private long id;
    private String text;
    private long userId;
    private long timestamp;
    private long mediaId;
    private long questionCommentId;
}
