package com.smsapplication.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfiguration {


    /**
     * Here we will create a topic with name
     */
    @Bean
    public NewTopic kafkaConfig() {

        return TopicBuilder.name("otp_validation_result")
                .partitions(10)
                .replicas(1)
                .build();
    }
}

