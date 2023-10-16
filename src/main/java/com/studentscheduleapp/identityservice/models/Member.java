package com.studentscheduleapp.identityservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    private long id;
    private long groupId;
    private long userId;
    private List<MemberRole> roles;
}
