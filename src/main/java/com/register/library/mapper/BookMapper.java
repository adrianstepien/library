package com.register.library.mapper;

import com.register.library.googleBooks.entity.GoogleBookInfo;
import com.register.library.repository.model.entity.BookEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface BookMapper {

	@Mapping(target = "title", source = "title")
	@Mapping(target = "authors", expression  = "java(mapListToString(googleBookInfo.getAuthors()))")
	@Mapping(target = "publishedDate", source = "publishedDate")
	@Mapping(target = "pageCount", source = "pageCount")
	@Mapping(target = "description", source = "description")
	@Mapping(target = "imageLink", source = "imageLinks.smallThumbnail")
	BookEntity convertGoogleBookInfoToBookEntity(GoogleBookInfo googleBookInfo);

	default <T> String mapListToString(List<T> listOfObjects) {
		return listOfObjects != null ? listOfObjects.toString() : null;
	}
}
