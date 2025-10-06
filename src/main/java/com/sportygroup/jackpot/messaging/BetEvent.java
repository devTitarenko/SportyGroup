package com.sportygroup.jackpot.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Event representing a bet that needs to be processed.
 * This is the message format used for Kafka communication.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BetEvent {
    
    private String betId;
    private String userId;
    private String jackpotId;
    private BigDecimal betAmount;
    private LocalDateTime timestamp;
    private String eventType;
}
