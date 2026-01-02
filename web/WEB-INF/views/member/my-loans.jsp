<%@page import="java.util.List"%>
<%@page import="com.ereadly.model.Loan"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/layout/header.jsp" />

<div class="container mt-4">
    <h2>Peminjaman Saya</h2>
    <hr>

    <table class="table table-hover table-striped shadow-sm mt-3">
        <thead class="table-dark">
            <tr>
                <th>Judul Buku</th>
                <th>Tanggal Pinjam</th>
                <th>Batas Kembali</th>
                <th>Status</th>
                <th>Aksi</th>
            </tr>
        </thead>
        <tbody>
            <% 
                List<Loan> loans = (List<Loan>) request.getAttribute("loans");
                if (loans != null && !loans.isEmpty()) {
                    for (Loan loan : loans) {
            %>
            <tr>
                <td><%= loan.getBook().getTitle() %></td>
                <td><%= loan.getLoanDate() %></td>
                <td><%= loan.getDueDate() %></td>
                <td>
                    <span class="badge <%= loan.getStatus().equals("Returned") ? "bg-success" : "bg-warning text-dark" %>">
                        <%= loan.getStatus() %>
                    </span>
                </td>
                <td>
                    <% if (loan.getStatus().equals("Borrowed")) { %>
                        <form action="<%= request.getContextPath() %>/return" method="POST" style="display:inline;">
                            <input type="hidden" name="loanId" value="<%= loan.getId() %>">
                            <button type="submit" class="btn btn-sm btn-primary">Kembalikan</button>
                        </form>
                    <% } else { %>
                        <a href="rating?bookId=<%= loan.getBook().getId() %>" class="btn btn-sm btn-outline-info">Beri Rating</a>
                    <% } %>
                </td>
            </tr>
            <% 
                    }
                } else {
            %>
            <tr>
                <td colspan="5" class="text-center">Anda belum meminjam buku apa pun.</td>
            </tr>
            <% } %>
        </tbody>
    </table>
</div>

<jsp:include page="/WEB-INF/views/layout/footer.jsp" />