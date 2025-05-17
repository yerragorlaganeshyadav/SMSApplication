package com.smsapplication.Repository;

import com.smsapplication.Entity.OTPEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OTPRepository extends JpaRepository<OTPEntity, Long> {
    Optional<OTPEntity> findByMobileNumber(String mobileNumber);

    List<OTPEntity> findAllByOtpCreatedAt(LocalDateTime localDateTime);
}
