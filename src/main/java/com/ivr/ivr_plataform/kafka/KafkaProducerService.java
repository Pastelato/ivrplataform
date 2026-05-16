package com.ivr.ivr_plataform.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerService(
            KafkaTemplate<String, Object> kafkaTemplate) {

        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(
            String topic,
            Object event) {

        kafkaTemplate.send(topic, event);

        System.out.println(
                "EVENT SENT: ");
    }
}
