package com.en.remembrance.repositories;

import com.en.remembrance.domain.TitlePage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TitlePageRepository extends JpaRepository<TitlePage, Long> {

}
