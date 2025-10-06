package com.sportygroup.jackpot.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * Mock Kafka producer for development and testing.
 * Logs the bet event instead of sending to Kafka when kafka.enabled=false.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "false", matchIfMissing = true)
public class MockKafkaBetProducer {
    
    private final ObjectMapper objectMapper;
    
    /**
     * Mock implementation that logs the bet event instead of publishing to Kafka
     */
    public void publishBetEvent(BetEvent betEvent) {
        try {
            String message = objectMapper.writeValueAsString(betEvent);
            log.info("MOCK KAFKA: Would publish bet event to jackpot-bets topic: {}", message);
            log.info("MOCK KAFKA: Bet ID: {}, User ID: {}, Jackpot ID: {}, Amount: {}", 
                    betEvent.getBetId(), betEvent.getUserId(), 
                    betEvent.getJackpotId(), betEvent.getBetAmount());
            
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize bet event for mock: {}", betEvent, e);
        }
    }
}
