#!/bin/bash

# Jackpot Service with Kafka Setup Script
# This script helps you quickly start the Jackpot Service with Kafka

echo "🎰 Jackpot Service with Kafka Setup"
echo "=================================="
echo ""

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker first."
    echo "   You can also use manual Kafka installation (see README.md)"
    exit 1
fi

echo "🐳 Starting Kafka with Docker Compose..."
docker-compose up -d

echo ""
echo "⏳ Waiting for Kafka to be ready..."
sleep 10

echo ""
echo "🔍 Checking Kafka status..."
# Check if Kafka is running
if docker-compose ps | grep -q "kafka.*Up"; then
    echo "✅ Kafka is running!"
else
    echo "❌ Kafka failed to start. Check logs with: docker-compose logs kafka"
    exit 1
fi

echo ""
echo "📋 Creating required Kafka topic..."
# Create the jackpot-bets topic
docker-compose exec kafka kafka-topics \
  --bootstrap-server localhost:9092 \
  --create --topic jackpot-bets \
  --partitions 1 --replication-factor 1 \
  --if-not-exists

echo ""
echo "🔧 Enabling Kafka in application configuration..."
# Create a temporary application-kafka.yml
cat > application-kafka.yml << EOF
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
EOF

echo ""
echo "🚀 Starting Jackpot Service with Kafka..."
echo "   The service will be available at: http://localhost:8080"
echo "   Kafka UI will be available at: http://localhost:8082"
echo ""

# Start the service with Kafka configuration
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.config.additional-location=application-kafka.yml"

echo ""
echo "🎉 Setup complete!"
echo ""
echo "📊 Useful commands:"
echo "   - Monitor Kafka messages: docker-compose exec kafka kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic jackpot-bets --from-beginning"
echo "   - View Kafka UI: http://localhost:8082"
echo "   - Test API: curl http://localhost:8080/api/v1/health"
echo "   - Stop services: docker-compose down"

