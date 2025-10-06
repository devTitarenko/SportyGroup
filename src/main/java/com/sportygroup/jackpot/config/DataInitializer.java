package com.sportygroup.jackpot.config;

import com.sportygroup.jackpot.domain.Jackpot;
import com.sportygroup.jackpot.service.JackpotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Data initializer for creating sample jackpots on application startup.
 * This helps demonstrate the system with pre-configured jackpots.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final JackpotService jackpotService;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing sample data...");
        
        // Create sample jackpots with different configurations
        createSampleJackpots();
        
        log.info("Sample data initialization completed");
    }
    
    private void createSampleJackpots() {
        // Main Jackpot - Fixed contribution, Variable reward
        if (!jackpotService.jackpotExists("main-jackpot")) {
            jackpotService.createJackpot(
                    "main-jackpot",
                    "Main Jackpot",
                    Jackpot.ContributionType.FIXED,
                    Jackpot.RewardType.VARIABLE
            );
            log.info("Created main jackpot with FIXED contribution and VARIABLE reward");
        }
        
        // Weekly Jackpot - Variable contribution, Fixed reward
        if (!jackpotService.jackpotExists("weekly-jackpot")) {
            jackpotService.createJackpot(
                    "weekly-jackpot",
                    "Weekly Jackpot",
                    Jackpot.ContributionType.VARIABLE,
                    Jackpot.RewardType.FIXED
            );
            log.info("Created weekly jackpot with VARIABLE contribution and FIXED reward");
        }
        
        // High Roller Jackpot - Variable contribution, Variable reward
        if (!jackpotService.jackpotExists("high-roller-jackpot")) {
            jackpotService.createJackpot(
                    "high-roller-jackpot",
                    "High Roller Jackpot",
                    Jackpot.ContributionType.VARIABLE,
                    Jackpot.RewardType.VARIABLE
            );
            log.info("Created high roller jackpot with VARIABLE contribution and VARIABLE reward");
        }
        
        // Classic Jackpot - Fixed contribution, Fixed reward
        if (!jackpotService.jackpotExists("classic-jackpot")) {
            jackpotService.createJackpot(
                    "classic-jackpot",
                    "Classic Jackpot",
                    Jackpot.ContributionType.FIXED,
                    Jackpot.RewardType.FIXED
            );
            log.info("Created classic jackpot with FIXED contribution and FIXED reward");
        }
    }
}
