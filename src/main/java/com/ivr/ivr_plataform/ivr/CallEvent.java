package com.ivr.ivr_plataform.ivr;

import java.time.LocalDateTime;

public class CallEvent {

    private String callId;

    private String eventType;

    private String payload;

    private LocalDateTime timestamp;

    public CallEvent() {
    }

    public CallEvent(
            String callId,
            String eventType,
            String payload) {

        this.callId = callId;
        this.eventType = eventType;
        this.payload = payload;
        this.timestamp = LocalDateTime.now();
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
