package com.sportygroup.jackpot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportygroup.jackpot.domain.Jackpot;
import com.sportygroup.jackpot.dto.BetRequest;
import com.sportygroup.jackpot.messaging.BetEventMapper;
import com.sportygroup.jackpot.messaging.KafkaBetProducer;
import com.sportygroup.jackpot.messaging.MockKafkaBetProducer;
import com.sportygroup.jackpot.service.BetService;
import com.sportygroup.jackpot.service.JackpotService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for BetController.
 * Tests the REST API endpoints for bet operations.
 */
@WebMvcTest(BetController.class)
class BetControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private BetService betService;
    
    @MockBean
    private JackpotService jackpotService;
    
    @MockBean
    private KafkaBetProducer kafkaBetProducer;
    
    @MockBean
    private MockKafkaBetProducer mockKafkaBetProducer;
    
    @MockBean
    private BetEventMapper betEventMapper;
    
    @Test
    void placeBet_WithValidRequest_ShouldReturnCreatedBet() throws Exception {
        // Given
        BetRequest betRequest = new BetRequest();
        betRequest.setUserId("user-1");
        betRequest.setJackpotId("jackpot-1");
        betRequest.setBetAmount(BigDecimal.valueOf(100.0));
        
        Jackpot jackpot = Jackpot.builder()
                .jackpotId("jackpot-1")
                .name("Test Jackpot")
                .build();
        
        when(jackpotService.jackpotExists("jackpot-1")).thenReturn(true);
        when(betService.createBet(anyString(), anyString(), any(BigDecimal.class)))
                .thenReturn(com.sportygroup.jackpot.domain.Bet.builder()
                        .betId("bet-1")
                        .userId("user-1")
                        .jackpotId("jackpot-1")
                        .betAmount(BigDecimal.valueOf(100.0))
                        .createdAt(LocalDateTime.now())
                        .build());
        
        // When & Then
        mockMvc.perform(post("/api/v1/bets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(betRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.betId").exists())
                .andExpect(jsonPath("$.userId").value("user-1"))
                .andExpect(jsonPath("$.jackpotId").value("jackpot-1"))
                .andExpect(jsonPath("$.betAmount").value(100.0))
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }
    
    @Test
    void placeBet_WithNonExistentJackpot_ShouldReturnBadRequest() throws Exception {
        // Given
        BetRequest betRequest = new BetRequest();
        betRequest.setUserId("user-1");
        betRequest.setJackpotId("non-existent");
        betRequest.setBetAmount(BigDecimal.valueOf(100.0));
        
        when(jackpotService.jackpotExists("non-existent")).thenReturn(false);
        
        // When & Then
        mockMvc.perform(post("/api/v1/bets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(betRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Jackpot not found: non-existent"));
    }
    
    @Test
    void placeBet_WithInvalidRequest_ShouldReturnBadRequest() throws Exception {
        // Given
        BetRequest betRequest = new BetRequest();
        betRequest.setUserId(""); // Invalid: empty user ID
        betRequest.setJackpotId("jackpot-1");
        betRequest.setBetAmount(BigDecimal.valueOf(-10.0)); // Invalid: negative amount
        
        // When & Then
        mockMvc.perform(post("/api/v1/bets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(betRequest)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void getBet_WithExistingBet_ShouldReturnBet() throws Exception {
        // Given
        String betId = "bet-1";
        com.sportygroup.jackpot.domain.Bet bet = com.sportygroup.jackpot.domain.Bet.builder()
                .betId(betId)
                .userId("user-1")
                .jackpotId("jackpot-1")
                .betAmount(BigDecimal.valueOf(100.0))
                .createdAt(LocalDateTime.now())
                .build();
        
        when(betService.getBet(betId)).thenReturn(Optional.of(bet));
        
        // When & Then
        mockMvc.perform(get("/api/v1/bets/{betId}", betId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.betId").value(betId))
                .andExpect(jsonPath("$.userId").value("user-1"))
                .andExpect(jsonPath("$.jackpotId").value("jackpot-1"))
                .andExpect(jsonPath("$.betAmount").value(100.0))
                .andExpect(jsonPath("$.status").value("FOUND"));
    }
    
    @Test
    void getBet_WithNonExistentBet_ShouldReturnNotFound() throws Exception {
        // Given
        String betId = "non-existent";
        when(betService.getBet(betId)).thenReturn(Optional.empty());
        
        // When & Then
        mockMvc.perform(get("/api/v1/bets/{betId}", betId))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void getBetsByUser_ShouldReturnUserBets() throws Exception {
        // Given
        String userId = "user-1";
        com.sportygroup.jackpot.domain.Bet bet1 = com.sportygroup.jackpot.domain.Bet.builder()
                .betId("bet-1")
                .userId(userId)
                .jackpotId("jackpot-1")
                .betAmount(BigDecimal.valueOf(100.0))
                .createdAt(LocalDateTime.now())
                .build();
        
        when(betService.getBetsByUserId(userId)).thenReturn(java.util.List.of(bet1));
        
        // When & Then
        mockMvc.perform(get("/api/v1/bets/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].betId").value("bet-1"))
                .andExpect(jsonPath("$[0].userId").value(userId));
    }
    
    @Test
    void getAllBets_ShouldReturnAllBets() throws Exception {
        // Given
        com.sportygroup.jackpot.domain.Bet bet1 = com.sportygroup.jackpot.domain.Bet.builder()
                .betId("bet-1")
                .userId("user-1")
                .jackpotId("jackpot-1")
                .betAmount(BigDecimal.valueOf(100.0))
                .createdAt(LocalDateTime.now())
                .build();
        
        when(betService.getAllBets()).thenReturn(java.util.List.of(bet1));
        
        // When & Then
        mockMvc.perform(get("/api/v1/bets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].betId").value("bet-1"));
    }
}
