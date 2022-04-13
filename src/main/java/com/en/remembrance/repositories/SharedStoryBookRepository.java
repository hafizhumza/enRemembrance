package com.en.remembrance.repositories;

import com.en.remembrance.domain.SharedStoryBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SharedStoryBookRepository extends JpaRepository<SharedStoryBook, Long> {

    Optional<SharedStoryBook> findByToken(String token);

}
