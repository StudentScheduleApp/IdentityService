package com.studentscheduleapp.identityservice.domain.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "app_tokens")
public class AppToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    @Column(name = "app_name", nullable = false)
    private String appName;
    @Column(name = "app_token", nullable = false)
    private String appToken;
}
