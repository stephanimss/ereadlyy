/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ereadly.dao;
import model.Book;
import exception.BookNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
/**
 *
 * @author Bening Pastika
 */
public class BookDAO {
    private List<Book> bookList = new ArrayList<>();
    
    public void addBook(Book book) {
        bookList.add(book);
    }
   public boolean removeBook(int id) throws BookNotFoundException {
        // cek satu per satu buku di list
        for (Book book : bookList) {
            if (book.getBookId() == id) {
                bookList.remove(book);
                return true;   // berhasil dihapus
            }
        }
        throw new BookNotFoundException(
                "Book with ID " + id + " not found"
        );
    }
    public Book findById(int id) {
        for (Book book : bookList) {
            if (book.getBookId() == id) {
                return book;
            }
        }
        return null; // kalau tidak ada
    }
      public List<Book> searchBooks(String keyword) {

        List<Book> result = new ArrayList<>();
        String key = keyword.toLowerCase();

        for (Book book : bookList) {

            String title = book.getTitle().toLowerCase();
            String author = book.getAuthor().toLowerCase();
            String publisher = book.getPublisher().toLowerCase();

            if (title.contains(key) ||
                author.contains(key) ||
                publisher.contains(key)) {

                result.add(book);
            }
        }

        return result;
    }
     public List<Book> filterByGenre(String genre) {

        List<Book> result = new ArrayList<>();
        String g = genre.toLowerCase();

        for (Book book : bookList) {
            if (book.getGenre().toLowerCase().equals(g)) {
                result.add(book);
            }
        }

        return result;
    }
    public List<Book> getAllBooks() {
        return bookList;
    }
}
