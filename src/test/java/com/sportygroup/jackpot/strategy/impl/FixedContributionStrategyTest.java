package com.sportygroup.jackpot.strategy.impl;

import com.sportygroup.jackpot.config.JackpotProperties;
import com.sportygroup.jackpot.domain.Jackpot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Unit tests for FixedContributionStrategy.
 * Tests the fixed percentage contribution calculation logic.
 */
@ExtendWith(MockitoExtension.class)
class FixedContributionStrategyTest {
    
    @Mock
    private JackpotProperties jackpotProperties;
    
    @Mock
    private JackpotProperties.Strategies strategies;
    
    @Mock
    private JackpotProperties.Strategies.FixedContribution fixedContribution;
    
    private FixedContributionStrategy strategy;
    
    @BeforeEach
    void setUp() {
        strategy = new FixedContributionStrategy(jackpotProperties);
    }
    
    @Test
    void calculateContribution_ShouldCalculateFixedPercentage() {
        // Given
        BigDecimal betAmount = BigDecimal.valueOf(100.0);
        BigDecimal percentage = BigDecimal.valueOf(0.05); // 5%
        BigDecimal expectedContribution = BigDecimal.valueOf(5.0);
        
        Jackpot jackpot = Jackpot.builder()
                .jackpotId("jackpot-1")
                .currentAmount(BigDecimal.valueOf(1000.0))
                .build();
        
        when(jackpotProperties.getStrategies()).thenReturn(strategies);
        when(strategies.getFixedContribution()).thenReturn(fixedContribution);
        when(fixedContribution.getPercentage()).thenReturn(percentage);
        
        // When
        BigDecimal result = strategy.calculateContribution(betAmount, jackpot);
        
        // Then
        assertThat(result).isEqualByComparingTo(expectedContribution);
    }
    
    @Test
    void calculateContribution_WithDifferentBetAmount_ShouldCalculateCorrectly() {
        // Given
        BigDecimal betAmount = BigDecimal.valueOf(200.0);
        BigDecimal percentage = BigDecimal.valueOf(0.10); // 10%
        BigDecimal expectedContribution = BigDecimal.valueOf(20.0);
        
        Jackpot jackpot = Jackpot.builder()
                .jackpotId("jackpot-1")
                .currentAmount(BigDecimal.valueOf(1000.0))
                .build();
        
        when(jackpotProperties.getStrategies()).thenReturn(strategies);
        when(strategies.getFixedContribution()).thenReturn(fixedContribution);
        when(fixedContribution.getPercentage()).thenReturn(percentage);
        
        // When
        BigDecimal result = strategy.calculateContribution(betAmount, jackpot);
        
        // Then
        assertThat(result).isEqualByComparingTo(expectedContribution);
    }
    
    @Test
    void calculateContribution_WithZeroBetAmount_ShouldReturnZero() {
        // Given
        BigDecimal betAmount = BigDecimal.ZERO;
        BigDecimal percentage = BigDecimal.valueOf(0.05);
        
        Jackpot jackpot = Jackpot.builder()
                .jackpotId("jackpot-1")
                .currentAmount(BigDecimal.valueOf(1000.0))
                .build();
        
        when(jackpotProperties.getStrategies()).thenReturn(strategies);
        when(strategies.getFixedContribution()).thenReturn(fixedContribution);
        when(fixedContribution.getPercentage()).thenReturn(percentage);
        
        // When
        BigDecimal result = strategy.calculateContribution(betAmount, jackpot);
        
        // Then
        assertThat(result).isEqualByComparingTo(BigDecimal.ZERO);
    }
    
    @Test
    void getStrategyType_ShouldReturnFixed() {
        // When
        String result = strategy.getStrategyType();
        
        // Then
        assertThat(result).isEqualTo("FIXED");
    }
}
