package com.sportygroup.jackpot.controller;

import com.sportygroup.jackpot.service.BetService;
import com.sportygroup.jackpot.service.JackpotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Health check controller for monitoring service status.
 * Provides endpoints for checking service health and statistics.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/health")
@RequiredArgsConstructor
public class HealthController {
    
    private final BetService betService;
    private final JackpotService jackpotService;
    
    /**
     * Health check endpoint
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "jackpot-service");
        health.put("timestamp", java.time.LocalDateTime.now());
        
        return ResponseEntity.ok(health);
    }
    
    /**
     * Service statistics endpoint
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> stats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBets", betService.getAllBets().size());
        stats.put("totalJackpots", jackpotService.getAllJackpots().size());
        stats.put("timestamp", java.time.LocalDateTime.now());
        
        return ResponseEntity.ok(stats);
    }
}
