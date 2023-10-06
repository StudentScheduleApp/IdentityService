package com.studentscheduleapp.identityservice.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "service_tokens")
public class ServiceToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    @Column(name = "service_name", nullable = false)
    private String serviceName;
    @Column(name = "service_token", nullable = false)
    private String serviceToken;
}
