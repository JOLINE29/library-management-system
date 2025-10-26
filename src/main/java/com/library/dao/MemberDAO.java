package com.library.dao;

import com.library.model.Member;
import com.library.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {
    
    public void addMember(Member member) throws SQLException {
        String sql = "INSERT INTO members (name, email, phone, address, membership_date, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, member.getName());
            stmt.setString(2, member.getEmail());
            stmt.setString(3, member.getPhone());
            stmt.setString(4, member.getAddress());
            stmt.setDate(5, Date.valueOf(member.getMembershipDate()));
            stmt.setString(6, member.getStatus());
            stmt.executeUpdate();
        }
    }

    public Member findById(Long id) throws SQLException {
        String sql = "SELECT * FROM members WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToMember(rs);
            }
        }
        return null;
    }

    public List<Member> findAll() throws SQLException {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                members.add(mapResultSetToMember(rs));
            }
        }
        return members;
    }

    private Member mapResultSetToMember(ResultSet rs) throws SQLException {
        Member member = new Member();
        member.setId(rs.getLong("id"));
        member.setName(rs.getString("name"));
        member.setEmail(rs.getString("email"));
        member.setPhone(rs.getString("phone"));
        member.setAddress(rs.getString("address"));
        member.setMembershipDate(rs.getDate("membership_date").toLocalDate());
        member.setStatus(rs.getString("status"));
        return member;
    }
}