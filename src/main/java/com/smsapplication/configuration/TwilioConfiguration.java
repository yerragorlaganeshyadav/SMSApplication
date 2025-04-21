package com.smsapplication.configuration;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties ("twilio")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TwilioConfiguration {

    private String accountSid;
    private String authToken;
    private String trialNumber;
}
