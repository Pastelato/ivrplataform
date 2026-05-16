package com.ivr.ivr_plataform.ivr;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ivr")
public class IVRHistoryController {

    private final CallLogRepository repository;

    public IVRHistoryController(
            CallLogRepository repository) {

        this.repository = repository;
    }

    @GetMapping("/calls")
    public List<CallLog> getAll() {

        return repository.findAll();
    }

    @GetMapping("/calls/{callId}")
    public List<CallLog> getByCallId(
            @PathVariable String callId) {

        return repository.findByCallId(callId);
    }
}
