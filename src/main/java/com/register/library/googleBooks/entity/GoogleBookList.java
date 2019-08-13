package com.register.library.googleBooks.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class GoogleBookList implements Serializable {
    @JsonProperty("kind")
    private String kind;
    @JsonProperty("items")
    private List<GoogleBook> listOfGoogleBooks;
}
