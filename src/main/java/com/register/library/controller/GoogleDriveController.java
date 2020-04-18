package com.register.library.controller;

import com.register.library.services.GoogleDriveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class GoogleDriveController {

    private GoogleDriveService googleDriveService;

    @Autowired
    public GoogleDriveController(GoogleDriveService googleDriveService) {
        this.googleDriveService = googleDriveService;
    }

    @GetMapping(value = "googleDrive/signIn")
    public void signIn(HttpServletResponse response) throws IOException {
        response.sendRedirect(googleDriveService.redirectToGoogleAuth());
    }

    @PostMapping(value = "saveFile/{bookId}")
    public void saveFile(HttpServletResponse response, @PathVariable("bookId") Long bookId, @RequestParam("bookFile") MultipartFile bookToSave) throws IOException {
        googleDriveService.saveFile(response, bookId, bookToSave);
    }

    @GetMapping(value = "getFile")
    public @ResponseBody byte[] getFile(HttpServletResponse response, @RequestParam("bookId") Long bookId) throws IOException {
        return googleDriveService.getFile(response, bookId);
    }

    @GetMapping(value = "oauth2/callback")
    public void logInAndSaveAuthorizationCode(HttpServletRequest httpServletRequest) throws Exception {
        googleDriveService.saveAuthorizationCode(httpServletRequest);
    }
}
