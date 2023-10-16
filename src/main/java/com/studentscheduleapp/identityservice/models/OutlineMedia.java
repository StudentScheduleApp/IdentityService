package com.studentscheduleapp.identityservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutlineMedia {
    private long id;
    private long timestamp;
    private long outlineId;
    private String imageUrl;

}
