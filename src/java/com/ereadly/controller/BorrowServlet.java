package com.ereadly.controller;

import com.ereadly.dao.LoanDAO;
import com.ereadly.model.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/borrow")
public class BorrowServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int bookId = Integer.parseInt(request.getParameter("bookId"));
        LoanDAO loanDAO = new LoanDAO();

        try {
            boolean success = loanDAO.borrowBook(user.getId(), bookId);
            
            if (success) {
                request.setAttribute("message", "Buku berhasil dipinjam!");
            } else {
                request.setAttribute("message", "Gagal meminjam buku (Stok habis).");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Terjadi kesalahan sistem.");
        }

        request.getRequestDispatcher("/WEB-INF/views/member/catalog").forward(request, response);
    }
}