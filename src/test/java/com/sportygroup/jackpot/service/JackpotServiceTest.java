package com.sportygroup.jackpot.service;

import com.sportygroup.jackpot.domain.Jackpot;
import com.sportygroup.jackpot.repository.JackpotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for JackpotService.
 * Tests the business logic for jackpot management operations.
 */
@ExtendWith(MockitoExtension.class)
class JackpotServiceTest {
    
    @Mock
    private JackpotRepository jackpotRepository;
    
    private JackpotService jackpotService;
    
    @BeforeEach
    void setUp() {
        jackpotService = new JackpotService(jackpotRepository);
    }
    
    @Test
    void createJackpot_ShouldCreateAndSaveJackpot() {
        // Given
        String jackpotId = "jackpot-1";
        String name = "Test Jackpot";
        Jackpot.ContributionType contributionType = Jackpot.ContributionType.FIXED;
        Jackpot.RewardType rewardType = Jackpot.RewardType.FIXED;
        
        when(jackpotRepository.save(any(Jackpot.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // When
        Jackpot result = jackpotService.createJackpot(jackpotId, name, contributionType, rewardType);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getJackpotId()).isEqualTo(jackpotId);
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getContributionType()).isEqualTo(contributionType);
        assertThat(result.getRewardType()).isEqualTo(rewardType);
        assertThat(result.getCurrentAmount()).isEqualTo(BigDecimal.valueOf(1000.0));
        assertThat(result.getInitialAmount()).isEqualTo(BigDecimal.valueOf(1000.0));
        
        verify(jackpotRepository).save(any(Jackpot.class));
    }
    
    @Test
    void getJackpot_WhenJackpotExists_ShouldReturnJackpot() {
        // Given
        String jackpotId = "jackpot-1";
        Jackpot jackpot = Jackpot.builder()
                .jackpotId(jackpotId)
                .name("Test Jackpot")
                .currentAmount(BigDecimal.valueOf(1000.0))
                .build();
        
        when(jackpotRepository.findById(jackpotId)).thenReturn(Optional.of(jackpot));
        
        // When
        Optional<Jackpot> result = jackpotService.getJackpot(jackpotId);
        
        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getJackpotId()).isEqualTo(jackpotId);
        
        verify(jackpotRepository).findById(jackpotId);
    }
    
    @Test
    void getJackpot_WhenJackpotDoesNotExist_ShouldReturnEmpty() {
        // Given
        String jackpotId = "non-existent";
        when(jackpotRepository.findById(jackpotId)).thenReturn(Optional.empty());
        
        // When
        Optional<Jackpot> result = jackpotService.getJackpot(jackpotId);
        
        // Then
        assertThat(result).isEmpty();
        
        verify(jackpotRepository).findById(jackpotId);
    }
    
    @Test
    void getAllJackpots_ShouldReturnAllJackpots() {
        // Given
        Jackpot jackpot1 = Jackpot.builder().jackpotId("jackpot-1").build();
        Jackpot jackpot2 = Jackpot.builder().jackpotId("jackpot-2").build();
        List<Jackpot> jackpots = List.of(jackpot1, jackpot2);
        
        when(jackpotRepository.findAll()).thenReturn(jackpots);
        
        // When
        List<Jackpot> result = jackpotService.getAllJackpots();
        
        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(jackpot1, jackpot2);
        
        verify(jackpotRepository).findAll();
    }
    
    @Test
    void updateJackpotAmount_ShouldUpdateAndSaveJackpot() {
        // Given
        Jackpot jackpot = Jackpot.builder()
                .jackpotId("jackpot-1")
                .currentAmount(BigDecimal.valueOf(1000.0))
                .build();
        
        BigDecimal newAmount = BigDecimal.valueOf(1500.0);
        when(jackpotRepository.save(any(Jackpot.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // When
        Jackpot result = jackpotService.updateJackpotAmount(jackpot, newAmount);
        
        // Then
        assertThat(result.getCurrentAmount()).isEqualTo(newAmount);
        assertThat(result.getUpdatedAt()).isNotNull();
        
        verify(jackpotRepository).save(jackpot);
    }
    
    @Test
    void resetJackpot_ShouldResetToInitialAmount() {
        // Given
        Jackpot jackpot = Jackpot.builder()
                .jackpotId("jackpot-1")
                .currentAmount(BigDecimal.valueOf(2000.0))
                .initialAmount(BigDecimal.valueOf(1000.0))
                .build();
        
        when(jackpotRepository.save(any(Jackpot.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // When
        Jackpot result = jackpotService.resetJackpot(jackpot);
        
        // Then
        assertThat(result.getCurrentAmount()).isEqualTo(BigDecimal.valueOf(1000.0));
        assertThat(result.getUpdatedAt()).isNotNull();
        
        verify(jackpotRepository).save(jackpot);
    }
    
    @Test
    void jackpotExists_WhenJackpotExists_ShouldReturnTrue() {
        // Given
        String jackpotId = "jackpot-1";
        when(jackpotRepository.existsById(jackpotId)).thenReturn(true);
        
        // When
        boolean result = jackpotService.jackpotExists(jackpotId);
        
        // Then
        assertThat(result).isTrue();
        
        verify(jackpotRepository).existsById(jackpotId);
    }
    
    @Test
    void jackpotExists_WhenJackpotDoesNotExist_ShouldReturnFalse() {
        // Given
        String jackpotId = "non-existent";
        when(jackpotRepository.existsById(jackpotId)).thenReturn(false);
        
        // When
        boolean result = jackpotService.jackpotExists(jackpotId);
        
        // Then
        assertThat(result).isFalse();
        
        verify(jackpotRepository).existsById(jackpotId);
    }
}
