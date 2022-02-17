package com.register.library.controller;

import com.register.library.services.GoogleDriveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * REST controller for book operations on google drive
 */
@RestController
@CrossOrigin(origins = "${cors.address}")
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/api/google/drive")
public class GoogleDriveController {

	private final GoogleDriveService googleDriveService;

	/**
	 * Get a file from google drive of authorized user
	 *
	 * @param response object to redirect unauthorized user
	 * @param bookId   id of book
	 * @throws IOException exception during getting a file from goolge drive
	 */
	@GetMapping(value = "/getFile/{bookId}")
	public byte[] getFile(HttpServletResponse response, @PathVariable("bookId") String bookId) throws IOException {
		log.info("GoogleDriveController getFile with bookId {}", bookId);
		return googleDriveService.getFile(response, Long.parseLong(bookId));
	}

	/**
	 * Save a file on google drive of authorized user
	 *
	 * @param response   object to redirect unauthorized user
	 * @param bookId     id of book
	 * @param bookToSave file to save
	 * @throws IOException exception during saving a file on goolge drive
	 */
	@PostMapping(value = "/saveFile/{bookId}")
	public void saveFile(HttpServletResponse response, @PathVariable("bookId") Long bookId, @RequestParam("bookFile") MultipartFile bookToSave) throws IOException {
		log.info("GoogleDriveController saveFile with bookId {}", bookId);
		googleDriveService.saveFile(response, bookId, bookToSave);
	}
}
