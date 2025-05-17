package com.smsapplication.Service;

import com.smsapplication.RequestDTO.OtpVerificationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaOTPConsumerService {

    private final UserService userService;
    private final OtpService otpService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaOTPConsumerService(KafkaTemplate<String, String> kafkaTemplate, UserService userService, OtpService otpService) {
        this.kafkaTemplate = kafkaTemplate;
        this.userService = userService;
        this.otpService = otpService;

    }

    @KafkaListener(topics = "otp_events", groupId = "otp-consumer-group")
    public void login(String mobileNumber) {
        try {
            userService.login(mobileNumber);
        } catch (Throwable exception) {
            log.error("Failed to send OTP: {}", exception.getMessage());
            kafkaTemplate.send("otp_failed_events", mobileNumber);
        }
    }

    @KafkaListener(topics = "validate_otp", groupId = "validate-otp-group", containerFactory = "otpVerificationDTOFactory")
    public void validateOTP(OtpVerificationDTO otpVerificationDTO) throws Throwable{
        try{
            otpService.validateOTP(otpVerificationDTO);
        } catch (Exception e){
            log.error("Failed to validate OTP: {}", e.getMessage());
            kafkaTemplate.send("otp_verification_failed_events", otpVerificationDTO.getMobileNumber());
        }
    }

}
