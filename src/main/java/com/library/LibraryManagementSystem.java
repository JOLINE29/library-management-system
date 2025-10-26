package com.library;

import com.library.model.*;
import com.library.service.LibraryService;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class LibraryManagementSystem {
    private static LibraryService libraryService = new LibraryService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Library Management System ===");
        
        while (true) {
            showMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            try {
                switch (choice) {
                    case 1: addBook(); break;
                    case 2: addMember(); break;
                    case 3: issueBook(); break;
                    case 4: returnBook(); break;
                    case 5: viewAllBooks(); break;
                    case 6: viewAllMembers(); break;
                    case 7: viewMemberTransactions(); break;
                    case 8: System.exit(0); break;
                    default: System.out.println("Invalid choice!");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        }
    }

    private static void showMenu() {
        System.out.println("\n1. Add Book");
        System.out.println("2. Add Member");
        System.out.println("3. Issue Book");
        System.out.println("4. Return Book");
        System.out.println("5. View All Books");
        System.out.println("6. View All Members");
        System.out.println("7. View Member Transactions");
        System.out.println("8. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addBook() throws SQLException {
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author: ");
        String author = scanner.nextLine();
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        System.out.print("Enter total copies: ");
        int copies = scanner.nextInt();
        
        Book book = new Book(title, author, isbn, category, copies);
        libraryService.addBook(book);
        System.out.println("Book added successfully!");
    }

    private static void addMember() throws SQLException {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        
        Member member = new Member(name, email, phone, address);
        libraryService.addMember(member);
        System.out.println("Member added successfully!");
    }

    private static void issueBook() throws SQLException {
        System.out.print("Enter book ID: ");
        Long bookId = scanner.nextLong();
        System.out.print("Enter member ID: ");
        Long memberId = scanner.nextLong();
        
        if (libraryService.issueBook(bookId, memberId)) {
            System.out.println("Book issued successfully!");
        } else {
            System.out.println("Book not available or not found!");
        }
    }

    private static void returnBook() throws SQLException {
        System.out.print("Enter book ID: ");
        Long bookId = scanner.nextLong();
        System.out.print("Enter member ID: ");
        Long memberId = scanner.nextLong();
        
        if (libraryService.returnBook(bookId, memberId)) {
            System.out.println("Book returned successfully!");
        } else {
            System.out.println("No active transaction found!");
        }
    }

    private static void viewAllBooks() throws SQLException {
        List<Book> books = libraryService.getAllBooks();
        System.out.println("\n=== All Books ===");
        for (Book book : books) {
            System.out.printf("ID: %d | Title: %s | Author: %s | Available: %d/%d%n",
                book.getId(), book.getTitle(), book.getAuthor(), 
                book.getAvailableCopies(), book.getTotalCopies());
        }
    }

    private static void viewAllMembers() throws SQLException {
        List<Member> members = libraryService.getAllMembers();
        System.out.println("\n=== All Members ===");
        for (Member member : members) {
            System.out.printf("ID: %d | Name: %s | Email: %s | Status: %s%n",
                member.getId(), member.getName(), member.getEmail(), member.getStatus());
        }
    }

    private static void viewMemberTransactions() throws SQLException {
        System.out.print("Enter member ID: ");
        Long memberId = scanner.nextLong();
        
        List<Transaction> transactions = libraryService.getMemberTransactions(memberId);
        System.out.println("\n=== Member Transactions ===");
        for (Transaction transaction : transactions) {
            System.out.printf("Book ID: %d | Issue Date: %s | Due Date: %s | Status: %s | Fine: $%.2f%n",
                transaction.getBookId(), transaction.getIssueDate(), 
                transaction.getDueDate(), transaction.getStatus(), transaction.getFineAmount());
        }
    }
}