package com.sportygroup.jackpot.service;

import com.sportygroup.jackpot.config.StrategyFactory;
import com.sportygroup.jackpot.domain.Bet;
import com.sportygroup.jackpot.domain.Jackpot;
import com.sportygroup.jackpot.domain.JackpotContribution;
import com.sportygroup.jackpot.repository.JackpotContributionRepository;
import com.sportygroup.jackpot.strategy.ContributionStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service for managing jackpot contribution operations.
 * Handles the calculation and recording of jackpot contributions from bets.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JackpotContributionService {
    
    private final JackpotContributionRepository contributionRepository;
    private final JackpotService jackpotService;
    private final StrategyFactory strategyFactory;
    
    /**
     * Processes a bet contribution to a jackpot
     */
    public JackpotContribution processContribution(Bet bet) {
        log.info("Processing contribution for bet: {} to jackpot: {}", 
                bet.getBetId(), bet.getJackpotId());
        
        // Get the jackpot
        Jackpot jackpot = jackpotService.getJackpot(bet.getJackpotId())
                .orElseThrow(() -> new IllegalArgumentException("Jackpot not found: " + bet.getJackpotId()));
        
        // Get the appropriate contribution strategy
        ContributionStrategy strategy = strategyFactory.getContributionStrategy(jackpot);
        
        // Calculate contribution amount
        BigDecimal contributionAmount = strategy.calculateContribution(bet.getBetAmount(), jackpot);
        
        // Add contribution to jackpot
        jackpot.addContribution(contributionAmount);
        jackpotService.updateJackpotAmount(jackpot, jackpot.getCurrentAmount());
        
        // Create contribution record
        JackpotContribution contribution = JackpotContribution.create(
                bet.getBetId(),
                bet.getUserId(),
                bet.getJackpotId(),
                bet.getBetAmount(),
                contributionAmount,
                jackpot.getCurrentAmount()
        );
        
        log.info("Contribution processed: {} added to jackpot {}, new total: {}", 
                contributionAmount, jackpot.getJackpotId(), jackpot.getCurrentAmount());
        
        return contributionRepository.save(contribution);
    }
    
    /**
     * Retrieves all contributions for a specific bet
     */
    public List<JackpotContribution> getContributionsByBetId(String betId) {
        return contributionRepository.findByBetId(betId);
    }
    
    /**
     * Retrieves all contributions for a specific user
     */
    public List<JackpotContribution> getContributionsByUserId(String userId) {
        return contributionRepository.findByUserId(userId);
    }
    
    /**
     * Retrieves all contributions for a specific jackpot
     */
    public List<JackpotContribution> getContributionsByJackpotId(String jackpotId) {
        return contributionRepository.findByJackpotId(jackpotId);
    }
    
    /**
     * Retrieves all contributions
     */
    public List<JackpotContribution> getAllContributions() {
        return contributionRepository.findAll();
    }
}
