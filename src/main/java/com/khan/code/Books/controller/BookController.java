package com.khan.code.Books.controller;

import com.khan.code.Books.entity.Book;
import com.khan.code.Books.exception.BookErrorResponse;
import com.khan.code.Books.exception.BookNotFoundException;
import com.khan.code.Books.request.BookRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Books Rest API Endpoints", description = "Operation related to Book")
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


    @Operation(summary = "Get a book by Id", description = "Retrieve a specific book by Id")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/id/{id}")
    public Book getBookById(@Parameter(description = "Id of book to be retrieved")
                            @PathVariable @Min(value = 1) long id) {
        return books.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException("Book not found - " + id));
    }


    @Operation(summary = "Get all books", description = "Retrieve a list of all available books")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Book> getBooks(@Parameter(description = "Optional query parameter ")  @RequestParam(required = false) String category) {

        if(category == null) {
            return books;
        }

        return books.stream().filter(book -> book.getCategory().equalsIgnoreCase(category))
                .toList();

    }



    @Operation(summary = "Get a book by title", description = "Retrieve a specific book by title")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{title}")
    public Book getBookByName(@PathVariable String title) {

        return books.stream().filter(book -> book.getTitle().equalsIgnoreCase(title)).findFirst().orElse(null);
    }



    @Operation(summary = "Create book", description = "create book")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createBook(@Valid @RequestBody BookRequest bookRequest) {

        long id = books.isEmpty() ? 1 : books.get(books.size()-1).getId() + 1;

        Book book = convertToBook(id,bookRequest);
        books.add(book);

    }


    @Operation(summary = "Update a book", description = "Update the details of an existing book")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateBook(@Parameter(description = "Id of the book to update") @PathVariable @Min(value = 1) long id, @Valid @RequestBody BookRequest bookRequest) {

        for(int i =0; i<books.size(); i++) {
            if(books.get(i).getId() == id) {
                Book updatedBook = convertToBook(id,bookRequest);
                books.set(i, updatedBook);
                return;
            }
        }
    }


    @Operation(summary = "Delete book by Id", description = "Remove a book from the book list")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteBook(@Parameter(description = "Id of the book to delete") @PathVariable @Min(value = 1) long id) {
        books.removeIf(book -> book.getId() == id);
    }

    private Book convertToBook(long id, BookRequest bookRequest) {
        return new Book(id, bookRequest.getTitle(), bookRequest.getAuthor(), bookRequest.getCategory(), bookRequest.getRating());
    }

    @ExceptionHandler
    public ResponseEntity<BookErrorResponse> handleException(BookNotFoundException exec) {
        BookErrorResponse bookErrorResponse = new BookErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                exec.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(bookErrorResponse, HttpStatus.NOT_FOUND);
    }

}
