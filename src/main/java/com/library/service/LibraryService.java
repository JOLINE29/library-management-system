package com.library.service;

import com.library.dao.*;
import com.library.model.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

public class LibraryService {
    private BookDAO bookDAO = new BookDAO();
    private MemberDAO memberDAO = new MemberDAO();
    private TransactionDAO transactionDAO = new TransactionDAO();

    public void addBook(Book book) throws SQLException {
        bookDAO.addBook(book);
    }

    public void addMember(Member member) throws SQLException {
        memberDAO.addMember(member);
    }

    public boolean issueBook(Long bookId, Long memberId) throws SQLException {
        Book book = bookDAO.findById(bookId);
        if (book != null && book.getAvailableCopies() > 0) {
            Transaction transaction = new Transaction(bookId, memberId, LocalDate.now(), LocalDate.now().plusDays(14));
            transactionDAO.addTransaction(transaction);
            bookDAO.updateAvailableCopies(bookId, book.getAvailableCopies() - 1);
            return true;
        }
        return false;
    }

    public boolean returnBook(Long bookId, Long memberId) throws SQLException {
        Transaction transaction = transactionDAO.findActiveTransactionByBookAndMember(bookId, memberId);
        if (transaction != null) {
            transaction.setReturnDate(LocalDate.now());
            transaction.setStatus("RETURNED");
            
            // Calculate fine if overdue
            if (LocalDate.now().isAfter(transaction.getDueDate())) {
                long overdueDays = LocalDate.now().toEpochDay() - transaction.getDueDate().toEpochDay();
                transaction.setFineAmount(BigDecimal.valueOf(overdueDays * 1.0)); // $1 per day
            }
            
            transactionDAO.updateTransaction(transaction);
            
            Book book = bookDAO.findById(bookId);
            bookDAO.updateAvailableCopies(bookId, book.getAvailableCopies() + 1);
            return true;
        }
        return false;
    }

    public List<Book> getAllBooks() throws SQLException {
        return bookDAO.findAll();
    }

    public List<Member> getAllMembers() throws SQLException {
        return memberDAO.findAll();
    }

    public List<Transaction> getMemberTransactions(Long memberId) throws SQLException {
        return transactionDAO.findByMemberId(memberId);
    }
}