package com.register.library.services;

import com.register.library.config.LiquibaseTestContext;
import com.register.library.dto.SearchCriteriaDto;
import com.register.library.googleBooks.GoogleBooksClient;
import com.register.library.googleBooks.entity.GoogleBookList;
import com.register.library.helper.BookEntityHelper;
import com.register.library.helper.GoogleBookHelper;
import com.register.library.repository.model.entity.BookEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@ActiveProfiles("test-db")
@SpringBootTest(classes = LiquibaseTestContext.class)
@TestPropertySource(properties = "liquibase.change-log=classpath:db/changelog/data/book-service.xml")
class BookServiceTest {

	private static final Long FAKE_ID = 0L;
	private static final Long BOOK_WITHOUT_FILE_ID = 1L;
	private static final Long BOOK_WITH_FILE_ID = 2L;
	private static final Long BOOK_TO_DELETE_ID = 3L;
	private static final String INPUT_PARAM = "Test Search Title";
	private static final String TITLE = "Test Title";
	private static final String SUBTITLE = "Test Subtitle";
	private static final String AUTHORS = "Test Author";
	private static final String PUBLISHED_DATE = "2003";
	private static final String PAGE_COUNT = "432";
	private static final String OWN_REVIEW = "Own review";
	private static final Integer OWN_RATING = 3;
	private static final String DESCRIPTION = "Test Description";
	private static final String IMAGE_LINKS = "Test Image Links";
	private static final String FILE_ID = "Test File ID";
	private static final LocalDate DATE_OF_READING = LocalDate.of(2022, 11, 13);

	@Autowired
	private BookService bookService;

	@MockBean
	private GoogleBooksClient googleBooksClient;

	@BeforeEach
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void findBooksByGoogleApiTest() {
		// given
		GoogleBookList googleApiResult = GoogleBookHelper.getGoogleBookList(10, TITLE, SUBTITLE, AUTHORS, PUBLISHED_DATE, PAGE_COUNT, DESCRIPTION, IMAGE_LINKS);
		Mockito.when(googleBooksClient.findBooksByParameter(Mockito.eq(INPUT_PARAM))).thenReturn(googleApiResult);

		// when
		List<BookEntity> output = bookService.findBookByGoogleApi(INPUT_PARAM);

		// then
		Assertions.assertEquals(10, output.size());
		BookEntity firstBook = output.get(0);
		Assertions.assertEquals(String.format("%s %s", TITLE, 0), firstBook.getTitle());
		Assertions.assertEquals(String.format("[%s %s]", AUTHORS, 0), firstBook.getAuthors());
		Assertions.assertEquals(String.format("%s %s", PUBLISHED_DATE, 0), firstBook.getPublishedDate());
		Assertions.assertEquals(String.format("%s %s", PAGE_COUNT, 0), firstBook.getPageCount());
		Assertions.assertEquals(String.format("%s %s", DESCRIPTION, 0), firstBook.getDescription());
		Assertions.assertEquals(String.format("%s %s", IMAGE_LINKS, 0), firstBook.getImageLink());
	}

	@Test
	public void findBooksByGoogleApiEmptyResultTest() {
		// given
		GoogleBookList googleApiResult = GoogleBookHelper.getGoogleBookList(0, TITLE, SUBTITLE, AUTHORS, PUBLISHED_DATE, PAGE_COUNT, DESCRIPTION, IMAGE_LINKS);
		Mockito.when(googleBooksClient.findBooksByParameter(Mockito.eq(INPUT_PARAM))).thenReturn(googleApiResult);

		// when
		List<BookEntity> output = bookService.findBookByGoogleApi(INPUT_PARAM);

		// then
		Assertions.assertEquals(0, output.size());
	}

	@Test
	public void findBooksByGoogleApiNullResultTest() {
		// given
		Mockito.when(googleBooksClient.findBooksByParameter(Mockito.eq(INPUT_PARAM))).thenReturn(new GoogleBookList());

		// when
		List<BookEntity> output = bookService.findBookByGoogleApi(INPUT_PARAM);

		// then
		Assertions.assertNull(output);
	}

	@Test
	public void findAllBooksInRegisterTest() {
		// given
		SearchCriteriaDto searchCriteriaDto = new SearchCriteriaDto(0, 100, Sort.Direction.DESC, SearchCriteriaDto.SortField.OWN_RATING);

		// given and when
		Page<BookEntity> output = bookService.findBooksInRegister(searchCriteriaDto);

		// then
		Assertions.assertTrue(output.getTotalElements() > 0);
	}

	@Test
	public void findFirstBookWithSearchCriteriaByFindAllBooksInRegisterTest() {
		// given
		SearchCriteriaDto searchCriteriaDto = new SearchCriteriaDto(0, 1, Sort.Direction.DESC, SearchCriteriaDto.SortField.OWN_RATING);

		// when
		Page<BookEntity> outputList = bookService.findBooksInRegister(searchCriteriaDto);

		// then
		Assertions.assertEquals(1, outputList.getSize());
		BookEntity output = outputList.getContent().get(0);
		Assertions.assertEquals("J.R.R. Tolkien", output.getAuthors());
		Assertions.assertEquals("The lord of the rings", output.getTitle());
		Assertions.assertEquals(9, (int) output.getOwnRating());
		Assertions.assertEquals("Good book", output.getOwnReview());
		Assertions.assertEquals("123", output.getPageCount());
		Assertions.assertEquals(LocalDate.of(2020, 10, 4), output.getDateOfReading());
	}

	@Test
	public void findAllBooksInRegisterWithTileLikeTest() {
		// given
		SearchCriteriaDto searchCriteriaDto = new SearchCriteriaDto(0, 10, Sort.Direction.DESC, SearchCriteriaDto.SortField.OWN_RATING);

		// when
		Page<BookEntity> outputList = bookService.findBooksInRegisterWithTitleLike(searchCriteriaDto, "lord of the ");

		// then
		Assertions.assertEquals(1, outputList.getTotalElements());
		BookEntity output = outputList.getContent().get(0);
		Assertions.assertEquals("J.R.R. Tolkien", output.getAuthors());
		Assertions.assertEquals("The lord of the rings", output.getTitle());
		Assertions.assertEquals(9, (int) output.getOwnRating());
		Assertions.assertEquals("Good book", output.getOwnReview());
		Assertions.assertEquals("123", output.getPageCount());
		Assertions.assertEquals(LocalDate.of(2020, 10, 4), output.getDateOfReading());
	}

	@Test
	public void findAllBooksInRegisterByIdTest() {
		// given and when
		BookEntity output = bookService.findBookInRegisterById(BOOK_WITHOUT_FILE_ID);

		// then
		Assertions.assertEquals("J.R.R. Tolkien", output.getAuthors());
		Assertions.assertEquals("The lord of the rings", output.getTitle());
		Assertions.assertEquals(9, (int) output.getOwnRating());
		Assertions.assertEquals("Good book", output.getOwnReview());
		Assertions.assertEquals("123", output.getPageCount());
		Assertions.assertEquals(LocalDate.of(2020, 10, 4), output.getDateOfReading());
	}

	@Test
	public void findAllBooksInRegisterByIdNoBookTest() {
		try {
			// given and when
			bookService.findBookInRegisterById(FAKE_ID);
		} catch (ResponseStatusException ex) {
			Assertions.assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
			Assertions.assertEquals("No book found", ex.getReason());
		}
	}

	@Test
	public void bookHasFileTest() {
		// given and when
		boolean output = bookService.hasFile(BOOK_WITH_FILE_ID);

		// then
		Assertions.assertTrue(output);
	}

	@Test
	public void bookHasFileNullFileIdTest() {
		// given and when
		boolean output = bookService.hasFile(BOOK_WITHOUT_FILE_ID);

		// then
		Assertions.assertFalse(output);
	}

	@Test
	public void bookHasFileNoBookTest() {
		try {
			// given and when
			bookService.hasFile(FAKE_ID);
		} catch (ResponseStatusException ex) {
			Assertions.assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
			Assertions.assertEquals("Cannot verify if book has a file. No book found", ex.getReason());
		}
	}

	@Test
	public void addBookToRegistryTest() {
		// given
		BookEntity input = BookEntityHelper.getBookEntity(TITLE, AUTHORS, PUBLISHED_DATE, PAGE_COUNT, DESCRIPTION,
				IMAGE_LINKS, OWN_REVIEW, OWN_RATING, FILE_ID, DATE_OF_READING);

		// when
		BookEntity output = bookService.addBookToRegister(input);

		// then
		Assertions.assertEquals(TITLE, output.getTitle());
		Assertions.assertEquals(AUTHORS, output.getAuthors());
		Assertions.assertEquals(PUBLISHED_DATE, output.getPublishedDate());
		Assertions.assertEquals(PAGE_COUNT, output.getPageCount());
		Assertions.assertEquals(DESCRIPTION, output.getDescription());
		Assertions.assertEquals(IMAGE_LINKS, output.getImageLink());
		Assertions.assertEquals(OWN_REVIEW, output.getOwnReview());
		Assertions.assertEquals(OWN_RATING, output.getOwnRating());
		Assertions.assertEquals(FILE_ID, output.getFileId());
		Assertions.assertEquals(DATE_OF_READING, output.getDateOfReading());
	}

	@Test
	public void updateBookInRegistryTest() {
		// given
		BookEntity input = BookEntityHelper.getBookEntity(TITLE, AUTHORS, PUBLISHED_DATE, PAGE_COUNT, DESCRIPTION,
				IMAGE_LINKS, OWN_REVIEW, OWN_RATING, FILE_ID, DATE_OF_READING);
		input.setId(BOOK_WITH_FILE_ID);

		// when
		BookEntity output = bookService.updateBookInRegister(input);

		// then
		Assertions.assertEquals(TITLE, output.getTitle());
		Assertions.assertEquals(AUTHORS, output.getAuthors());
		Assertions.assertEquals(PUBLISHED_DATE, output.getPublishedDate());
		Assertions.assertEquals(PAGE_COUNT, output.getPageCount());
		Assertions.assertEquals(DESCRIPTION, output.getDescription());
		Assertions.assertEquals(IMAGE_LINKS, output.getImageLink());
		Assertions.assertEquals(OWN_REVIEW, output.getOwnReview());
		Assertions.assertEquals(OWN_RATING, output.getOwnRating());
		Assertions.assertEquals(FILE_ID, output.getFileId());
		Assertions.assertEquals(DATE_OF_READING, output.getDateOfReading());
	}

	@Test
	public void updateBookInRegistryNoBookTest() {
		// given
		BookEntity input = BookEntityHelper.getBookEntity(TITLE, AUTHORS, PUBLISHED_DATE, PAGE_COUNT, DESCRIPTION,
				IMAGE_LINKS, OWN_REVIEW, OWN_RATING, FILE_ID, DATE_OF_READING);
		input.setId(FAKE_ID);

		try {
			// when
			bookService.updateBookInRegister(input);
		} catch (ResponseStatusException ex) {
			Assertions.assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
			Assertions.assertEquals("No book found", ex.getReason());
		}
	}

	@Test
	public void deleteBookInRegistryTest() {
		// given and when
		bookService.deleteBookFromRegister(BOOK_TO_DELETE_ID);

		// then no error
	}

	@Test
	public void deleteBookInRegistryNoBookTest() {
		try {
			// given and when
			bookService.deleteBookFromRegister(FAKE_ID);
		} catch (ResponseStatusException ex) {
			Assertions.assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
			Assertions.assertEquals("No book found", ex.getReason());
		}
	}
}