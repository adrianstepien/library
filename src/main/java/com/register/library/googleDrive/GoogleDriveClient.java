package com.register.library.googleDrive;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

/**
 * Google drive REST client
 */
@Service
@Slf4j
public class GoogleDriveClient {

    private static NetHttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private static final String USER_IDENTIFIER_KEY = "DUMMY_USER";
    private final String callbackUri;
    //path to save multipart files
    private final String multipartTempPath;
    //load file as resource
    private final Resource credentialsFilePath;
    private final Resource keyStoreFolder;
    private GoogleAuthorizationCodeFlow codeFlow;

    public GoogleDriveClient(@Value("${google.oauth.callback.uri}") String callbackUri,
                             @Value("${google.secret.key.path}") Resource credentialsFilePath,
                             @Value("${google.credentials.folder.path}") Resource keyStoreFolder,
                             @Value("${spring.servlet.multipart.location}") String multipartTempPath) {
        this.callbackUri = callbackUri;
        this.credentialsFilePath = credentialsFilePath;
        this.keyStoreFolder = keyStoreFolder;
        this.multipartTempPath = multipartTempPath;
    }

    /**
     * init oauth2 in google drive server
     */
    @PostConstruct
    public void init() throws Exception {
        GoogleClientSecrets googleClientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(credentialsFilePath.getInputStream()));
        codeFlow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT,
                JSON_FACTORY,
                googleClientSecrets,
                SCOPES).setDataStoreFactory(new FileDataStoreFactory(keyStoreFolder.getFile())).build();
    }

    public String redirectToGoogleAuth() {
        GoogleAuthorizationCodeRequestUrl url = codeFlow.newAuthorizationUrl();
        return url.setRedirectUri(callbackUri).setAccessType("offline").build();
    }

    public void saveAuthorizationCode(HttpServletRequest request) throws IOException {
        String code = request.getParameter("code");
        if (code != null) {
            log.info("There is no code in request");
            GoogleTokenResponse response = codeFlow.newTokenRequest(code).setRedirectUri(callbackUri).execute();
            codeFlow.createAndStoreCredential(response, USER_IDENTIFIER_KEY);
        }
    }

    public boolean isAuthorized() throws IOException {
        Credential credential = codeFlow.loadCredential(USER_IDENTIFIER_KEY);
        if (credential != null) {
            credential.refreshToken();
            return true;
        }
        return false;
    }

    public String saveFile(Long fileId, MultipartFile bookToSave) throws IOException {
        Drive drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, codeFlow.loadCredential(USER_IDENTIFIER_KEY)).setApplicationName("Quickstart").build();
        File file = new File();
        file.setName(fileId + ".mobi");

        java.io.File fileToUpload = new java.io.File(multipartTempPath + bookToSave.getOriginalFilename());
        bookToSave.transferTo(fileToUpload);

        FileContent content = new FileContent("application/x-mobipocket-ebook", fileToUpload);
        File savedFile = drive.files().create(file, content).setFields("id").execute();
        return savedFile.getId();
    }

    public byte[] getFile(String fileId) throws IOException {
        Drive drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, codeFlow.loadCredential(USER_IDENTIFIER_KEY)).setApplicationName("Quickstart").build();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        drive.files().get(fileId).executeMediaAndDownloadTo(outputStream);
        return outputStream.toByteArray();
    }
}
