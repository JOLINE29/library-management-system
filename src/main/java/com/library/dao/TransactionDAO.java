package com.library.dao;

import com.library.model.Transaction;
import com.library.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    
    public void addTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (book_id, member_id, issue_date, due_date, fine_amount, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, transaction.getBookId());
            stmt.setLong(2, transaction.getMemberId());
            stmt.setDate(3, Date.valueOf(transaction.getIssueDate()));
            stmt.setDate(4, Date.valueOf(transaction.getDueDate()));
            stmt.setBigDecimal(5, transaction.getFineAmount());
            stmt.setString(6, transaction.getStatus());
            stmt.executeUpdate();
        }
    }

    public void updateTransaction(Transaction transaction) throws SQLException {
        String sql = "UPDATE transactions SET return_date = ?, fine_amount = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, transaction.getReturnDate() != null ? Date.valueOf(transaction.getReturnDate()) : null);
            stmt.setBigDecimal(2, transaction.getFineAmount());
            stmt.setString(3, transaction.getStatus());
            stmt.setLong(4, transaction.getId());
            stmt.executeUpdate();
        }
    }

    public List<Transaction> findByMemberId(Long memberId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE member_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, memberId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        }
        return transactions;
    }

    public Transaction findActiveTransactionByBookAndMember(Long bookId, Long memberId) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE book_id = ? AND member_id = ? AND status = 'ISSUED'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, bookId);
            stmt.setLong(2, memberId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToTransaction(rs);
            }
        }
        return null;
    }

    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setId(rs.getLong("id"));
        transaction.setBookId(rs.getLong("book_id"));
        transaction.setMemberId(rs.getLong("member_id"));
        transaction.setIssueDate(rs.getDate("issue_date").toLocalDate());
        transaction.setDueDate(rs.getDate("due_date").toLocalDate());
        Date returnDate = rs.getDate("return_date");
        if (returnDate != null) {
            transaction.setReturnDate(returnDate.toLocalDate());
        }
        transaction.setFineAmount(rs.getBigDecimal("fine_amount"));
        transaction.setStatus(rs.getString("status"));
        return transaction;
    }
}