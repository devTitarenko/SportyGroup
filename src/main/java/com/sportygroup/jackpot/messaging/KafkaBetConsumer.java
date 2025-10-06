package com.sportygroup.jackpot.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportygroup.jackpot.service.BetService;
import com.sportygroup.jackpot.service.JackpotContributionService;
import com.sportygroup.jackpot.service.JackpotRewardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Kafka consumer for processing bet events.
 * Listens to the jackpot-bets topic and processes bet contributions and rewards.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaBetConsumer {
    
    private final ObjectMapper objectMapper;
    private final BetService betService;
    private final JackpotContributionService contributionService;
    private final JackpotRewardService rewardService;
    
    /**
     * Consumes bet events from Kafka and processes them
     */
    @KafkaListener(topics = "${jackpot.topics.bets}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleBetEvent(String message) {
        try {
            log.info("Received bet event: {}", message);
            
            BetEvent betEvent = objectMapper.readValue(message, BetEvent.class);
            
            // Create bet entity
            var bet = betService.createBet(
                    betEvent.getUserId(),
                    betEvent.getJackpotId(),
                    betEvent.getBetAmount()
            );
            
            // Process contribution
            contributionService.processContribution(bet);
            
            // Evaluate reward
            rewardService.evaluateReward(bet);
            
            log.info("Successfully processed bet event: {}", betEvent.getBetId());
            
        } catch (Exception e) {
            log.error("Failed to process bet event: {}", message, e);
            // In a real system, you might want to implement dead letter queue or retry logic
        }
    }
}
