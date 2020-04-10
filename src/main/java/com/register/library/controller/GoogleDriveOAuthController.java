package com.register.library.controller;

import com.register.library.googleDrive.GoogleDriveClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping(value = "googleDrive/signIn")
    public void signIn(HttpServletResponse response) throws IOException {
        response.sendRedirect(googleDriveClient.redirectToGoogleAuth());
    }

    @PostMapping(value = "saveFile/{fileId}")
    public void saveFile(HttpServletResponse response, @PathVariable("fileId") String fileId, @RequestParam("bookFile") MultipartFile bookToSave) throws IOException {
        if (googleDriveClient.isAuthorized()) {
            googleDriveClient.saveFile(fileId, bookToSave);
        } else {
            response.sendRedirect(googleDriveClient.redirectToGoogleAuth());
        }
    }

    @GetMapping(value = "oauth2/callback")
    public void logInAndSaveAuthorizationCode(HttpServletRequest httpServletRequest) throws Exception {
        googleDriveClient.saveAuthorizationCode(httpServletRequest);
    }
}
