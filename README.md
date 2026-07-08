SMARTCAMPUS CONNECT
Project Overview

SmartCampus Connect is a distributed backend platform designed to support essential university operations through a collection of independently deployable services. The system demonstrates the application of distributed systems concepts including Service-Oriented Architecture (SOA), event-driven communication, RESTful services, SOAP integration, concurrency handling, fault tolerance, and database-per-service design.

The platform consists of five core services:
Student Profile Service
Course Enrolment Service
Notification Service
Library & Booking Service
Reporting & Analytics Service
Each service owns its own database and communicates with other services through REST APIs and asynchronous events. This architecture promotes scalability, maintainability, loose coupling, and fault isolation.

Architecture
SmartCampus Connect adopts a Three-Tier Client-Server Architecture combined with distributed service components and choreography-based service composition.

Architecture Layers
Presentation Layer
Client applications used by end users:
Web Portal
Mobile Application
Administrative Console
Postman/API Clients

Service Layer
Core backend services:
Student Profile Service
Course Enrolment Service
Notification Service
Library & Booking Service
Reporting & Analytics Service

Data Layer
Independent databases:
studentdb
enrolmentdb
notificationdb
librarydb
analyticsdb

---

Services
Student Profile Service
Responsibilities
Manage student information
Maintain academic records
Validate student status
Provide student data to other services
Database

studentdb

Tables
students
Column	Type
student_id	PK
first_name	VARCHAR
last_name	VARCHAR
email	VARCHAR
tel_number	VARCHAR
programme_code	VARCHAR
status	ENUM
academic_records
Column	Type
record_id	PK
student_id	FK
academic_year	VARCHAR
semester	INT
gpa	DECIMAL
cgpa	DECIMAL
credits_earned	INT

Relationship:

One Student → Many Academic Records

Course Enrolment Service
Responsibilities
Course registration
Course withdrawal
Student validation
Capacity checking
Event publishing
Database

enrolmentdb

Tables
courses
course_id
course_name
credit_hours
max_capacity
available_seats
enrolments
enrolment_id
student_id
course_id
enrolment_date
status
Published Events
StudentEnrolled
StudentDropped
Notification Service
Responsibilities
Send enrolment notifications
Send reservation notifications
Manage notification history
Database

notificationdb

Tables
notifications
notification_id
student_id
message
notification_type
sent_time
status
Consumed Events
StudentEnrolled
StudentDropped
BookReserved
Library & Booking Service
Responsibilities
Manage books
Book reservation
Availability checking
Event publishing
Database

librarydb

Tables
books
book_id
title
author
available_copies
reservations
reservation_id
student_id
book_id
reservation_date
Published Events
BookReserved
BookReturned
SOAP Operation
reserveBook()

or

checkBookAvailability()

The SOAP endpoint demonstrates integration with legacy systems while REST APIs support modern clients.

Reporting & Analytics Service
Responsibilities
Generate reports
Aggregate statistics
Track enrolment trends
Monitor library usage
Database

analyticsdb

Tables
analytics_summary
analytics_id
event_type
total_count
updated_at
Consumed Events
StudentEnrolled
StudentDropped
BookReserved
Database Design

The system follows the Database-per-Service Pattern.

Service	Database
Student Profile Service	studentdb
Course Enrolment Service	enrolmentdb
Notification Service	notificationdb
Library & Booking Service	librarydb
Reporting & Analytics Service	analyticsdb

Important:

Services do not directly access other databases.
Cross-service communication is performed through REST APIs, SOAP services, and event messaging.
This ensures loose coupling and fault isolation.
Communication Model
Synchronous Communication

REST APIs are used when immediate responses are required.

Examples:

Validate Student
Retrieve Student Profile
Check Course Information
Check Book Availability
Asynchronous Communication

Events are used for background processing.

Examples:

StudentEnrolled
StudentDropped
BookReserved
BookReturned

Benefits:

Reduced coupling
Better scalability
Improved fault tolerance
Technologies
Backend
Java
Spring Boot
Spring Web
Spring Data JPA
Communication
REST API
SOAP/WSDL
Event-Driven Messaging
Database
MySQL
Testing
Postman
Build Tool
Maven
Key Distributed System Features
Transparency
Location Transparency
Access Transparency
Replication Transparency
Failure Transparency
Concurrency Transparency
Mobility Transparency
Performance Transparency
Scaling Transparency
Fault Tolerance
Retry Mechanisms
Timeouts
Graceful Degradation
Service Isolation
Scalability
Independent Service Deployment
Database-per-Service Design
Event-Driven Communication

Prerequisites
Java 17+
Maven
MySQL
Git
Postman
Setup
Clone the repository
git clone <https://github.com/txrou2004-crypto/smartcampus-connect->
Create all required databases
studentdb
enrolmentdb
notificationdb
librarydb
analyticsdb
Execute the provided DDL scripts.
Execute the provided DML scripts.
Configure database credentials in application.properties.
Start each service individually.
Test APIs using Postman.

## **Team Members and Roles**

Team Lead:  LING JIA XUAN
Architect:  TAN XIN ROU
Backend Engineer:   HEMA MALINI A/P R.SUNDARESWARAN & NOR NADHIRAH BINTI MAZELAN
Concurrency / Messaging Engineer:  ALIEF ADAM BIN ARMIZAL AZDWAN

---
