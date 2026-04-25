# Event-Driven Movie Ticket System

## Services And Ports

- User Service: 8081
- Movie Service: 8082
- Booking Service: 8083
- Payment Notification Service: 8084
- API Gateway: 8080
- Frontend: 3000
- RabbitMQ: 5672 (broker), 15672 (management)

## Architecture Flow

1. User registers and User Service publishes USER_REGISTERED.
2. User creates booking and Booking Service publishes BOOKING_CREATED.
3. Payment Notification Service consumes BOOKING_CREATED and simulates payment success or failure.
4. Payment Notification Service publishes PAYMENT_COMPLETED or BOOKING_FAILED.
5. Notification listeners log success or failure messages.

## Run Local

### 1) Start RabbitMQ

Use Docker Desktop:

docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management

RabbitMQ UI: http://localhost:15672 (guest/guest)

### 2) Start Backend Services

Open a terminal for each service and run:

- user-service: .\mvnw.cmd spring-boot:run
- movie-service: .\mvnw.cmd spring-boot:run
- booking-service: .\mvnw.cmd spring-boot:run
- payment-notification-service: .\mvnw.cmd spring-boot:run
- api-gateway: .\mvnw.cmd spring-boot:run

### 3) Start Frontend

From frontend directory:

npm install
npm start

Open http://localhost:3000

## Run With Docker Compose

Build JARs first:

- user-service: .\mvnw.cmd -DskipTests package
- movie-service: .\mvnw.cmd -DskipTests package
- booking-service: .\mvnw.cmd -DskipTests package
- payment-notification-service: .\mvnw.cmd -DskipTests package
- api-gateway: .\mvnw.cmd -DskipTests package

Then from repository root:

docker compose up --build

## Mandatory Demo Scenario

1. Register user at frontend and verify USER_REGISTERED log in User Service.
2. Login and create booking from Movies page.
3. Verify BOOKING_CREATED log in Booking Service.
4. Verify Payment listener receives event and publishes payment result.
5. Verify Notification logs success or failure.