package com.register.library.helper;

import com.register.library.repository.model.entity.BookEntity;

import java.time.LocalDate;

public class BookEntityHelper {

	public static BookEntity getBookEntity(String title, String authors, String publishedDate, String pageCount,
										   String description, String imageLinks, String ownReview, Integer ownRating,
										   String fileId, LocalDate dateOfReading) {
		BookEntity bookEntity = new BookEntity();

		bookEntity.setTitle(title);
		bookEntity.setAuthors(authors);
		bookEntity.setPublishedDate(publishedDate);
		bookEntity.setPageCount(pageCount);
		bookEntity.setDescription(description);
		bookEntity.setImageLink(imageLinks);
		bookEntity.setOwnReview(ownReview);
		bookEntity.setOwnRating(ownRating);
		bookEntity.setFileId(fileId);
		bookEntity.setDateOfReading(dateOfReading);

		return bookEntity;
	}
}
