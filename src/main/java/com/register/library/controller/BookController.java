package com.register.library.controller;

import com.register.library.dto.SearchCriteriaDto;
import com.register.library.repository.model.entity.BookEntity;
import com.register.library.services.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for book operations
 */
@RestController
@CrossOrigin(origins = "${cors.address:http://localhost}")
@RequestMapping(path = "/api/book")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookService bookService;

    /**
     * Find all books in google book API
     *
     * @param phrase parameter to search defined by user
     * @return list of books found in google book API
     */
    @GetMapping(path = "/google/findByParam/{phrase}")
    public ResponseEntity<List<BookEntity>> findBooksByParameter(@PathVariable(value = "phrase") String phrase) {
        log.info("BookController findBooksByParameter with phrase {}", phrase);
        return ResponseEntity.ok(bookService.findBookByGoogleApi(phrase));
    }

    /**
     * Find all books saved by user
     *
     * @param searchCriteriaDto criteria to search defined by user
     * @return page of user' books
     */
    @GetMapping
    public ResponseEntity<Page<BookEntity>> findBooksInRegister(SearchCriteriaDto searchCriteriaDto) {
        log.info("BookController findBooksInRegister with search criteria {}", searchCriteriaDto);
        return ResponseEntity.ok(bookService.findBooksInRegister(searchCriteriaDto));
    }

    /**
     * Find all books saved by user with title like phrase
     *
     * @param searchCriteriaDto criteria to search defined by user
     * @param phrase parameter to search defined by user
     * @return page of user' books
     */
    @GetMapping(path = "/byTitle/{phrase}")
    public ResponseEntity<Page<BookEntity>> findBooksInRegisterWithTitleLike(SearchCriteriaDto searchCriteriaDto, @PathVariable(value = "phrase") String phrase) {
        log.info("BookController findBooksInRegisterWithParameter with search criteria {} and phrase {}", searchCriteriaDto, phrase);
        return ResponseEntity.ok(bookService.findBooksInRegisterWithTitleLike(searchCriteriaDto, phrase));
    }

    /**
     * Find book saved by user
     *
     * @param id id of book
     * @return book saved by user
     */
    @GetMapping(path = "/{id}")
    public ResponseEntity<BookEntity> findBookInRegisterById(@PathVariable(value = "id") Long id) {
        log.info("BookController findBookInRegisterById with id {}", id);
        return ResponseEntity.ok(bookService.findBookInRegisterById(id));
    }

    /**
     * Check if book has a file saved on google drive
     *
     * @param bookId id of book
     * @return information if book has a file
     */
    @GetMapping(value = "/hasFile/{bookId}")
    public ResponseEntity<Boolean> hasBookFile(@PathVariable("bookId") Long bookId) {
        log.info("BookController hasBookFile with id {}", bookId);
        return ResponseEntity.ok(bookService.hasFile(bookId));
    }

    /**
     * Save book on database
     *
     * @param bookEntity entity defined by user
     * @return saved book
     */
    @PostMapping
    public ResponseEntity<BookEntity> addBookToRegister(@RequestBody BookEntity bookEntity) {
        log.info("BookController addBookToRegister with bookEntity {}", bookEntity.toString());
        return ResponseEntity.ok(bookService.addBookToRegister(bookEntity));
    }

    /**
     * Edit book on database
     *
     * @param bookEntity entity defined by user
     * @return edited book
     */
    @PutMapping
    public ResponseEntity<BookEntity> updateBookInRegister(@RequestBody BookEntity bookEntity) {
        log.info("BookController updateBookInRegister with bookEntity {}", bookEntity.toString());
        return ResponseEntity.ok(bookService.updateBookInRegister(bookEntity));
    }

    /**
     * Delete book from database
     *
     * @param bookId id of book
     */
    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBookFromRegister(@PathVariable("bookId") Long bookId) {
        log.info("BookController deleteBookFromRegister with bookId {}", bookId);
        bookService.deleteBookFromRegister(bookId);
        return ResponseEntity.ok().build();
    }
}
