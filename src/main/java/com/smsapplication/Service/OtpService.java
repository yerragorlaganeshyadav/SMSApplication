package com.smsapplication.Service;

import com.smsapplication.Exceptions.OTPExceptions;
import com.smsapplication.RequestDTO.OtpVerificationDTO;
import com.smsapplication.ResponseDTO.OtpVerificationResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();

    public String generateAndStoreOTP(String mobileNumber) {
        String otp = generateOTP();
        otpStorage.put(mobileNumber, otp);
        return otp;
    }

    public boolean validateOTP(String mobileNumber, String otp) {
        String storedOtp = otpStorage.get(mobileNumber);
        return storedOtp != null && storedOtp.equals(otp);
    }

    public String generateOTP(){
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000);
        return String.valueOf(otp);
    }

    public void clearOTP(String mobileNumber) {
        otpStorage.remove(mobileNumber);
    }
    public ResponseEntity<OtpVerificationResponseDTO> validateOTP(OtpVerificationDTO otpVerificationDTO) throws Throwable {
        OtpVerificationResponseDTO otpVerificationResponseDTO = new OtpVerificationResponseDTO();
        if(validateOTP(otpVerificationDTO.getMobileNumber(), otpVerificationDTO.getOtp())){
            otpVerificationResponseDTO.setOTPVerified(true);
            otpVerificationResponseDTO.setMessage("OTP verified successfully");
            clearOTP(otpVerificationDTO.getMobileNumber());
            return ResponseEntity.status(HttpStatus.OK).body(otpVerificationResponseDTO);
        }
        else{
            throw  new OTPExceptions(HttpStatus.BAD_REQUEST, "Unable to verify OTP please try again");
        }
    }
}

