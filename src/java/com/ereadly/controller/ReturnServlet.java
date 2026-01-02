package com.ereadly.controller;

import com.ereadly.dao.LoanDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/return")
public class ReturnServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        int loanId = Integer.parseInt(request.getParameter("loanId"));
        LoanDAO loanDAO = new LoanDAO();

        try {
            boolean success = loanDAO.returnBook(loanId);
            
            if (success) {
                response.sendRedirect("my-loans?message=Buku dikembalikan!");
            } else {
                response.sendRedirect("my-loans?message=Gagal memproses pengembalian.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("my-loans?message=Error!");
        }
    }
}