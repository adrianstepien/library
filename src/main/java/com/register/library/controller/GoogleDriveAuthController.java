package com.register.library.controller;

import com.register.library.services.GoogleDriveAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * REST controller auth operations on google drive
 */
@RestController
@CrossOrigin(origins = "${cors.address}")
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/api/google/drive/oauth2")
public class GoogleDriveAuthController {

	private final GoogleDriveAuthService googleDriveAuthService;

	/**
	 * Sing in new user on google drive
	 *
	 * @param response object to redirect unauthorized user
	 * @throws IOException error during authorization
	 */
	@GetMapping(value = "/signIn")
	public void signIn(HttpServletResponse response) throws IOException {
		log.info("GoogleDriveAuthController signIn");
		response.sendRedirect(googleDriveAuthService.redirectToGoogleAuth());
	}

	/**
	 * Authenticate user on google drive
	 *
	 * @param httpServletRequest request object
	 * @throws IOException exception during authentication
	 */
	@GetMapping(value = "/callback")
	public void logInAndSaveAuthorizationCode(HttpServletRequest httpServletRequest) throws IOException {
		log.info("GoogleDriveAuthController logInAndSaveAuthorizationCode");
		googleDriveAuthService.saveAuthorizationCode(httpServletRequest);
	}
}

