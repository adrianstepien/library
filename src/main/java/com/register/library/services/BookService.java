package com.register.library.services;

import com.register.library.googleBooks.GoogleBooksClient;
import com.register.library.googleBooks.entity.GoogleBook;
import com.register.library.googleBooks.entity.GoogleBookList;
import com.register.library.mapper.BookMapper;
import com.register.library.repository.model.entity.BookEntity;
import com.register.library.repository.model.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for book operations
 */
@Service
@RequiredArgsConstructor
public class BookService {

    private final GoogleBooksClient googleBooksClient;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    /**
     * Find books in google book API by declared parameter and mapped to transport model
     *
     * @param inputParameter parameter to search defined by user
     * @return list of books found in google book API
     */
    public List<BookEntity> findBookByGoogleApi(String inputParameter) {
        GoogleBookList googleApiResult = googleBooksClient.findBooksByParameter(inputParameter);
        if (googleApiResult.getListOfGoogleBooks() != null) {
            return parseToBookEntityList(googleApiResult);
        } else {
            return null;
        }
    }

    /**
     * Find all books saved by user
     *
     * @return list of user' books
     */
    public List<BookEntity> findBooksInRegister() {
        return bookRepository.findAll();
    }

    /**
     * Find book saved by user
     *
     * @param id id of book
     * @return book saved by user
     */
    public BookEntity findBookInRegisterById(Long id) {
        return bookRepository.findById(id)
                .orElse(null);
    }

    /**
     * Check if book has a file saved on google drive
     *
     * @param bookId id of book
     * @return information if book has a file
     */
    public boolean hasFile(Long bookId) {
        return bookRepository.findById(bookId)
                .map(bookEntity -> bookEntity.getFileId() != null)
                .orElse(false);
    }

    /**
     * Save book on database
     *
     * @param bookEntity entity defined by user
     * @return saved book
     */
    public BookEntity addBookToRegister(BookEntity bookEntity) {
        return bookRepository.save(bookEntity);
    }

    /**
     * Edit book on database
     *
     * @param bookEntity entity defined by user
     * @return edited book
     */
    public BookEntity updateBookInRegister(BookEntity bookEntity) {
        Optional<BookEntity> bookEntityOptional = bookRepository.findById(bookEntity.getId());
        if (!bookEntityOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No book found");
        }
        return bookRepository.save(bookEntity);
    }

    /**
     * Delete book from database
     *
     * @param bookId id of book
     */
    public void deleteBookFromRegister(Long bookId) {
        Optional<BookEntity> bookEntityOptional = bookRepository.findById(bookId);
        if (!bookEntityOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No book found");
        }
        bookRepository.deleteById(bookId);
    }

    private List<BookEntity> parseToBookEntityList(GoogleBookList googleApiResult) {
        return googleApiResult.getListOfGoogleBooks().stream()
                        .map(GoogleBook::getGoogleBookInfo)
                        .map(bookMapper::convertGoogleBookInfoToBookEntity)
                        .collect(Collectors.toList());
    }
}
