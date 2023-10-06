package com.studentscheduleapp.identityservice.repos;

import com.studentscheduleapp.identityservice.models.ServiceToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceTokenRepository extends JpaRepository<ServiceToken, Long> {
    boolean existsByServiceToken(String serviceToken);
}
