package com.datpd.config;

import com.datpd.utils.KafkaTopicKeyEnum;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic friendSuggestions() {
        return TopicBuilder.name(KafkaTopicKeyEnum.FRIENDS_SUGGESTIONS.getKey())
                .partitions(3)
                .replicas(1)
                .build();
    }
}
