package com.register.library.repository.model.repository;

import com.register.library.repository.model.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(timeout = 100)
public interface BookRepository extends JpaRepository<BookEntity, Long> {
}
