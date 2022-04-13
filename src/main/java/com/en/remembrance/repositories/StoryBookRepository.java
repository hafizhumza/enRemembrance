package com.en.remembrance.repositories;

import com.en.remembrance.domain.StoryBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface StoryBookRepository extends JpaRepository<StoryBook, Long> {

    long countByUser_IdAndRecycledIsFalse(Long id);

    Optional<StoryBook> findByIdAndUser_IdAndRecycledIsFalse(Long id, Long userId);

    List<StoryBook> findByUser_IdEqualsAndRecycledIsFalse(Long id);

    long countByIdIsNotNull();

    long countByRecycledIsTrue();

    long countByRecycledIsFalse();

    List<StoryBook> findByRecycledIsTrue();

    List<StoryBook> findByRecycledIsFalse();

}
