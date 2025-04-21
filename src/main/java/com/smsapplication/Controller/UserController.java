package com.smsapplication.Controller;
import com.smsapplication.Exceptions.MobileNumberExceptions;
import com.smsapplication.RequestDTO.OtpVerificationDTO;
import com.smsapplication.RequestDTO.UserRequestDTO;
import com.smsapplication.ResponseDTO.OtpVerificationResponseDTO;
import com.smsapplication.ResponseDTO.UserResponseDTO;
import com.smsapplication.Service.OtpService;
import com.smsapplication.Service.UserService;
import com.smsapplication.Utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/V1")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    OtpService  otpService;

    @PostMapping ("/login")
    public ResponseEntity<ApiResponse<UserResponseDTO>> login (@RequestBody UserRequestDTO userRequestDTO) throws Throwable {
       return userService.login(userRequestDTO.getMobileNumber());
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<OtpVerificationResponseDTO> verifyOtp(@RequestBody OtpVerificationDTO otpVerificationDTO) throws Throwable{
        return otpService.validateOTP(otpVerificationDTO);
    }
}
