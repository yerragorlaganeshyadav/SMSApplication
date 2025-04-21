package com.smsapplication.RequestDTO;

import com.smsapplication.Exceptions.MobileNumberExceptions;
import com.smsapplication.Service.OtpService;
import com.smsapplication.Utils.Constants;
import com.smsapplication.configuration.TwilioConfiguration;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@Slf4j
public class TwilioSMSSender {

    private final TwilioConfiguration twilioConfiguration;
    private final OtpService otpService;

    public TwilioSMSSender(TwilioConfiguration twilioConfiguration, OtpService otpService) {
        this.twilioConfiguration = twilioConfiguration;
        this.otpService = otpService;
    }

    public void sendOtpSMS(String mobileNumber) throws Throwable {
        log.info("Sending OTP to your registered mobile number {}", maskMobileNumber(mobileNumber));
        if (isValidPhoneNumber(mobileNumber)) {
            String otp = otpService.generateAndStoreOTP(mobileNumber);
            String message = String.format(Constants.MESSAGE, otp);

            PhoneNumber to = new PhoneNumber(mobileNumber);
            PhoneNumber from = new PhoneNumber(twilioConfiguration.getTrialNumber());

            Message.creator(to, from, message).create();
            log.info("Sent OTP SMS to {}", maskMobileNumber(mobileNumber));
        } else {
            throw new MobileNumberExceptions(HttpStatus.BAD_REQUEST ,"Phone number [" + mobileNumber + "] is invalid");
        }
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("^\\+\\d{10,15}$");
    }

    public String maskMobileNumber(String mobileNumber) throws MobileNumberExceptions {


          if(isValidPhoneNumber(mobileNumber)){
              char [] arr = mobileNumber.toCharArray();
              for(int i = 0; i < arr.length; i ++){
                  if ( i >2 && i < 9 ){
                      arr [i] = '*';
                  }
              }
              return String.valueOf(arr);
          }

        else   {
            throw new MobileNumberExceptions(HttpStatus.BAD_REQUEST ,"Phone number [" + mobileNumber + "] is invalid");
        }
    }
}

