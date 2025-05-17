package com.smsapplication.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpVerificationResponseDTO {
    private String mobileNumber;
    private String message;
    private boolean isOTPVerified;
}
