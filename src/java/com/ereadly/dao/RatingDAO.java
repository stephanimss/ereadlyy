/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ereadly.dao;

import com.ereadly.config.DatabaseConfig;
import com.ereadly.model.Book;
import com.ereadly.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RatingDAO {
    
    public boolean addRating(int userId, int bookId, int score, String comment) {
        String sql = "INSERT INTO ratings (user_id, book_id, score, comment) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            ps.setInt(2, bookId);
            ps.setInt(3, score);
            ps.setString(4, comment);
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void rateBook(Book book, double score) {
        String insertRating = 
            "INSERT INTO ratings (book_id, skor) VALUES (?, ?)";

        String updateBookRating = 
            "UPDATE books SET rating = (SELECT AVG(skor) FROM ratings WHERE book_id = ?) WHERE book_id = ?";

        try (Connection conn = DBConnection.getConnection()) {

            // simpan rating baru
            PreparedStatement psInsert = conn.prepareStatement(insertRating);
            psInsert.setInt(1, book.getBookId());
            psInsert.setDouble(2, score);
            psInsert.executeUpdate();

            // update rata-rata rating buku
            PreparedStatement psUpdate = conn.prepareStatement(updateBookRating);
            psUpdate.setInt(1, book.getBookId());
            psUpdate.setInt(2, book.getBookId());
            psUpdate.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

