package com.register.library.controller;

import com.register.library.googleBooks.GoogleBooksClient;
import com.register.library.googleDrive.GoogleDriveClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class GoogleDriveOAuthController {

    private GoogleDriveClient googleDriveClient;

    @Autowired
    public GoogleDriveOAuthController(GoogleDriveClient googleDriveClient) {
        this.googleDriveClient = googleDriveClient;
    }

    /*@GetMapping(value = "googleDrive/signIn")
    public void signIn(HttpServletResponse response) throws IOException {
        response.sendRedirect(googleDriveClient.redirectToGoogleAuth());
    }*/

    @GetMapping(value = "saveFile")
    public void saveFile(HttpServletResponse response) throws IOException {
        if (googleDriveClient.isAuthorized()) {
            googleDriveClient.saveFile();
        } else {
            response.sendRedirect(googleDriveClient.redirectToGoogleAuth());
        }
    }

    @GetMapping(value = "oauth2/callback")
    public void logInAndSaveAuthorizationCode(HttpServletRequest httpServletRequest) throws Exception {
        googleDriveClient.saveAuthorizationCode(httpServletRequest);
    }
}
