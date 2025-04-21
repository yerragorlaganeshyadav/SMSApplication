package com.smsapplication.configuration;

import com.twilio.Twilio;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
@Configuration
public class TwilioInitializer {
    @Autowired
    TwilioConfiguration twilioConfiguration;
    private final Logger log = LoggerFactory.getLogger(TwilioInitializer.class);
    public TwilioInitializer(@NotNull TwilioConfiguration twilioConfiguration){
        Twilio.init(
                twilioConfiguration.getAccountSid(),
                twilioConfiguration.getAuthToken()
        );
    }
}
