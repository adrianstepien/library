package com.register.library.services;

import com.register.library.googleDrive.GoogleDriveClient;
import com.register.library.repository.model.entity.BookEntity;
import com.register.library.repository.model.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Service for book operations on google drive
 */
@Service
@RequiredArgsConstructor
public class GoogleDriveService {

	private final GoogleDriveClient googleDriveClient;
	private final BookRepository bookRepository;

	/**
	 * Get a file from google drive of authorized user
	 *
	 * @param response object to redirect unauthorized user
	 * @param bookId   id of book
	 * @throws IOException exception during getting a file from goolge drive
	 */
	public byte[] getFile(HttpServletResponse response, Long bookId) throws IOException {
		if (googleDriveClient.isAuthorized()) {
            Optional<BookEntity> bookEntityOptional = bookRepository.findById(bookId);
            if (bookEntityOptional.isPresent()) {
                BookEntity book = bookEntityOptional.get();
                if (book.getFileId() != null) {
                    return googleDriveClient.getFile(book.getFileId());
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
	 * Save a file on google drive of authorized user
	 *
	 * @param response   object to redirect unauthorized user
	 * @param bookId     id of book
	 * @param bookToSave file to save
	 * @throws IOException exception during saving a file on goolge drive
	 */
	public void saveFile(HttpServletResponse response, Long bookId, MultipartFile bookToSave) throws IOException {
		if (googleDriveClient.isAuthorized()) {
            Optional<BookEntity> bookEntityOptional = bookRepository.findById(bookId);
			if (bookEntityOptional.isPresent()) {
                BookEntity book = bookEntityOptional.get();
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
}
