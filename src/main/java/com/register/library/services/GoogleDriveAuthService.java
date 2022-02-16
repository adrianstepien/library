package com.register.library.services;

import com.register.library.googleDrive.GoogleDriveClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Service for auth operations on google drive
 */
@Service
@RequiredArgsConstructor
public class GoogleDriveAuthService {

	private final GoogleDriveClient googleDriveClient;

	/**
	 * Authenticate user on google drive
	 *
	 * @param httpServletRequest request object
	 * @throws IOException exception during authentication
	 */
	public void saveAuthorizationCode(HttpServletRequest httpServletRequest) throws IOException {
		googleDriveClient.saveAuthorizationCode(httpServletRequest);
	}

	/**
	 * Redirect user to google drive authorization page
	 */
	public String redirectToGoogleAuth() {
		return googleDriveClient.redirectToGoogleAuth();
	}
}
