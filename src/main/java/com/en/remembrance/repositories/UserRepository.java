package com.en.remembrance.repositories;

import com.en.remembrance.domain.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<AuthUser, Long> {

    Optional<AuthUser> findByVerificationToken(String verificationToken);

    Optional<AuthUser> findByEmail(String email);

    List<AuthUser> findByEnabledTrueAndIdNotInAndRoles_Name(Collection<Long> ids, String name);

    Optional<AuthUser> findByForgotPassword_Token(String token);

}
