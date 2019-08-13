package com.register.library.googleBooks.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class GoogleBook implements Serializable {
    @JsonProperty("id")
    private String id;
    @JsonProperty("volumeInfo")
    private GoogleBookInfo googleBookInfo;
}
