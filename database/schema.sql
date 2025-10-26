CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;

CREATE TABLE books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) UNIQUE NOT NULL,
    category VARCHAR(100),
    total_copies INT NOT NULL DEFAULT 1,
    available_copies INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20),
    address TEXT,
    membership_date DATE NOT NULL,
    status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE'
);

CREATE TABLE transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    issue_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    fine_amount DECIMAL(10,2) DEFAULT 0.00,
    status ENUM('ISSUED', 'RETURNED', 'OVERDUE') DEFAULT 'ISSUED',
    FOREIGN KEY (book_id) REFERENCES books(id),
    FOREIGN KEY (member_id) REFERENCES members(id)
);

-- Sample data
INSERT INTO books (title, author, isbn, category, total_copies, available_copies) VALUES
('Java Programming', 'John Smith', '978-0123456789', 'Programming', 5, 5),
('Database Systems', 'Jane Doe', '978-0987654321', 'Database', 3, 3);

INSERT INTO members (name, email, phone, address, membership_date) VALUES
('Alice Johnson', 'alice@email.com', '555-0101', '123 Main St', '2024-01-01'),
('Bob Wilson', 'bob@email.com', '555-0102', '456 Oak Ave', '2024-01-02');