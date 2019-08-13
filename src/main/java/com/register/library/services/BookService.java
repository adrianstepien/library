package com.register.library.services;

import com.register.library.googleBooks.GoogleBooksClient;
import com.register.library.googleBooks.entity.GoogleBookList;
import com.register.library.repository.model.entity.BookEntity;
import com.register.library.repository.model.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {
    private GoogleBooksClient googleBooksClient;
    private BookRepository bookRepository;

    @Autowired
    public BookService(GoogleBooksClient googleBooksClient, BookRepository bookRepository) {
        this.googleBooksClient = googleBooksClient;
        this.bookRepository = bookRepository;
    }

    public List<BookEntity> findBookByGoogleApi(String findParameter) {
        GoogleBookList googleApiResult = googleBooksClient.findBooksByParameter(findParameter);
        return parseToBookEntityList(googleApiResult);
    }

    public BookEntity addBookToRegister(BookEntity bookEntity) {
        return bookRepository.save(bookEntity);
    }

    public BookEntity updateBookInRegister(BookEntity bookEntity) {
        return bookRepository.save(bookEntity);
    }

    public void deleteBookFromRegister(Long bookId) {
        bookRepository.deleteById(bookId);
    }

    public void createNewBookInLibrary(BookEntity newBookEntity) {
        //skorzystac z metody wysylajacej stworzenie ksiazki w biblioteczce
    }

    private List<BookEntity> parseToBookEntityList(GoogleBookList googleApiResult) {
        List<BookEntity> listOfBookEntity = new ArrayList<>();
        googleApiResult.getListOfGoogleBooks().forEach(
                googleBook -> listOfBookEntity.add(new BookEntity(
                        null,
                        googleBook.getGoogleBookInfo() != null ? googleBook.getGoogleBookInfo().getTitle() : null,
                        googleBook.getGoogleBookInfo() != null && googleBook.getGoogleBookInfo().getAuthors() != null ? googleBook.getGoogleBookInfo().getAuthors().toString() : null,
                        googleBook.getGoogleBookInfo() != null ? googleBook.getGoogleBookInfo().getPublishedDate() : null,
                        googleBook.getGoogleBookInfo() != null ? googleBook.getGoogleBookInfo().getPageCount() : null,
                        /*googleBook.getGoogleBookInfo() != null ? googleBook.getGoogleBookInfo().getDescription() : null*/null,
                        googleBook.getGoogleBookInfo() != null && googleBook.getGoogleBookInfo().getImageLinks() != null ? googleBook.getGoogleBookInfo().getImageLinks().getSmallThumbnail() : null,
                        null,
                        null,
                        null
                ))
        );
        return listOfBookEntity;
    }
}
