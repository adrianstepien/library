package com.register.library.helper;

import com.register.library.googleBooks.entity.GoogleBook;
import com.register.library.googleBooks.entity.GoogleBookInfo;
import com.register.library.googleBooks.entity.GoogleBookList;
import com.register.library.googleBooks.entity.ImageLinks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GoogleBookHelper {

	public static GoogleBookList getGoogleBookList(int amountOfBooks, String title, String subtitle, String authors, String publishedDate, String pageCount, String description, String imageLinks) {
		GoogleBookList googleBookList = new GoogleBookList();
		googleBookList.setKind("books#volumes");

		List<GoogleBook> listOfGoogleBooks = new ArrayList<>();
		googleBookList.setListOfGoogleBooks(listOfGoogleBooks);

		for (int i = 0; i < amountOfBooks; ++i) {
			GoogleBookInfo googleBookInfo = new GoogleBookInfo();
			googleBookInfo.setTitle(String.format("%s %s", title, i));
			googleBookInfo.setSubtitle(String.format("%s %s", subtitle, i));
			googleBookInfo.setAuthors(Collections.singletonList(String.format("%s %s", authors, i)));
			googleBookInfo.setPublishedDate(String.format("%s %s", publishedDate, i));
			googleBookInfo.setPageCount(String.format("%s %s", pageCount, i));
			googleBookInfo.setDescription(String.format("%s %s", description, i));
			googleBookInfo.setImageLinks(new ImageLinks(String.format("%s %s", imageLinks, i), ""));

			GoogleBook googleBook = new GoogleBook();
			googleBook.setId(String.valueOf(i));
			googleBook.setGoogleBookInfo(googleBookInfo);
			listOfGoogleBooks.add(googleBook);
		}

		return googleBookList;
	}
}
