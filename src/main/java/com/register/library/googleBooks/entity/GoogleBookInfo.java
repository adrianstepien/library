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
public class GoogleBookInfo implements Serializable {
    @JsonProperty("title")
    private String title;
    @JsonProperty("subtitle")
    private String subtitle;
    @JsonProperty("authors")
    private List<String> authors;
    @JsonProperty("publishedDate")
    private String publishedDate;
    @JsonProperty("pageCount")
    private String pageCount;
    @JsonProperty("description")
    private String description;
    @JsonProperty("imageLinks")
    private ImageLinks imageLinks;
}

