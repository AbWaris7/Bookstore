package com.khan.code.Books.controller;

import com.khan.code.Books.entity.Book;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {


    ArrayList<Book> books = new ArrayList<>();

    public void initializeBooks() {
        books.add(new Book("Java","Abdul","Computer-Science"));
        books.add(new Book("Algorithm","Alex","Computer-Science"));
        books.add(new Book("Chemistry","Bob","Science"));
        books.add(new Book("Algebra","Bob","math"));
    }

    BookController(){
        initializeBooks();
    }

    @GetMapping
    public List<Book> getBooks(@RequestParam(required = false) String category) {

        if(category == null) {
            return books;
        }

        return books.stream().filter(book -> book.getCategory().equalsIgnoreCase(category))
                .toList();

    }

    @GetMapping("/{title}")
    public Book getBookByName(@PathVariable String title) {

        return books.stream().filter(book -> book.getTitle().equalsIgnoreCase(title)).findFirst().orElse(null);
    }

    @PostMapping
    public void createBook(@RequestBody Book newBook) {

        boolean isNewBook = books.stream().noneMatch(book -> book.getTitle().equalsIgnoreCase(newBook.getTitle()));

        if(isNewBook) {
            books.add(newBook);
        }

    }

    @PutMapping("/{title}")
    public void updateBook(@PathVariable String title, @RequestBody Book updatedBook) {

        for(int i =0; i<books.size(); i++) {
            if(books.get(i).getTitle().equalsIgnoreCase(title)) {
                books.set(i, updatedBook);
                return;
            }
        }
    }

    @DeleteMapping("/api/books/{title}")
    public void deleteBook(@PathVariable String title) {
        books.removeIf(book -> book.getTitle().equalsIgnoreCase(title));
    }

}
