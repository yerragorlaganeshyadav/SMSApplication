package com.smsapplication.Service;

import com.smsapplication.RequestDTO.TwilioSMSSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class UserService {



    @Autowired
    TwilioSMSSender twilioSMSSender;


    public void login(String mobileNumber) throws Throwable {
        log.info("Initiating call to send otp to register mobile number....");
        twilioSMSSender.sendOtpSMS(mobileNumber);
    }

}
