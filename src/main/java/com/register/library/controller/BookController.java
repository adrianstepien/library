package com.register.library.controller;

import com.register.library.repository.model.entity.BookEntity;
import com.register.library.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "${cors.address}")
@RequestMapping(path = "/api/book")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(path = "/findByParam/{phrase}")
    public ResponseEntity<List<BookEntity>> findBooksByParameter(@PathVariable(value = "phrase") String phrase) {
        return ResponseEntity.ok(bookService.findBookByGoogleApi(phrase));
    }

    @GetMapping
    public ResponseEntity<List<BookEntity>> findBooksInRegister() {
        return ResponseEntity.ok(bookService.findBooksInRegister());
    }

    @PostMapping
    public ResponseEntity<BookEntity> addBookToRegister(@RequestBody BookEntity bookEntity) {
        return ResponseEntity.ok(bookService.addBookToRegister(bookEntity));
    }

    @PutMapping
    public ResponseEntity<BookEntity> updateBookInRegister(@RequestBody BookEntity bookEntity) {
        return ResponseEntity.ok(bookService.updateBookInRegister(bookEntity));
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity deleteBookFromRegister(@PathVariable("bookId") Long bookId) {
        bookService.deleteBookFromRegister(bookId);
        return ResponseEntity.ok().build();
    }
}
