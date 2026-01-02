package com.ereadly.controller;

import com.ereadly.dao.BookDAO;
import com.ereadly.dao.LoanDAO;
import com.ereadly.model.Book;
import com.ereadly.model.Loan;
import com.ereadly.model.User;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet({"/catalog", "/my-loans"})
public class MemberServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String path = request.getServletPath();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/views/auth/login.jsp");
            return;
        }

        if (path.equals("/catalog")) {
            BookDAO bookDAO = new BookDAO();
            List<Book> books = bookDAO.getAllBooks();
            request.setAttribute("books", books);
            request.getRequestDispatcher("/views/member/catalog.jsp").forward(request, response);
            
        } else if (path.equals("/my-loans")) {
            LoanDAO loanDAO = new LoanDAO();
            List<Loan> userLoans = loanDAO.getLoansByUserId(user.getId());
            request.setAttribute("loans", userLoans);
            request.getRequestDispatcher("/WEB-INF/views/member/my-loans.jsp").forward(request, response);
        }
    }
}