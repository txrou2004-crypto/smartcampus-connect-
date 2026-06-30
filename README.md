# SmartCampus Connect

## Project Overview
SmartCampus Connect is a distributed backend platform designed to support essential university operations through a collection of independently deployable services. The system demonstrates the application of distributed systems concepts including Service-Oriented Architecture (SOA), event-driven communication, RESTful services, SOAP integration, concurrency handling, fault tolerance, and database-per-service design.

The platform consists of five core services:

Student Profile Service
Course Enrolment Service
Notification Service
Library & Booking Service
Reporting & Analytics Service

Each service owns its own database and communicates with other services through REST APIs and asynchronous events. This architecture promotes scalability, maintainability, loose coupling, and fault isolation.

---

## System Architecture
SmartCampus Connect adopts a Three-Tier Client-Server Architecture combined with distributed service components and choreography-based service composition.
Presentation Layer (Client applications used by end users):
Web Portal
Mobile Application
Administrative Console
Postman/API Clients

Service Layer (Core backend services):
Student Profile Service
Course Enrolment Service
Notification Service
Library & Booking Service
Reporting & Analytics Service

Data Layer (Independent databases):
studentdb
enrolmentdb
notificationdb
librarydb
analyticsdb

---

## Microservices

### Student Profile Service
Manage student information
Maintain academic records
Validate student status
Provide student data to other services

### Course Enrolment Service
Course registration
Course withdrawal
Student validation
Capacity checking
Event publishing

### Notification Service
Send enrolment notifications
Send reservation notifications
Manage notification history

### Library / Booking Service
Manage books
Book reservation
Room reservation
Availability checking
Event publishing

### Reporting / Analytics Service
Generate reports
Aggregate statistics
Track enrolment trends
Monitor library usage

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
