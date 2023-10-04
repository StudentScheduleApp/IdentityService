package com.studentscheduleapp.identityservice.repos;

import com.studentscheduleapp.identityservice.domain.models.AppToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppTokenRepo extends JpaRepository<AppToken, Long> {
    boolean existsByAppToken(String appToken);
    AppToken findByAppToken(String appToken);
}
