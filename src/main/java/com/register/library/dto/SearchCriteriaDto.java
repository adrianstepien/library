package com.register.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * Custom object with search criteria
 * Page number first index is 0
 */
@Getter
@Setter

public class SearchCriteriaDto extends PageRequest {
	public SearchCriteriaDto(int page, int size, Sort.Direction direction, SortField sortField) {
		super(page, size, direction, sortField.getFieldValue());
	}

	@AllArgsConstructor
	@Getter
	public enum SortField {
		OWN_RATING("ownRating"),
		DATE_OF_READING("dateOfReading");

		private final String fieldValue;
	}
}
