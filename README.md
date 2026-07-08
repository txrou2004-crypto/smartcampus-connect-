## **SMARTCAMPUS CONNECT**
## Project Overview

SmartCampus Connect is a distributed backend platform designed to support essential university operations through a collection of independently deployable services. The system demonstrates the application of distributed systems concepts including Service-Oriented Architecture (SOA), event-driven communication, RESTful services, SOAP integration, concurrency handling, fault tolerance, and database-per-service design.

Below is the link to the demonstration video: https://www.youtube.com/watch?v=lxR3VgNozos

The platform consists of five core services:
Student Profile Service
Course Enrolment Service
Notification Service
Library & Booking Service
Reporting & Analytics Service
Each service owns its own database and communicates with other services through REST APIs and asynchronous events. This architecture promotes scalability, maintainability, loose coupling, and fault isolation.

---

# Architecture

SmartCampus Connect adopts a **Three-Tier Client-Server Architecture** combined with **distributed service components** and **choreography-based service composition**.

## Architecture Layers

### Presentation Layer

Client applications used by end users:

- Web Portal
- Mobile Application
- Administrative Console
- Postman / API Clients

### Service Layer

Core backend services:

- Student Profile Service
- Course Enrolment Service
- Notification Service
- Library & Booking Service
- Reporting & Analytics Service

### Data Layer

Independent databases:

- studentdb
- enrolmentdb
- notificationdb
- librarydb
- analyticsdb

---

# Services

## Student Profile Service

### Responsibilities

- Manage student information
- Maintain academic records
- Validate student status
- Provide student data to other services

### Database

**studentdb**

### Tables

#### students

| Column | Type |
|--------|------|
| student_id | PK |
| first_name | VARCHAR |
| last_name | VARCHAR |
| email | VARCHAR |
| tel_number | VARCHAR |
| programme_code | VARCHAR |
| status | ENUM |

#### academic_records

| Column | Type |
|--------|------|
| record_id | PK |
| student_id | FK |
| academic_year | VARCHAR |
| semester | INT |
| gpa | DECIMAL |
| cgpa | DECIMAL |
| credits_earned | INT |

Relationship:

**One Student → Many Academic Records**

---

## Course Enrolment Service

### Responsibilities

- Course registration
- Course withdrawal
- Student validation
- Capacity checking
- Publish enrolment events

### Database

**enrolmentdb**

### Tables

#### courses

- course_id
- course_name
- credit_hours
- max_capacity
- available_seats

#### enrolments

- enrolment_id
- student_id
- course_id
- enrolment_date
- status

### Published Events

- StudentEnrolled
- StudentDropped

---

## Notification Service

### Responsibilities

- Send enrolment notifications
- Send reservation notifications
- Manage notification history

### Database

**notificationdb**

### Tables

#### notifications

- notification_id
- student_id
- message
- notification_type
- sent_time
- status

### Consumed Events

- StudentEnrolled
- StudentDropped
- BookReserved

---

## Library & Booking Service

### Responsibilities

- Manage books
- Book reservation
- Check book availability
- Publish reservation events

### Database

**librarydb**

### Tables

#### books

- book_id
- title
- author
- available_copies

#### reservations

- reservation_id
- student_id
- book_id
- reservation_date

### Published Events

- BookReserved
- BookReturned

### SOAP Operations

```java
reserveBook();
```

or

```java
checkBookAvailability();
```

The SOAP endpoint demonstrates integration with legacy systems, while REST APIs support communication with modern applications.

---

## Reporting & Analytics Service

### Responsibilities

- Generate reports
- Aggregate statistics
- Track enrolment trends
- Monitor library usage

### Database

**analyticsdb**

### Tables

#### analytics_summary

- analytics_id
- event_type
- total_count
- updated_at

### Consumed Events

- StudentEnrolled
- StudentDropped
- BookReserved

---

# Database Design

The project follows the **Database-per-Service Pattern**.

| Service | Database |
|----------|----------|
| Student Profile Service | studentdb |
| Course Enrolment Service | enrolmentdb |
| Notification Service | notificationdb |
| Library & Booking Service | librarydb |
| Reporting & Analytics Service | analyticsdb |

### Design Principles

- Each service owns its own database.
- Services never access another service's database directly.
- Cross-service communication occurs through REST APIs, SOAP services, and asynchronous event messaging.
- This design improves loose coupling, scalability, and fault isolation.

---

# Communication Model

## Synchronous Communication (REST)

Used when an immediate response is required.

Examples:

- Validate Student
- Retrieve Student Profile
- Check Course Availability
- Check Book Availability

## Asynchronous Communication (Events)

Used for background processing.

Published Events:

- StudentEnrolled
- StudentDropped
- BookReserved
- BookReturned

### Benefits

- Loose coupling
- Better scalability
- Improved fault tolerance
- Independent service execution

---

# Technologies

### Backend

- Java 17+

### Communication

- REST API
- SOAP/WSDL
- Event-Driven Messaging

### Database

- MySQL

### Build Tool

- Maven

### Testing

- Postman

---

# Distributed System Features

## Transparency

- Location Transparency
- Access Transparency
- Replication Transparency
- Failure Transparency
- Concurrency Transparency
- Mobility Transparency
- Performance Transparency
- Scaling Transparency

## Fault Tolerance

- Retry Mechanisms
- Timeouts
- Graceful Degradation
- Service Isolation

## Scalability

- Independent Service Deployment
- Database-per-Service Pattern
- Event-Driven Communication

---

## **Team Members and Roles**

- Team Lead:  LING JIA XUAN
- Architect:  TAN XIN ROU
- Backend Engineer:   HEMA MALINI A/P R.SUNDARESWARAN & NOR NADHIRAH BINTI MAZELAN
- Concurrency / Messaging Engineer:  ALIEF ADAM BIN ARMIZAL AZDWAN

---
