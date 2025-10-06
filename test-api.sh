#!/bin/bash

# Jackpot Service API Test Script
# This script demonstrates the main API endpoints

BASE_URL="http://localhost:8080/api/v1"

echo "🎰 Jackpot Service API Test Script"
echo "=================================="
echo ""

# Check if service is running
echo "1. Checking service health..."
curl -s "$BASE_URL/health" | jq '.' || echo "Service not running or jq not installed"
echo ""

# Get service statistics
echo "2. Getting service statistics..."
curl -s "$BASE_URL/health/stats" | jq '.' || echo "Failed to get stats"
echo ""

# List available jackpots
echo "3. Available jackpots..."
curl -s "$BASE_URL/jackpots" | jq '.' || echo "Failed to get jackpots"
echo ""

# Place a bet
echo "4. Placing a bet on main jackpot..."
BET_RESPONSE=$(curl -s -X POST "$BASE_URL/bets" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user-123",
    "jackpotId": "main-jackpot",
    "betAmount": 100.00
  }')

echo "$BET_RESPONSE" | jq '.' || echo "$BET_RESPONSE"
echo ""

# Extract bet ID from response
BET_ID=$(echo "$BET_RESPONSE" | jq -r '.betId' 2>/dev/null)

if [ "$BET_ID" != "null" ] && [ "$BET_ID" != "" ]; then
    echo "5. Getting bet details for bet ID: $BET_ID"
    curl -s "$BASE_URL/bets/$BET_ID" | jq '.' || echo "Failed to get bet details"
    echo ""
    
    echo "6. Evaluating reward for bet ID: $BET_ID"
    REWARD_RESPONSE=$(curl -s -X POST "$BASE_URL/rewards/evaluate" \
      -H "Content-Type: application/json" \
      -d "{\"betId\": \"$BET_ID\"}")
    
    echo "$REWARD_RESPONSE" | jq '.' || echo "$REWARD_RESPONSE"
    echo ""
fi

# Place multiple bets to test different scenarios
echo "7. Placing additional bets to test different jackpots..."

# Bet on weekly jackpot
echo "   - Betting on weekly jackpot..."
curl -s -X POST "$BASE_URL/bets" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user-456",
    "jackpotId": "weekly-jackpot",
    "betAmount": 50.00
  }' | jq '.' || echo "Failed to place bet on weekly jackpot"
echo ""

# Bet on high roller jackpot
echo "   - Betting on high roller jackpot..."
curl -s -X POST "$BASE_URL/bets" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user-789",
    "jackpotId": "high-roller-jackpot",
    "betAmount": 500.00
  }' | jq '.' || echo "Failed to place bet on high roller jackpot"
echo ""

# Get all bets
echo "8. Getting all bets..."
curl -s "$BASE_URL/bets" | jq '.' || echo "Failed to get all bets"
echo ""

# Get all rewards
echo "9. Getting all rewards..."
curl -s "$BASE_URL/rewards" | jq '.' || echo "Failed to get all rewards"
echo ""

# Get updated jackpot information
echo "10. Updated jackpot information..."
curl -s "$BASE_URL/jackpots" | jq '.' || echo "Failed to get updated jackpots"
echo ""

echo "✅ API test completed!"
echo ""
echo "💡 Tips:"
echo "   - Check the logs for Kafka messages (mock mode)"
echo "   - Try different bet amounts to see contribution calculations"
echo "   - Place multiple bets to see jackpot pool growth"
echo "   - Evaluate rewards multiple times to see the probability in action"


