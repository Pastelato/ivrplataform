package com.ivr.ivr_plataform.ivr;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CallLogRepository
        extends JpaRepository<CallLog, Long> {

    List<CallLog> findByCallId(String callId);
}
