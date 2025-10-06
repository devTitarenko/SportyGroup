package com.sportygroup.jackpot.messaging;

import com.sportygroup.jackpot.domain.Bet;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between Bet domain objects and BetEvent messages.
 * Manual implementation for object mapping.
 */
@Component
public class BetEventMapper {
    
    /**
     * Converts a Bet domain object to a BetEvent
     */
    public BetEvent toBetEvent(Bet bet) {
        return BetEvent.builder()
                .betId(bet.getBetId())
                .userId(bet.getUserId())
                .jackpotId(bet.getJackpotId())
                .betAmount(bet.getBetAmount())
                .timestamp(bet.getCreatedAt())
                .eventType("BET_PLACED")
                .build();
    }
}
