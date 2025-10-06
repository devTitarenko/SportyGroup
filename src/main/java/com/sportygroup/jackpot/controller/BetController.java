package com.sportygroup.jackpot.controller;

import com.sportygroup.jackpot.domain.Bet;
import com.sportygroup.jackpot.dto.BetRequest;
import com.sportygroup.jackpot.dto.BetResponse;
import com.sportygroup.jackpot.messaging.BetEvent;
import com.sportygroup.jackpot.messaging.BetEventMapper;
import com.sportygroup.jackpot.messaging.KafkaBetProducer;
import com.sportygroup.jackpot.messaging.MockKafkaBetProducer;
import com.sportygroup.jackpot.service.BetService;
import com.sportygroup.jackpot.service.JackpotService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for bet operations.
 * Provides endpoints for placing bets and retrieving bet information.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/bets")
public class BetController {
    
    private final BetService betService;
    private final JackpotService jackpotService;
    private final BetEventMapper betEventMapper;
    
    @Autowired(required = false)
    private KafkaBetProducer kafkaBetProducer;
    
    @Autowired(required = false)
    private MockKafkaBetProducer mockKafkaBetProducer;
    
    public BetController(BetService betService, JackpotService jackpotService, BetEventMapper betEventMapper) {
        this.betService = betService;
        this.jackpotService = jackpotService;
        this.betEventMapper = betEventMapper;
    }
    
    /**
     * Places a new bet and publishes it to Kafka
     */
    @PostMapping
    public ResponseEntity<BetResponse> placeBet(@Valid @RequestBody BetRequest betRequest) {
        log.info("Received bet request: {}", betRequest);
        
        // Validate jackpot exists
        if (!jackpotService.jackpotExists(betRequest.getJackpotId())) {
            BetResponse errorResponse = BetResponse.builder()
                    .status("ERROR")
                    .message("Jackpot not found: " + betRequest.getJackpotId())
                    .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        // Create bet
        Bet bet = betService.createBet(
                betRequest.getUserId(),
                betRequest.getJackpotId(),
                betRequest.getBetAmount()
        );
        
        // Create bet event
        BetEvent betEvent = betEventMapper.toBetEvent(bet);
        
        // Publish to Kafka (or mock)
        if (kafkaBetProducer != null) {
            try {
                kafkaBetProducer.publishBetEvent(betEvent);
                log.info("Bet event published to Kafka successfully");
            } catch (Exception e) {
                log.warn("Kafka producer failed, using mock: {}", e.getMessage());
                if (mockKafkaBetProducer != null) {
                    mockKafkaBetProducer.publishBetEvent(betEvent);
                }
            }
        } else if (mockKafkaBetProducer != null) {
            log.info("Using mock Kafka producer");
            mockKafkaBetProducer.publishBetEvent(betEvent);
        } else {
            log.warn("No Kafka producer available, bet event not published");
        }
        
        // Create response
        BetResponse response = BetResponse.builder()
                .betId(bet.getBetId())
                .userId(bet.getUserId())
                .jackpotId(bet.getJackpotId())
                .betAmount(bet.getBetAmount())
                .createdAt(bet.getCreatedAt())
                .status("SUCCESS")
                .message("Bet placed successfully")
                .build();
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Retrieves a bet by its ID
     */
    @GetMapping("/{betId}")
    public ResponseEntity<BetResponse> getBet(@PathVariable String betId) {
        Optional<Bet> bet = betService.getBet(betId);
        
        if (bet.isPresent()) {
            Bet betEntity = bet.get();
            BetResponse response = BetResponse.builder()
                    .betId(betEntity.getBetId())
                    .userId(betEntity.getUserId())
                    .jackpotId(betEntity.getJackpotId())
                    .betAmount(betEntity.getBetAmount())
                    .createdAt(betEntity.getCreatedAt())
                    .status("FOUND")
                    .build();
            
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Retrieves all bets for a specific user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BetResponse>> getBetsByUser(@PathVariable String userId) {
        List<Bet> bets = betService.getBetsByUserId(userId);
        
        List<BetResponse> responses = bets.stream()
                .map(bet -> BetResponse.builder()
                        .betId(bet.getBetId())
                        .userId(bet.getUserId())
                        .jackpotId(bet.getJackpotId())
                        .betAmount(bet.getBetAmount())
                        .createdAt(bet.getCreatedAt())
                        .status("FOUND")
                        .build())
                .toList();
        
        return ResponseEntity.ok(responses);
    }
    
    /**
     * Retrieves all bets
     */
    @GetMapping
    public ResponseEntity<List<BetResponse>> getAllBets() {
        List<Bet> bets = betService.getAllBets();
        
        List<BetResponse> responses = bets.stream()
                .map(bet -> BetResponse.builder()
                        .betId(bet.getBetId())
                        .userId(bet.getUserId())
                        .jackpotId(bet.getJackpotId())
                        .betAmount(bet.getBetAmount())
                        .createdAt(bet.getCreatedAt())
                        .status("FOUND")
                        .build())
                .toList();
        
        return ResponseEntity.ok(responses);
    }
}
