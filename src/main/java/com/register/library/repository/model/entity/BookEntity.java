package com.register.library.repository.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "book")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;
    private String title;
    private String authors;
    @Column(name = "published_date")
    private String publishedDate;
    @Column(name = "page_count")
    private String pageCount;
    private String description;
    @Column(name = "image_link")
    private String imageLink;
    @Column(name = "own_review")
    private String ownReview;
    @Column(name = "own_rating")
    private Integer ownRating;
    @Column(name = "file_id")
    private String fileId;
    @Column(name = "date_of_reading")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dateOfReading;
}
