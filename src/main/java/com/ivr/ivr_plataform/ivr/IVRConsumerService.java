package com.ivr.ivr_plataform.ivr;

import java.time.LocalDateTime;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class IVRConsumerService {

    private final CallLogRepository repository;

    public IVRConsumerService(
            CallLogRepository repository) {

        this.repository = repository;
    }

    @KafkaListener(topics = "ivr-events", groupId = "ivr-group")
    public void consume(CallEvent event) {

        System.out.println("IVR EVENT RECEIVED");

        System.out.println(event.getCallId());
        System.out.println(event.getEventType());
        System.out.println(event.getPayload());

        CallLog log = new CallLog();

        log.setCallId(event.getCallId());
        log.setEventType(event.getEventType());
        log.setPayload(event.getPayload());
        log.setCreatedAt(LocalDateTime.now());

        repository.save(log);
    }
}
