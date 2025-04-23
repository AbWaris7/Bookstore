package com.khan.code.Books.controller;

import com.khan.code.Books.entity.Book;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BookController {


    ArrayList<Book> books = new ArrayList<>();

    public void initializeBooks() {
        books.add(new Book("Java","Abdul","Computer-Science"));
        books.add(new Book("Algorithm","Alex","Computer-Science"));
        books.add(new Book("Chemistry","Bob","Science"));
    }

    BookController(){
        initializeBooks();
    }

    @GetMapping("/api/books")
    public List<Book> getBooks() {
        return books;
    }

    @GetMapping("/api/books/{title}")
    public Book getBookByName(@PathVariable String title) {

        return books.stream().filter(book -> book.getTitle().equalsIgnoreCase(title)).findFirst().orElse(null);
    }


}
