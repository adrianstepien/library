package com.register.library.googleBooks;

import com.register.library.googleBooks.entity.GoogleBookList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GoogleBooksClient {

    private final String googleBooksUrl;
    private final String googleKey;

    public GoogleBooksClient(@Value("${google.books.url}") String googleBooksUrl,
                             @Value("${google.books.key}") String googleKey) {
        this.googleBooksUrl = googleBooksUrl;
        this.googleKey = googleKey;
    }

    public GoogleBookList findBooksByParameter(String parameter) {
        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(googleBooksUrl)
                .queryParam("q", parameter.replaceAll(" ", "+"))
                .queryParam("key", googleKey)
                .queryParam("maxResults", 40);

        return restTemplate.getForObject(
                builder.toUriString(),
                GoogleBookList.class
        );
    }
}
