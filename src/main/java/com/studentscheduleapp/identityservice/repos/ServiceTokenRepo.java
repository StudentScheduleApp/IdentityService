package com.studentscheduleapp.identityservice.repos;

import com.studentscheduleapp.identityservice.domain.models.ServiceToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceTokenRepo extends JpaRepository<ServiceToken, Long> {
    boolean existsByServiceToken(String serviceToken);
}
