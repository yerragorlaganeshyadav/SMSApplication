package com.smsapplication.Service;

import com.smsapplication.Entity.OTPEntity;
import com.smsapplication.Exceptions.OTPExceptions;
import com.smsapplication.Mapper.OTPMapper;
import com.smsapplication.Repository.OTPRepository;
import com.smsapplication.RequestDTO.OtpVerificationDTO;
import com.smsapplication.ResponseDTO.OtpVerificationResponseDTO;
import com.smsapplication.Utils.ApiResponse;
import com.smsapplication.Utils.EncryptionUtil;
import org.apache.kafka.common.requests.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private final OTPRepository otpRepository;
    private final EncryptionUtil encryptionUtil;
    private final OTPMapper otpMapper;
    private final KafkaTemplate<String, OtpVerificationResponseDTO> otpVerificationResponseDTOKafkaTemplate;

    public OtpService(OTPRepository otpRepository, EncryptionUtil encryptionUtil, OTPMapper otpMapper, KafkaTemplate<String, OtpVerificationResponseDTO> otpVerificationResponseDTOKafkaTemplate) {
        this.otpRepository = otpRepository;
        this.encryptionUtil = encryptionUtil;
        this.otpMapper = otpMapper;
        this.otpVerificationResponseDTOKafkaTemplate = otpVerificationResponseDTOKafkaTemplate;
    }


    public String generateAndStoreOTP(String mobileNumber) throws Throwable {
        String otp = generateOTP();
        OtpVerificationDTO otpVerificationDTO = new OtpVerificationDTO();
        String encryptedOTP = encryptionUtil.encrypt(otp);
        Optional<OTPEntity> otpEntity = otpRepository.findByMobileNumber(encryptionUtil.encrypt(mobileNumber));
        if (otpEntity.isEmpty()) {
            otpVerificationDTO.setMobileNumber(encryptionUtil.encrypt(mobileNumber));
            otpVerificationDTO.setOtp(encryptedOTP);
            otpVerificationDTO.setOtpCreatedAt(LocalDateTime.now());
            OTPEntity entity = otpMapper.convertToEntity(otpVerificationDTO);
            otpRepository.save(entity);
        } else {
            OTPEntity entity = otpEntity.get();
            otpVerificationDTO.setOtpCreatedAt(LocalDateTime.now());
            entity.setOtp(encryptedOTP);
            otpRepository.save(entity);
        }
        return otp;
    }

    public String generateOTP() {
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000);
        return String.valueOf(otp);
    }

    /*    public void validateOTP(OtpVerificationDTO otpVerificationDTO) throws Throwable{
            Optional<OTPEntity> otpEntity = otpRepository.findByMobileNumber(encryptionUtil.encrypt(otpVerificationDTO.getMobileNumber()));
            if(otpEntity.isEmpty()){
                throw new OTPExceptions(HttpStatus.BAD_REQUEST, "OTP has been expired, please try again!");
            }
            OTPEntity entity = otpEntity.get();
             if (entity.getOtpCreatedAt().plusMinutes(5).isBefore(LocalDateTime.now())) {
                otpRepository.delete(entity);
                throw new OTPExceptions(HttpStatus.BAD_REQUEST, "OTP has expired, please try again!");
            }
             if (!encryptionUtil.decrypt(entity.getOtp()).equals(otpVerificationDTO.getOtp())){
                throw new OTPExceptions(HttpStatus.BAD_REQUEST, "Please enter valid OTP");
            }
            else {
                OtpVerificationResponseDTO otpVerificationResponseDTO = new OtpVerificationResponseDTO();
                otpVerificationResponseDTO.setOTPVerified(true);
                otpVerificationResponseDTO.setMobileNumber(otpVerificationDTO.getMobileNumber());
                otpVerificationResponseDTO.setMessage("OTP verified successfully");
                 otpVerificationResponseDTOKafkaTemplate.send("otp_validation_result", otpVerificationResponseDTO);
                otpRepository.delete(entity);
            }
        }*/
    public void validateOTP(OtpVerificationDTO otpVerificationDTO) {
        try {
            Optional<OTPEntity> otpEntity = otpRepository.findByMobileNumber(encryptionUtil.encrypt(otpVerificationDTO.getMobileNumber()));
            if (otpEntity.isEmpty()) {
                sendFailureResponse(otpVerificationDTO.getMobileNumber(), "OTP has been expired, please try again!");
                return;
            }
            OTPEntity entity = otpEntity.get();
            if (entity.getOtpCreatedAt().plusMinutes(5).isBefore(LocalDateTime.now())) {
                otpRepository.delete(entity);
                sendFailureResponse(otpVerificationDTO.getMobileNumber(), "OTP has expired, please try again!");
                return;
            }
            if (!encryptionUtil.decrypt(entity.getOtp()).equals(otpVerificationDTO.getOtp())) {
                sendFailureResponse(otpVerificationDTO.getMobileNumber(), "Please enter valid OTP");
                return;
            } else {
                OtpVerificationResponseDTO otpVerificationResponseDTO = new OtpVerificationResponseDTO();
                otpVerificationResponseDTO.setOTPVerified(true);
                otpVerificationResponseDTO.setMobileNumber(otpVerificationDTO.getMobileNumber());
                otpVerificationResponseDTO.setMessage("OTP verified successfully");
                otpVerificationResponseDTOKafkaTemplate.send("otp_validation_result", otpVerificationResponseDTO);
                otpRepository.delete(entity);
            }
        } catch (Exception e) {
            // Handle other unexpected exceptions
            sendFailureResponse(otpVerificationDTO.getMobileNumber(), "An unexpected error occurred during OTP validation.");
            // Optionally log the error: logger.error("Unexpected error:", e);
        }
    }

    private void sendFailureResponse(String mobileNumber, String message) {
        OtpVerificationResponseDTO otpVerificationResponseDTO = new OtpVerificationResponseDTO();
        otpVerificationResponseDTO.setOTPVerified(false);
        otpVerificationResponseDTO.setMobileNumber(mobileNumber);
        otpVerificationResponseDTO.setMessage(message);
        otpVerificationResponseDTOKafkaTemplate.send("otp_validation_result", otpVerificationResponseDTO);
    }

    @Scheduled(fixedRate = 300000)
    public void deleteOTPAfterexpiration() {
        List<OTPEntity> otpEntities = otpRepository.findAllByOtpCreatedAt(LocalDateTime.now().minusMinutes(5));
        otpRepository.deleteAll(otpEntities);
    }
}

