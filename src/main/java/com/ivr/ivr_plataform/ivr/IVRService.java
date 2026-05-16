package com.ivr.ivr_plataform.ivr;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ivr.ivr_plataform.kafka.KafkaProducerService;

@Service
public class IVRService {

    private final KafkaProducerService kafkaProducerService;

    public IVRService(
            KafkaProducerService kafkaProducerService) {

        this.kafkaProducerService = kafkaProducerService;
    }

    public String startCall() {

        String callId = UUID.randomUUID().toString();

        kafkaProducerService.sendEvent(
                "ivr-events",
                new CallEvent(
                        callId,
                        "CALL_STARTED",
                        "Incoming customer call"));

        return callId;
    }

    public void authenticateCustomer(
            String callId,
            String customerId) {

        kafkaProducerService.sendEvent(
                "ivr-events",
                new CallEvent(
                        callId,
                        "CUSTOMER_AUTHENTICATED",
                        customerId));
    }

    public void transferToAgent(
            String callId,
            String department) {

        kafkaProducerService.sendEvent(
                "ivr-events",
                new CallEvent(
                        callId,
                        "TRANSFER_REQUESTED",
                        department));
    }

    public void endCall(String callId) {

        kafkaProducerService.sendEvent(
                "ivr-events",
                new CallEvent(
                        callId,
                        "CALL_ENDED",
                        "Call finished"));
    }
}
