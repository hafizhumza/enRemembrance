package com.en.remembrance.repositories;

import com.en.remembrance.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PageRepository extends JpaRepository<Page, Long> {

}
