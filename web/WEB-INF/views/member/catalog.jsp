<%@page import="java.util.List"%>
<%@page import="com.ereadly.model.Book"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/layout/header.jsp" />


<div class="container mt-4">
    <h2>Katalog Buku</h2>
    <hr>
    
    <% if (request.getAttribute("message") != null) { %>
        <div class="alert alert-info"><%= request.getAttribute("message") %></div>
    <% } %>

    <div class="row">
        <% 
            List<Book> books = (List<Book>) request.getAttribute("books");
            if (books != null && !books.isEmpty()) {
                for (Book book : books) {
        %>
        <div class="col-md-3 mb-4">
            <div class="card h-100 shadow-sm">
                <div class="card-body text-center">
                    <h5 class="card-title"><%= book.getTitle() %></h5>
                    <p class="card-text text-muted">Oleh: <%= book.getAuthor() %></p>
                    <p class="badge bg-primary"><%= book.getCategory() %></p>
                    <p class="small">Stok: <%= book.getStock() %></p>
                    
                    <% if (book.getStock() > 0) { %>
                        <form action="<%= request.getContextPath() %>/borrow" method="POST">
                            <input type="hidden" name="bookId" value="<%= book.getId() %>">
                            <button type="submit" class="btn btn-success w-100">Pinjam Buku</button>
                        </form>
                    <% } else { %>
                        <button class="btn btn-secondary w-100" disabled>Habis</button>
                    <% } %>
                </div>
            </div>
        </div>
        <% 
                }
            } else {
        %>
            <div class="col-12 text-center">
                <p>Tidak ada buku tersedia saat ini.</p>
            </div>
        <% } %>
    </div>
</div>

<jsp:include page="/WEB-INF/views/layout/footer.jsp" />