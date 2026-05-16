package com.ivr.ivr_plataform.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "ai-events", groupId = "ivr-group")
    public void consume(AIEvent event) {

        System.out.println("EVENT RECEIVED");

        System.out.println(event.getType());

        System.out.println(event.getPayload());
    }
}
