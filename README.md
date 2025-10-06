# Jackpot Service

A comprehensive backend service for managing jackpot contributions and rewards, built with Spring Boot and modern Java technologies.

## Overview

The Jackpot Service handles the complete lifecycle of jackpot operations, from bet placement to reward evaluation. It features:

- **REST API endpoints** for bet submission and reward evaluation
- **Kafka integration** for asynchronous bet processing
- **Configurable contribution and reward strategies**
- **In-memory data storage** for development and testing
- **Comprehensive test coverage** with unit and integration tests

## Features

### Core Functionality
- ✅ **Bet Management**: Place bets and track them through the system
- ✅ **Jackpot Contributions**: Automatic contribution calculation based on configurable strategies
- ✅ **Reward Evaluation**: Determine jackpot winners using various reward strategies
- ✅ **Kafka Integration**: Asynchronous processing with both real and mock implementations
- ✅ **Strategy Pattern**: Pluggable contribution and reward calculation algorithms

### Contribution Strategies
- **Fixed Contribution**: Fixed percentage of bet amount (e.g., 5%)
- **Variable Contribution**: Percentage that decreases as jackpot pool increases

### Reward Strategies
- **Fixed Reward**: Fixed chance percentage for winning
- **Variable Reward**: Chance increases with jackpot pool size, with 100% chance at trigger limit

## Technology Stack

- **Java 21** - Latest LTS version with modern language features
- **Spring Boot 3.2.0** - Latest stable version with Spring Framework 6
- **Spring Kafka** - For message processing and event-driven architecture
- **Lombok** - For reducing boilerplate code
- **Jackson** - For JSON serialization/deserialization
- **JUnit 5** - For comprehensive testing
- **Testcontainers** - For integration testing with real Kafka
- **Maven** - For dependency management and build automation

## Quick Start

### Prerequisites
- Java 21 or higher
- Maven 3.8 or higher
- (Optional) Apache Kafka for production use

### Running the Application

#### Option 1: Development Mode (Without Kafka)
```bash
# Clone and build
git clone <repository-url>
cd jackpot-service
mvn clean compile

# Run the application (Kafka disabled by default)
mvn spring-boot:run

# Access the application
# - Application: http://localhost:8080
# - Health check: http://localhost:8080/api/v1/health
# - Service stats: http://localhost:8080/api/v1/health/stats
```

#### Option 2: Production Mode (With Kafka)

**Quick Start Script (Recommended)**
```bash
# Use the provided script for easy setup
./start-with-kafka.sh
```

**Manual Setup**
```bash
# 1. Start Kafka using Docker
docker-compose up -d

# 2. Create required topic
docker-compose exec kafka kafka-topics --bootstrap-server localhost:9092 \
  --create --topic jackpot-bets --partitions 1 --replication-factor 1 --if-not-exists

# 3. Enable Kafka in application.yml
# Set kafka.enabled: true

# 4. Run the application
mvn spring-boot:run

# 5. Monitor Kafka messages (optional)
docker-compose exec kafka kafka-console-consumer --bootstrap-server localhost:9092 \
  --topic jackpot-bets --from-beginning
```

### Testing the Complete Flow

```bash
# Place a bet (this will publish to Kafka if enabled)
curl -X POST http://localhost:8080/api/v1/bets \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user-123",
    "jackpotId": "main-jackpot",
    "betAmount": 100.00
  }'

# Evaluate reward
curl -X POST http://localhost:8080/api/v1/rewards/evaluate \
  -H "Content-Type: application/json" \
  -d '{"betId": "bet-123"}'

# Check jackpot status
curl http://localhost:8080/api/v1/jackpots/main-jackpot
```

## API Documentation

### Base URL
```
http://localhost:8080/api/v1
```

### Endpoints

#### Bet Management
- **POST /bets** - Place a new bet
- **GET /bets/{betId}** - Get bet by ID
- **GET /bets/user/{userId}** - Get all bets for a user
- **GET /bets** - Get all bets

#### Reward Evaluation
- **POST /rewards/evaluate** - Evaluate if a bet wins a jackpot

#### Jackpot Management
- **GET /jackpots/{jackpotId}** - Get jackpot information
- **GET /jackpots** - Get all jackpots
- **POST /admin/jackpots** - Create a new jackpot

#### Health & Monitoring
- **GET /health** - Service health check
- **GET /health/stats** - Service statistics

### Example API Usage

**Place a Bet:**
```json
POST /bets
{
  "userId": "user-123",
  "jackpotId": "main-jackpot",
  "betAmount": 100.00
}
```

**Evaluate Reward:**
```json
POST /rewards/evaluate
{
  "betId": "bet-123"
}
```

**Response (Winner):**
```json
{
  "betId": "bet-123",
  "userId": "user-123",
  "jackpotId": "jackpot-1",
  "isWinner": true,
  "rewardAmount": 1500.00,
  "message": "Congratulations! You won the jackpot!",
  "evaluatedAt": "2024-01-15T10:30:00"
}
```

## Configuration

### Application Properties

The service can be configured through `application.yml`:

```yaml
jackpot:
  topics:
    bets: jackpot-bets
  default-initial-pool: 1000.0
  strategies:
    fixed-contribution:
      percentage: 0.05  # 5%
    variable-contribution:
      initial-percentage: 0.10  # 10%
      decay-rate: 0.001  # 0.1% per pool increase
    fixed-reward:
      chance-percentage: 0.01  # 1%
    variable-reward:
      base-chance: 0.005  # 0.5%
      increase-rate: 0.0001  # 0.01% per pool increase
      max-chance: 1.0  # 100%
      trigger-limit: 10000.0  # Pool amount to trigger 100% chance
```

### Kafka Configuration

#### Development Mode (Mock Kafka - Default)
```yaml
kafka:
  enabled: false
```

#### Production Mode (Real Kafka)
```yaml
kafka:
  enabled: true

spring:
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: jackpot-service-group
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
```

## Architecture

### Domain Model
```
Bet (1) -----> (1) Jackpot
  |                |
  |                |
  v                v
Contribution    Reward
```

### Key Components
1. **Domain Layer**: Core business entities (Bet, Jackpot, Contribution, Reward)
2. **Repository Layer**: In-memory data access with thread-safe operations
3. **Service Layer**: Business logic and orchestration
4. **Controller Layer**: REST API endpoints
5. **Messaging Layer**: Kafka integration with mock fallback
6. **Strategy Layer**: Pluggable algorithms for contributions and rewards

### Design Patterns
- **Strategy Pattern**: For contribution and reward calculation algorithms
- **Repository Pattern**: For data access abstraction
- **Factory Pattern**: For strategy instantiation
- **Builder Pattern**: For object creation
- **DTO Pattern**: For API communication

## Development

### Project Structure
```
src/
├── main/
│   ├── java/com/sportygroup/jackpot/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # REST controllers
│   │   ├── domain/          # Domain entities
│   │   ├── dto/             # Data transfer objects
│   │   ├── messaging/       # Kafka integration
│   │   ├── repository/      # Data access layer
│   │   ├── service/         # Business logic
│   │   ├── strategy/        # Strategy implementations
│   │   └── JackpotServiceApplication.java
│   └── resources/
│       └── application.yml
└── test/
    ├── java/com/sportygroup/jackpot/
    │   ├── controller/      # Controller tests
    │   ├── service/         # Service tests
    │   └── strategy/         # Strategy tests
    └── resources/
        └── application-test.yml
```

### Adding New Strategies

1. Implement the strategy interface:
   ```java
   @Component
   public class CustomContributionStrategy implements ContributionStrategy {
       @Override
       public BigDecimal calculateContribution(BigDecimal betAmount, Jackpot jackpot) {
           // Custom logic
       }
       
       @Override
       public String getStrategyType() {
           return "CUSTOM";
       }
   }
   ```

2. Update the `StrategyFactory` to include the new strategy
3. Add configuration properties if needed
4. Write comprehensive tests

### Testing

```bash
# Run all tests
mvn test

# Run tests with coverage
mvn test jacoco:report

# Run specific test class
mvn test -Dtest=JackpotServiceTest
```

The project includes comprehensive test coverage:
- **Unit Tests**: Test individual components in isolation
- **Integration Tests**: Test component interactions
- **Controller Tests**: Test REST API endpoints
- **Strategy Tests**: Test contribution and reward algorithms
