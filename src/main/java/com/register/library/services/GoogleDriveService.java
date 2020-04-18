package com.register.library.services;

import com.register.library.googleDrive.GoogleDriveClient;
import com.register.library.repository.model.entity.BookEntity;
import com.register.library.repository.model.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class GoogleDriveService {
    private GoogleDriveClient googleDriveClient;
    private BookRepository bookRepository;

    @Autowired
    public GoogleDriveService(GoogleDriveClient googleDriveClient, BookRepository bookRepository) {
        this.googleDriveClient = googleDriveClient;
        this.bookRepository = bookRepository;
    }

    public void saveAuthorizationCode(HttpServletRequest httpServletRequest) throws IOException {
        googleDriveClient.saveAuthorizationCode(httpServletRequest);
    }

    /**
     * get a file from google drive
     * check if user is logged in google drive
     * yes - check if this record in db has assigned book in google drive
     * yes - get a file
     * no - throw exception
     */
    public byte[] getFile(HttpServletResponse response, Long bookId) throws IOException {
        if (isAuthorized()) {
            if (bookRepository.existsById(bookId)) {
                BookEntity bookEntity = bookRepository.getOne(bookId);
                googleDriveClient.getFile(bookEntity.getFileId());//TODO tmp
                if  (bookEntity.getFileId() != null) {
                    return googleDriveClient.getFile(bookEntity.getFileId());
                } else {
                    //TODO: rzucic blad o braku zdefiniowanego pliku dla danej ksiazki
                }
            } else {
                //TODO: rzucic blad o braku takiej ksiazki w bazie
            }
        } else {
            response.sendRedirect(googleDriveClient.redirectToGoogleAuth());
        }
        //TODO: zmienic zwracana wartosc
        return null;
    }

    /**
     * Save a file in google drive
     * check if user is logged in google drive
     * yes - check if this record in db has assigned book in google drive
     * yes - delete the previous file and save the new one
     * no - save the new file
     */
    public void saveFile(HttpServletResponse response, Long bookId, MultipartFile bookToSave) throws IOException {
        if (isAuthorized()) {
            if (bookRepository.existsById(bookId)) {
                BookEntity book = bookRepository.getOne(bookId);
                if (book.getFileId() == null) {
                    String fileId = googleDriveClient.saveFile(bookId, bookToSave);
                    book.setFileId(fileId);
                    bookRepository.save(book);
                } else {
                    //TODO: usunac poprzedni plik w googleDrive i dodac kolejny
                }
            } else {
                //TODO: dodac wyjatek o braku takiej ksiazki w bazie danych
            }
        } else {
            response.sendRedirect(googleDriveClient.redirectToGoogleAuth());
        }
    }

    public String redirectToGoogleAuth() {
        return googleDriveClient.redirectToGoogleAuth();
    }

    private boolean isAuthorized() throws IOException {
        return googleDriveClient.isAuthorized();
    }
}
