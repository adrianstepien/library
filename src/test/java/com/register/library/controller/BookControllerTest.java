package com.register.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.register.library.dto.SearchCriteriaDto;
import com.register.library.helper.BookEntityHelper;
import com.register.library.repository.model.entity.BookEntity;
import com.register.library.services.BookService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

class BookControllerTest {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final String TITLE = "Test Title";
	private static final String AUTHORS = "Test Author";
	private static final String PUBLISHED_DATE = "2003";
	private static final String PAGE_COUNT = "432";
	private static final String OWN_REVIEW = "Own review";
	private static final Integer OWN_RATING = 3;
	private static final String DESCRIPTION = "Test Description";
	private static final String IMAGE_LINKS = "Test Image Links";
	private static final String FILE_ID = "Test File ID";
	private static final LocalDate DATE_OF_READING = LocalDate.of(2022, 11, 13);
	private static final int PAGE = 0;
	private static final int SIZE = 5;
	private static final Sort.Direction DIRECTION = Sort.Direction.DESC;
	private static final SearchCriteriaDto.SortField SORT_FIELD = SearchCriteriaDto.SortField.OWN_RATING;
	private static ObjectMapper jacksonObjectMapper;

	@InjectMocks
	private BookController bookController;

	@Mock
	private BookService bookService;

	private MockMvc mockMvc;

	@BeforeAll
	public static void init() {
		jacksonObjectMapper = new ObjectMapper();
		jacksonObjectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		jacksonObjectMapper.registerModule(new JavaTimeModule());
	}

	@BeforeEach
	public void initBeforeMethod() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(bookController)
				.build();
	}

	@Test
	public void findBooksByParameterTest() throws Exception {
		//given
		BookEntity output = BookEntityHelper.getBookEntity(TITLE, AUTHORS, PUBLISHED_DATE, PAGE_COUNT, DESCRIPTION,
				IMAGE_LINKS, OWN_REVIEW, OWN_RATING, FILE_ID, DATE_OF_READING);
		Mockito.when(bookService.findBookByGoogleApi(Mockito.eq(TITLE)))
				.thenReturn(Collections.singletonList(output));

		//when
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/book/google/findByParam/" + TITLE)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		//then
		DocumentContext result = JsonPath.parse(mvcResult.getResponse().getContentAsString());
		checkBookResponse(result, "[0].");
	}

	@Test
	public void findBooksInRegisterTest() throws Exception {
		//given
		SearchCriteriaDto searchCriteriaDto = new SearchCriteriaDto(PAGE, SIZE, DIRECTION, SORT_FIELD);
		BookEntity output = BookEntityHelper.getBookEntity(TITLE, AUTHORS, PUBLISHED_DATE, PAGE_COUNT, DESCRIPTION,
				IMAGE_LINKS, OWN_REVIEW, OWN_RATING, FILE_ID, DATE_OF_READING);
		PageImpl page = new PageImpl(Collections.singletonList(output), searchCriteriaDto, 1L);
		Mockito.when(bookService.findBooksInRegister(Mockito.eq(searchCriteriaDto)))
				.thenReturn(page);

		//when
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/book")
						.accept(MediaType.APPLICATION_JSON)
						.param("page", String.valueOf(PAGE))
						.param("size", String.valueOf(SIZE))
						.param("direction", DIRECTION.toString())
						.param("sortField", SORT_FIELD.toString())
				)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		//then
		DocumentContext result = JsonPath.parse(mvcResult.getResponse().getContentAsString());
		checkBookResponse(result, "content.[0].");
		Assertions.assertEquals(Integer.valueOf(SIZE), result.read("size"));
		Assertions.assertEquals(Integer.valueOf(PAGE), result.read("number"));;
	}

	@Test
	public void findBooksInRegisterWithTitleLikeTest() throws Exception {
		//given
		SearchCriteriaDto searchCriteriaDto = new SearchCriteriaDto(PAGE, SIZE, DIRECTION, SORT_FIELD);
		BookEntity output = BookEntityHelper.getBookEntity(TITLE, AUTHORS, PUBLISHED_DATE, PAGE_COUNT, DESCRIPTION,
				IMAGE_LINKS, OWN_REVIEW, OWN_RATING, FILE_ID, DATE_OF_READING);
		PageImpl page = new PageImpl(Collections.singletonList(output), searchCriteriaDto, 1L);
		Mockito.when(bookService.findBooksInRegisterWithTitleLike(Mockito.eq(searchCriteriaDto), Mockito.eq(TITLE)))
				.thenReturn(page);

		//when
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/book/byTitle/" + TITLE)
						.accept(MediaType.APPLICATION_JSON)
						.param("page", String.valueOf(PAGE))
						.param("size", String.valueOf(SIZE))
						.param("direction", DIRECTION.toString())
						.param("sortField", SORT_FIELD.toString())
				)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		//then
		DocumentContext result = JsonPath.parse(mvcResult.getResponse().getContentAsString());
		checkBookResponse(result, "content.[0].");
		Assertions.assertEquals(Integer.valueOf(SIZE), result.read("size"));
		Assertions.assertEquals(Integer.valueOf(PAGE), result.read("number"));
	}

	@Test
	public void findBookInRegisterByIdTest() throws Exception {
		//given
		BookEntity output = BookEntityHelper.getBookEntity(TITLE, AUTHORS, PUBLISHED_DATE, PAGE_COUNT, DESCRIPTION,
				IMAGE_LINKS, OWN_REVIEW, OWN_RATING, FILE_ID, DATE_OF_READING);
		Mockito.when(bookService.findBookInRegisterById(Mockito.eq(1L)))
				.thenReturn(output);

		//when
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/book/" + 1L)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		//then
		DocumentContext result = JsonPath.parse(mvcResult.getResponse().getContentAsString());
		checkBookResponse(result, "");
	}

	@Test
	public void hasBookFileTest() throws Exception {
		//given
		Mockito.when(bookService.hasFile(Mockito.eq(1L))).thenReturn(true);

		//when
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/book/hasFile/" + 1L)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		//then
		Assertions.assertEquals(true, JsonPath.parse(mvcResult.getResponse().getContentAsString()).json());
	}

	@Test
	public void addBookToRegisterTest() throws Exception {
		//given
		BookEntity input = BookEntityHelper.getBookEntity(TITLE, AUTHORS, PUBLISHED_DATE, PAGE_COUNT, DESCRIPTION,
				IMAGE_LINKS, OWN_REVIEW, OWN_RATING, FILE_ID, DATE_OF_READING);
		Mockito.when(bookService.addBookToRegister(Mockito.any())).thenReturn(input);
		String inputJson = jacksonObjectMapper.writeValueAsString(input);

		//when
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/book")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(inputJson)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		//then
		DocumentContext result = JsonPath.parse(mvcResult.getResponse().getContentAsString());
		checkBookResponse(result, "");
	}

	@Test
	public void updateBookInRegisterTest() throws Exception {
		//given
		BookEntity input = BookEntityHelper.getBookEntity(TITLE, AUTHORS, PUBLISHED_DATE, PAGE_COUNT, DESCRIPTION,
				IMAGE_LINKS, OWN_REVIEW, OWN_RATING, FILE_ID, DATE_OF_READING);
		Mockito.when(bookService.updateBookInRegister(Mockito.any())).thenReturn(input);
		String inputJson = jacksonObjectMapper.writeValueAsString(input);

		//when
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/book")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(inputJson)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		//then
		DocumentContext result = JsonPath.parse(mvcResult.getResponse().getContentAsString());
		checkBookResponse(result, "");
	}

	@Test
	public void deleteBookFromRegisterTest() throws Exception {
		//given and when
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/book/" + 1L)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		//then not exception
	}

	private void checkBookResponse(DocumentContext result, String prefix) {
		Assertions.assertEquals(TITLE, result.read(prefix + "title"));
		Assertions.assertEquals(AUTHORS, result.read(prefix + "authors"));
		Assertions.assertEquals(PUBLISHED_DATE, result.read(prefix + "publishedDate"));
		Assertions.assertEquals(PAGE_COUNT, result.read(prefix + "pageCount"));
		Assertions.assertEquals(DESCRIPTION, result.read(prefix + "description"));
		Assertions.assertEquals(IMAGE_LINKS, result.read(prefix + "imageLink"));
		Assertions.assertEquals(OWN_REVIEW, result.read(prefix + "ownReview"));
		Assertions.assertEquals(OWN_RATING, result.read(prefix + "ownRating"));
		Assertions.assertEquals(FILE_ID, result.read(prefix + "fileId"));
		Assertions.assertEquals(DATE_OF_READING.format(FORMATTER), result.read(prefix + "dateOfReading"));
	}
}