package com.ivr.ivr_plataform.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic aiTopic() {

        return new NewTopic(
                "ai-events",
                1,
                (short) 1);
    }

    @Bean
    public NewTopic ivrTopic() {

        return TopicBuilder.name("ivr-events")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
