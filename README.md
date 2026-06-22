# SmartCampus Connect

## Project Overview
SmartCampus Connect is a distributed backend system designed for managing university campus services. It supports student management, course enrolment, notifications, library services, and reporting.

The system is built using a service-oriented / microservices architecture and demonstrates REST APIs, SOAP web services, multithreading, and asynchronous messaging.

---

## System Architecture
The system follows a distributed multi-tier architecture:

- Client Layer: Postman / CLI / simple web interface
- Service Layer: Independent backend services
- Communication: REST APIs, SOAP services, and asynchronous messaging

Each service is independently deployable and owns its own database (database-per-service principle).

---

## Microservices

### Student Profile Service
- Provides CRUD operations for student data
- Exposes REST API endpoints
- Stores student demographic and academic information

### Course Enrolment Service
- Handles course registration, drop/add operations
- Performs capacity validation
- Depends on Student Profile Service

### Notification Service
- Sends system notifications for enrolment, payment, and library events
- Consumes messages asynchronously from other services

### Library / Booking Service
- Manages book loans and room reservations
- Provides both REST and SOAP (WSDL) interface for legacy integration

### Reporting / Analytics Service
- Aggregates data from multiple services
- Generates summary reports such as enrolment counts per programme

---

## Prerequisites
- Java 17 or above
- Maven installed
- Git installed

---

## How to Run the Project

### Step 1: Clone Repository
git clone https://github.com/txrou2004-crypto/smartcampus-connect-
cd SmartCampusConnect

---

### Step 2: Build the Project
mvn clean install

---

### Step 3: Run Services

Each service runs independently on different ports.

Student Profile Service:
cd student-service
mvn spring-boot:run

Course Enrolment Service:
cd enrolment-service
mvn spring-boot:run

Notification Service:
cd notification-service
mvn spring-boot:run

Library Service:
cd library-service
mvn spring-boot:run

Reporting Service:
cd reporting-service
mvn spring-boot:run

---

## API Testing

A Postman collection is provided for testing all REST endpoints.

Import:
postman/SmartCampusConnect.postman_collection.json

Example endpoints:

GET /students
POST /students
GET /enrolments
POST /enrolments
GET /reports

---

## SOAP Web Service

The Library Service exposes SOAP operations via WSDL.

WSDL URL:
http://localhost:<port>/ws/library?wsdl

A sample SOAP request and response is included in the documentation.

A SOAP Fault scenario is also demonstrated by triggering invalid requests.

---

## Asynchronous Messaging

The Notification Service consumes events asynchronously using:

- Java TCP sockets (Producer-Consumer pattern) OR
- Message broker (RabbitMQ / Kafka / ActiveMQ)

Message format:
JSON-based event structure containing event type, timestamp, and payload data.

---

## Multithreading

At least one service uses a thread pool (ExecutorService) to handle concurrent requests.

Shared mutable state is protected using synchronization mechanisms such as:
- synchronized blocks
- ReentrantLock
- concurrent collections (java.util.concurrent)

---

## Failure Handling

The system demonstrates fault tolerance using:
- Timeouts for service calls
- Retry mechanisms with idempotent operations
- Graceful degradation when dependent services are unavailable

---

## Version Control

This project uses Git for version control.

Each team member contributes through separate commits showing:
- feature implementation
- bug fixes
- service development

Commit history is maintained in a single shared repository.

---

## Build and Execution

The project supports a single command build:

mvn clean install

After build, services can be started individually as shown above.

---

## Team Members and Roles

Team Lead: Ling Jia Xuan
Architect: Tan Xin Rou
Backend Engineer: Hema Malini A/P R.Sundareswaran & Nor Nadhirah Binti Mazelan
Concurrency / Messaging Engineer: Alief Adam Bin Armizal Azdwan

---

## Notes
- Ensure all services are started before testing workflows
- Each service runs independently on its own port
- Database-per-service principle must be maintained
- Use Postman for full system testing
