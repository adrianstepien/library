package com.register.library.controller;

import com.register.library.repository.model.entity.BookEntity;
import com.register.library.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/book")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<BookEntity>> findBooksByParameter(@RequestHeader(value = "findParam") String findParam) {
        return ResponseEntity.ok(bookService.findBookByGoogleApi(findParam));
    }

    @PostMapping
    public ResponseEntity<BookEntity> addBookToRegister(@RequestBody BookEntity bookEntity) {
        return ResponseEntity.ok(bookService.addBookToRegister(bookEntity));
    }

    @PutMapping
    public ResponseEntity<BookEntity> updateBookInRegister(@RequestBody BookEntity bookEntity) {
        return ResponseEntity.ok(bookService.updateBookInRegister(bookEntity));
    }

    @DeleteMapping
    public ResponseEntity deleteBookFromRegister(@RequestParam Long bookId) {
        bookService.deleteBookFromRegister(bookId);
        return ResponseEntity.ok().build();
    }
}
