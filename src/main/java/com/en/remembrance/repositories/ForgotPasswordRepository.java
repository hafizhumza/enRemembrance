package com.en.remembrance.repositories;

import com.en.remembrance.domain.ForgotPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Long> {

    Optional<ForgotPassword> findByToken(String token);

    Optional<ForgotPassword> findByUser_Id(Long userId);

}
