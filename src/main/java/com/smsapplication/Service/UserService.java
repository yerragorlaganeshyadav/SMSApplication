package com.smsapplication.Service;

import com.smsapplication.Entity.User;
import com.smsapplication.Exceptions.MobileNumberExceptions;
import com.smsapplication.Mapper.UserMapper;
import com.smsapplication.Repository.UserRepository;
import com.smsapplication.RequestDTO.TwilioSMSSender;
import com.smsapplication.RequestDTO.UserRequestDTO;
import com.smsapplication.ResponseDTO.UserResponseDTO;
import com.smsapplication.Utils.ApiResponse;
import com.smsapplication.Utils.EncryptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    EncryptionUtil encryptionUtil;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TwilioSMSSender twilioSMSSender;


    public ResponseEntity<ApiResponse<UserResponseDTO>> login(String mobileNumber) throws Throwable {
        log.info("Validating mobile number....");
        Optional<User> optionalUser = userRepository.findByMobileNumber(encryptionUtil.encrypt(mobileNumber));
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        if(optionalUser.isEmpty()) {

            userRequestDTO.setMobileNumber(mobileNumber);
            userRequestDTO.setProfileComplete(false);
            twilioSMSSender.sendOtpSMS(mobileNumber);

            User mapUser = userMapper.convertToEntity(userRequestDTO);
            userRepository.save(mapUser);
            userResponseDTO = userMapper.convertToDTO(mapUser);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "New user created, OTP sent to registered mobile number", userResponseDTO));
        } else {
            userResponseDTO = userMapper.convertToDTO(optionalUser.get());
            twilioSMSSender.sendOtpSMS(mobileNumber);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse<>(true, "Existing user, OTP sent to registered mobile number", userResponseDTO));
        }
    }

}
