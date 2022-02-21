package com.register.library.repository.model.repository;

import com.register.library.repository.model.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
	@Query("Select b From BookEntity b")
	Page<BookEntity> findBooks(Pageable page);
}
