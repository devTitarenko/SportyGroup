package com.sportygroup.jackpot.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportygroup.jackpot.config.JackpotProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Kafka producer for publishing bet events.
 * Handles the publishing of bet events to the jackpot-bets topic.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true")
public class KafkaBetProducer {
    
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final JackpotProperties jackpotProperties;
    
    /**
     * Publishes a bet event to Kafka
     */
    public void publishBetEvent(BetEvent betEvent) {
        try {
            String topic = jackpotProperties.getTopics().getBets();
            String message = objectMapper.writeValueAsString(betEvent);
            
            log.info("Publishing bet event to topic {}: {}", topic, message);
            kafkaTemplate.send(topic, betEvent.getBetId(), message);
            
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize bet event: {}", betEvent, e);
            throw new RuntimeException("Failed to publish bet event", e);
        }
    }
}
