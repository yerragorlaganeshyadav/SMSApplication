package com.smsapplication.Controller;
import com.smsapplication.RequestDTO.OtpVerificationDTO;
import com.smsapplication.RequestDTO.UserRequestDTO;
import com.smsapplication.Service.OtpService;
import com.smsapplication.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/api/V1")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    OtpService  otpService;


    @PostMapping ("/login")
    public void login (@RequestBody UserRequestDTO userRequestDTO) throws Throwable {
        log.info("Requested user details {}", userRequestDTO);
        userService.login(userRequestDTO.getMobileNumber());
    }

    @PostMapping("/verify-otp")
    public void  verifyOtp(@RequestBody OtpVerificationDTO otpVerificationDTO) throws Throwable{
         otpService.validateOTP(otpVerificationDTO);
    }
}
