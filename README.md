# Event-Driven Microservices with Kafka 

This repository demonstrates an example of **event-driven microservices architecture** using the **Saga pattern** and **compensation transactions**. The project showcases how microservices can communicate asynchronously via Kafka, enabling distributed transactions and eventual consistency.

## Overview

In this small pet project, there are several microservices communicating with each other through Kafka topics. Each microservice is designed to perform a specific domain function and is part of the overall business process. The project includes:

- **Products Microservice**
- **Payments Microservice**
- **Orders Microservice**
- **Credit Card Processor Microservice**
- **Kafka UI (for monitoring)**

### Key Features

- **Event-driven architecture**: Microservices communicate asynchronously through Kafka events.
- **Saga pattern**: Each microservice handles its part of the transaction and can trigger compensation actions if something goes wrong.
- **Kafka-based messaging**: Kafka is used for event sourcing and messaging between services.
- **Compensation transactions**: In case of failure, compensating transactions are triggered to revert changes made during the process.

## Services

### 1. **Products Service**

This microservice handles operations related to products. It produces events to Kafka topics and listens to commands for updates.

- **Exposed on port**: `8081`
- **Kafka consumer group ID**: `products-ms`

### 2. **Payments Service**

This microservice is responsible for processing payments. It interacts with the credit card processor service and produces events when payments are processed.

- **Exposed on port**: `8082`
- **Kafka consumer group ID**: `payments-ms`
- **Remote Credit Card Processor URL**: `http://ccp:8084`

### 3. **Orders Service**

This microservice manages customer orders. It listens to product and payment events and processes orders accordingly.

- **Exposed on port**: `8080`
- **Kafka consumer group ID**: `orders-ms`
- **Database**: In-memory H2 database (for demo purposes)

### 4. **Credit Card Processor Service**

This microservice processes credit card transactions. It simulates a payment processor and can be connected to third-party payment systems.

- **Exposed on port**: `8084`

### 5. **Kafka UI**

A Kafka monitoring UI tool that lets you interact with and monitor Kafka topics, producers, and consumers.

- **Exposed on port**: `8090`

### 6. **Kafka Brokers (3 instances)**

Kafka is the backbone of the messaging system. Three Kafka brokers are used for fault tolerance and scalability.

- **Ports**: `9091`, `9092`, `9093`
- **Listener ports**: `9192` (for internal communication)

## Architecture

The architecture follows the **Saga pattern**, which is a sequence of local transactions where each service performs its task and, if necessary, triggers compensating actions.

1. **Products** service emits events when a product is created, updated, or deleted.
2. **Orders** service listens to product events and processes them, initiating the order creation flow.
3. **Payments** service listens to order events and processes payments.
4. If any step fails, compensation actions are triggered to undo the changes made by previous steps.

Kafka topics are used for communication between services, where the events are published and consumed asynchronously.

## Setup

To run the project locally, you'll need Docker and Docker Compose installed.

### Steps:

1. Clone the repository:
   ```bash
   git clone <repository_url>
   cd <repository_folder>
