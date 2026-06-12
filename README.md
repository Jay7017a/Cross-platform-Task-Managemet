Cross-Platform Task Management Application

Overview

Cross-Platform Task Management Application is a backend system developed using Java and Spring Boot to help users efficiently manage daily tasks. The application provides secure and scalable REST APIs for creating, updating, tracking, and deleting tasks. It follows a layered architecture and integrates with MySQL for persistent data storage.

Tech Stack

- Language: Java 17
- Framework: Spring Boot 3.x
- Database: MySQL
- ORM: Spring Data JPA / Hibernate
- Build Tool: Maven
- API Testing: Postman
- Version Control: Git & GitHub
- Architecture: Layered MVC Architecture
- Tools: IntelliJ IDEA / Eclipse

Features

- Create new tasks
- View all tasks
- Update task details
- Delete tasks
- Mark tasks as completed or pending
- Filter tasks based on status
- Database integration with MySQL
- RESTful APIs for task operations
- Exception handling and validation
- Scalable and maintainable architecture

Project Structure

src/main/java
 ├── controller
 ├── service
 ├── repository
 ├── entity
 ├── dto
 ├── exception
 └── config

API Endpoints

Method| Endpoint| Description
POST| /api/tasks| Create a task
GET| /api/tasks| Get all tasks
GET| /api/tasks/{id}| Get task by ID
PUT| /api/tasks/{id}| Update task
DELETE| /api/tasks/{id}| Delete task
PATCH| /api/tasks/{id}/status| Update task status

Database

MySQL

Sample Entity Fields:

- id
- title
- description
- priority
- dueDate
- status
- createdAt

How to Run the Project

1. Clone Repository

git clone https://github.com/Jay7017a/Cross-platform-Task-Management.git

2. Open Project

Open the project in IntelliJ IDEA or Eclipse.

3. Configure Database

Create a MySQL database:

CREATE DATABASE task_management;

Update "application.properties":

spring.datasource.url=jdbc:mysql://localhost:3306/task_management
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

4. Build Project

mvn clean install

5. Run Application

mvn spring-boot:run

Application runs on:

http://localhost:8080

6. Test APIs

Use Postman to test all REST endpoints.

Future Enhancements

- JWT Authentication and Authorization
- Role-Based Access Control
- Email Notifications
- Task Categories and Tags
- Priority-based Scheduling
- Docker Deployment
- Microservices Architecture

Author

Jayasree Banda

GitHub:
https://github.com/Jay7017a/Cross-platform-Task-Management
