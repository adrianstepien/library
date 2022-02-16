package com.register.library.controller;

import com.register.library.services.GoogleDriveAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
public class GoogleDriveAuthController {

	private final GoogleDriveAuthService googleDriveAuthService;

	/**
	 * Sing in new user on google drive
	 *
	 * @param response object to redirect unauthorized user
	 * @throws IOException error during authorization
	 */
	@GetMapping(value = "/googleDrive/signIn")
	public void signIn(HttpServletResponse response) throws IOException {
		response.sendRedirect(googleDriveAuthService.redirectToGoogleAuth());
	}

	/**
	 * Authenticate user on google drive
	 *
	 * @param httpServletRequest request object
	 * @throws IOException exception during authentication
	 */
	@GetMapping(value = "/oauth2/callback")
	public void logInAndSaveAuthorizationCode(HttpServletRequest httpServletRequest) throws IOException {
		googleDriveAuthService.saveAuthorizationCode(httpServletRequest);
	}
}
