package com.en.remembrance.repositories;

import com.en.remembrance.domain.ConclusionPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ConclusionPageRepository extends JpaRepository<ConclusionPage, Long> {

}
