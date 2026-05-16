package com.ivr.ivr_plataform.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.ivr.ivr_plataform.kafka.AIEvent;
import com.ivr.ivr_plataform.kafka.KafkaProducerService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.Timer.Sample;

@Service
public class AIService {

        private final WebClient webClient;
        private final StringRedisTemplate redisTemplate;
        private final AIHistoryRepository historyRepository;
        private final KafkaProducerService kafkaProducerService;
        private final Counter classifyCounter;
        private final Timer classifyTimer;

        public AIService(
                        @Value("${anthropic.api-key}") String apiKey,
                        StringRedisTemplate redisTemplate,
                        AIHistoryRepository historyRepository,
                        KafkaProducerService kafkaProducerService,
                        MeterRegistry registry) {

                this.redisTemplate = redisTemplate;
                this.historyRepository = historyRepository;
                this.kafkaProducerService = kafkaProducerService;

                this.webClient = WebClient.builder()
                                .baseUrl("https://api.anthropic.com/v1")
                                .defaultHeader("x-api-key", apiKey)
                                .defaultHeader("anthropic-version", "2023-06-01")
                                .build();

                this.classifyCounter = Counter.builder("ai_classify_requests_total")
                                .description("Total AI classify requests")
                                .register(registry);
                this.classifyTimer = Timer.builder("ai_classify_duration")
                                .description("AI classify duration")
                                .register(registry);
        }

        public String classify(String text) {

                classifyCounter.increment();

                Sample sample = Timer.start();

                String result = askAI(
                                "classify",
                                """
                                                Classify the customer intent.

                                                Possible intents:
                                                - CANCEL_SERVICE
                                                - TECH_SUPPORT
                                                - BILLING
                                                - GENERAL_QUESTION

                                                Text:
                                                %s

                                                Return ONLY the intent name.
                                                """.formatted(text));

                sample.stop(classifyTimer);

                return result;
        }

        public String summarize(String text) {

                return askAI(
                                "summarize",
                                """
                                                Summarize this customer interaction:

                                                %s
                                                """.formatted(text));
        }

        public String sentiment(String text) {

                return askAI(
                                "sentiment",
                                """
                                                Analyze customer sentiment.

                                                Possible values:
                                                - POSITIVE
                                                - NEGATIVE
                                                - NEUTRAL

                                                Text:
                                                %s

                                                Return ONLY the sentiment.
                                                """.formatted(text));
        }

        private String askAI(
                        String type,
                        String prompt) {

                String cacheKey = "ai:" + type + ":" + prompt.hashCode();

                String cached = redisTemplate.opsForValue()
                                .get(cacheKey);

                if (cached != null) {

                        System.out.println("CACHE HIT");

                        return cached;
                }

                Map<String, Object> body = new HashMap<>();

                body.put("model", "claude-haiku-4-5-20251001");
                body.put("max_tokens", 100);

                List<Map<String, String>> messages = List.of(
                                Map.of(
                                                "role", "user",
                                                "content", prompt));

                body.put("messages", messages);

                Map response = webClient.post()
                                .uri("/messages")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(body)
                                .retrieve()
                                .bodyToMono(Map.class)
                                .block();

                List content = (List) response.get("content");

                Map first = (Map) content.get(0);

                String result = first.get("text").toString();

                kafkaProducerService.sendEvent(
                                "ai-events",
                                new AIEvent(
                                                type,
                                                result));
                redisTemplate.opsForValue()
                                .set(
                                                cacheKey,
                                                result,
                                                10,
                                                TimeUnit.MINUTES);

                AIHistory history = new AIHistory();

                history.setType(type);
                history.setInputText(prompt);
                history.setResult(result);
                history.setCreatedAt(LocalDateTime.now());

                historyRepository.save(history);

                return result;

        }

}
