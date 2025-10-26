# Library Management System

A simple Java-based library management system using MySQL database.

## Features

- Add books and members
- Issue and return books
- Track transactions and calculate fines
- View all books, members, and transaction history

## Prerequisites

- Java 11 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

## Setup Instructions

1. **Database Setup**
   - Install MySQL and create a database
   - Run the SQL script in `database/schema.sql` to create tables and sample data
   - Update database credentials in `src/main/resources/db.properties`

2. **Build and Run**
   ```bash
   mvn clean compile
   mvn exec:java
   ```

## Project Structure

```
src/main/java/com/library/
├── model/          # Entity classes (Book, Member, Transaction)
├── dao/            # Data Access Objects
├── service/        # Business logic layer
├── util/           # Database connection utility
└── LibraryManagementSystem.java  # Main application
```

## Database Configuration

Update `src/main/resources/db.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/library_db
db.username=your_username
db.password=your_password
db.driver=com.mysql.cj.jdbc.Driver
```

## Usage

The application provides a console-based menu with the following options:
1. Add Book
2. Add Member
3. Issue Book
4. Return Book
5. View All Books
6. View All Members
7. View Member Transactions
8. Exit