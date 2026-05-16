package com.ivr.ivr_plataform.ivr;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ivr")
public class IVRController {

    private final IVRService ivrService;

    public IVRController(
            IVRService ivrService) {

        this.ivrService = ivrService;
    }

    @PostMapping("/start")
    public String startCall() {

        return ivrService.startCall();
    }

    @PostMapping("/auth")
    public String authenticate(
            @RequestParam String callId,
            @RequestParam String customerId) {

        ivrService.authenticateCustomer(
                callId,
                customerId);

        return "CUSTOMER AUTHENTICATED";
    }

    @PostMapping("/transfer")
    public String transfer(
            @RequestParam String callId,
            @RequestParam String department) {

        ivrService.transferToAgent(
                callId,
                department);

        return "TRANSFER REQUESTED";
    }

    @PostMapping("/end")
    public String endCall(
            @RequestParam String callId) {

        ivrService.endCall(callId);

        return "CALL ENDED";
    }
}
