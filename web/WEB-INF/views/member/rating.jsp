<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/layout/header.jsp" />

<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-md-6 border p-4 rounded shadow-sm bg-white">
            <h3 class="text-center mb-3">Beri Ulasan Buku</h3>
            <hr>
            <form action="<%= request.getContextPath() %>/rating" method="POST">
                <input type="hidden" name="bookId" value="<%= request.getParameter("bookId") %>">
                
                <div class="mb-3">
                    <label class="form-label">Rating (1-5 Bintang):</label>
                    <select name="score" class="form-select" required>
                        <option value="5">⭐⭐⭐⭐⭐ (Sangat Bagus)</option>
                        <option value="4">⭐⭐⭐⭐ (Bagus)</option>
                        <option value="3">⭐⭐⭐ (Biasa Saja)</option>
                        <option value="2">⭐⭐ (Kurang)</option>
                        <option value="1">⭐ (Buruk)</option>
                    </select>
                </div>

                <div class="mb-3">
                    <label class="form-label">Komentar:</label>
                    <textarea name="comment" class="form-control" rows="3" placeholder="Apa pendapatmu tentang buku ini?"></textarea>
                </div>

                <div class="d-grid gap-2">
                    <button type="submit" class="btn btn-success text-white">Kirim Rating</button>
                    <a href="my-loans" class="btn btn-light border text-dark text-center">Batal</a>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/layout/footer.jsp" />