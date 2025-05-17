package com.smsapplication.RequestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpVerificationDTO {
    private String mobileNumber;
    private String otp;
    private boolean isValid;
    private LocalDateTime otpCreatedAt;
}
