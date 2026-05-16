package com.ivr.ivr_plataform.ai;

import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "AI APIs")
@RestController
@RequestMapping("/ai")
public class AIController {

    private final AIService service;

    public AIController(AIService service) {
        this.service = service;
    }

    @Operation(summary = "Classify customer intent")
    @PostMapping("/classify")
    public AIResponse classify(
            @RequestBody AIRequest request) {

        String result = service.classify(request.getText());

        return new AIResponse(result);
    }

    @PostMapping("/summarize")
    public AIResponse summarize(
            @RequestBody AIRequest request) {

        return new AIResponse(
                service.summarize(request.getText()));
    }

    @PostMapping("/sentiment")
    public AIResponse sentiment(
            @RequestBody AIRequest request) {

        return new AIResponse(
                service.sentiment(request.getText()));
    }
}
