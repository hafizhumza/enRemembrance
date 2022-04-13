package com.en.remembrance.services;

import com.en.remembrance.domain.ForgotPassword;
import com.en.remembrance.exceptions.InvalidAccessException;
import com.en.remembrance.repositories.ForgotPasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ForgotPasswordService {

    private final ForgotPasswordRepository forgotPasswordRepository;

    @Autowired
    public ForgotPasswordService(ForgotPasswordRepository forgotPasswordRepository) {
        this.forgotPasswordRepository = forgotPasswordRepository;
    }

    public ForgotPassword findByTokenOrElseThrow(String token) {
        return forgotPasswordRepository.findByToken(token).orElseThrow(InvalidAccessException::new);
    }

    public Optional<ForgotPassword> findByUserId(long userId) {
        return forgotPasswordRepository.findByUser_Id(userId);
    }

    public void save(ForgotPassword forgotPassword) {
        forgotPasswordRepository.save(forgotPassword);
    }
}
