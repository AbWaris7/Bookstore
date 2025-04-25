package com.khan.code.Books.controller;

import com.khan.code.Books.entity.Book;
import com.khan.code.Books.request.BookRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {


    ArrayList<Book> books = new ArrayList<>();

    public void initializeBooks() {
        books.add(new Book(1,"Java","Abdul","Computer-Science",3));
        books.add(new Book(2,"Algorithm","Alex","Computer-Science",4));
        books.add(new Book(3,"Chemistry","Bob","Science",5));
        books.add(new Book(4,"Algebra","Bob","math",3));
    }

    BookController(){
        initializeBooks();
    }

    @GetMapping("/{id}")
    public Book getBook(@PathVariable @Min(value = 1) long id) {


        return books.stream()
                .filter(book -> book.getId() ==id)
                .findFirst()
                .orElse(null);
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
    public void createBook(@Valid @RequestBody BookRequest bookRequest) {

        long id = books.isEmpty() ? 1 : books.get(books.size()-1).getId() + 1;

        Book book = convertToBook(id,bookRequest);
        books.add(book);

    }

    @PutMapping("/{id}")
    public void updateBook(@PathVariable @Min(value = 1) long id, @Valid @RequestBody BookRequest bookRequest) {

        for(int i =0; i<books.size(); i++) {
            if(books.get(i).getId() == id) {
                Book updatedBook = convertToBook(id,bookRequest);
                books.set(i, updatedBook);
                return;
            }
        }
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable @Min(value = 1) long id) {
        books.removeIf(book -> book.getId() == id);
    }

    private Book convertToBook(long id, BookRequest bookRequest) {
        return new Book(id, bookRequest.getTitle(), bookRequest.getAuthor(), bookRequest.getCategory(), bookRequest.getRating());
    }

}
