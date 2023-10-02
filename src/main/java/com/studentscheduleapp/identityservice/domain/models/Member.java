package com.studentscheduleapp.identityservice.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    private Long id;
    private Long groupId;
    private Long userId;
    private List<MemberRole> roles;
}
