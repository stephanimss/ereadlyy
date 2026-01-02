package com.ereadly.dao;

import com.ereadly.config.DatabaseConfig;
import com.ereadly.exception.BookNotAvailableException;
import com.ereadly.model.Book;
import com.ereadly.model.Loan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoanDAO {

    public void createLoan(Loan loan) throws BookNotAvailableException {
        if (loan == null || loan.getBook() == null || loan.getMember() == null) {
            throw new IllegalArgumentException("Loan/Book/Member tidak boleh null");
        }

        int bookId = loan.getBook().getIdBuku();     // sesuaikan jika method beda
        int userId = loan.getMember().getIdUser();   // sesuaikan jika method beda

        String qCheckStock = "SELECT stok FROM books WHERE id_buku=?";
        String qInsertLoan = "INSERT INTO loans (id_user, id_buku, tanggal_pinjam, tanggal_kembali, status) "
                           + "VALUES (?, ?, ?, ?, ?)";
        String qUpdateStock = "UPDATE books SET stok=? WHERE id_buku=?";

        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);

            int stok;
            try (PreparedStatement ps = conn.prepareStatement(qCheckStock)) {
                ps.setInt(1, bookId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        throw new BookNotAvailableException("Buku tidak ditemukan");
                    }
                    stok = rs.getInt("stok");
                }
            }

            if (stok <= 0) {
                conn.rollback();
                throw new BookNotAvailableException("Stok buku habis");
            }

            if (loan.getBorrowDate() == null) loan.setBorrowDate(new Date());

            // tanggal_kembali (jatuh tempo) default +7 hari
            Date due = new Date(loan.getBorrowDate().getTime() + 7L * 24L * 60L * 60L * 1000L);

            try (PreparedStatement ps = conn.prepareStatement(qInsertLoan)) {
                ps.setInt(1, userId);
                ps.setInt(2, bookId);
                ps.setDate(3, new java.sql.Date(loan.getBorrowDate().getTime()));
                ps.setDate(4, new java.sql.Date(due.getTime()));
                ps.setString(5, "dipinjam");
                ps.executeUpdate();
            }

            int newStock = stok - 1;
            try (PreparedStatement ps = conn.prepareStatement(qUpdateStock)) {
                ps.setInt(1, newStock);
                ps.setInt(2, bookId);
                ps.executeUpdate();
            }

            conn.commit();

            // sinkron object
            loan.getBook().setStok(newStock);

        } catch (BookNotAvailableException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("createLoan gagal: " + e.getMessage(), e);
        }
    }
    
    public boolean borrowBook(int userId, int bookId) {
        Connection conn = null;
        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false); 
            
            String sqlCheck = "SELECT stock FROM books WHERE id = ?";
            PreparedStatement psCheck = conn.prepareStatement(sqlCheck);
            psCheck.setInt(1, bookId);
            ResultSet rs = psCheck.executeQuery();
            
            if (rs.next() && rs.getInt("stock") > 0) {
                String sqlUpdateStock = "UPDATE books SET stock = stock - 1 WHERE id = ?";
                PreparedStatement psUpdate = conn.prepareStatement(sqlUpdateStock);
                psUpdate.setInt(1, bookId);
                psUpdate.executeUpdate();

                String sqlLoan = "INSERT INTO loans (user_id, book_id, loan_date, due_date, status) VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 7 DAY), 'Borrowed')";
                PreparedStatement psLoan = conn.prepareStatement(sqlLoan);
                psLoan.setInt(1, userId);
                psLoan.setInt(2, bookId);
                psLoan.executeUpdate();

                conn.commit(); 
                return true;
            }
            return false;
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        }
    }

    public boolean returnBook(int loanId) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);

            String sqlGetBook = "SELECT book_id FROM loans WHERE id = ?";
            PreparedStatement psGet = conn.prepareStatement(sqlGetBook);
            psGet.setInt(1, loanId);
            ResultSet rs = psGet.executeQuery();

            if (rs.next()) {
                int bookId = rs.getInt("book_id");

                String sqlUpdateLoan = "UPDATE loans SET status = 'Returned' WHERE id = ?";
                PreparedStatement ps1 = conn.prepareStatement(sqlUpdateLoan);
                ps1.setInt(1, loanId);
                ps1.executeUpdate();

                String sqlUpdateBook = "UPDATE books SET stock = stock + 1 WHERE id = ?";
                PreparedStatement ps2 = conn.prepareStatement(sqlUpdateBook);
                ps2.setInt(1, bookId);
                ps2.executeUpdate();

                conn.commit();
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Loan> getLoansByUserId(int userId) {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.*, b.title FROM loans l JOIN books b ON l.book_id = b.id WHERE l.user_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Loan loan = new Loan();
                loan.setId(rs.getInt("id"));
                loan.setLoanDate(rs.getDate("loan_date"));
                loan.setDueDate(rs.getDate("due_date"));
                loan.setStatus(rs.getString("status"));
                
                Book book = new Book();
                book.setId(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                loan.setBook(book);
                
                loans.add(loan);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return loans;
    }
}
