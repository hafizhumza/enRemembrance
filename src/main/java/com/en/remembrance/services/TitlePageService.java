package com.en.remembrance.services;

import com.en.remembrance.repositories.TitlePageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TitlePageService {

    private final TitlePageRepository titlePageRepository;

    @Autowired
    public TitlePageService(TitlePageRepository titlePageRepository) {
        this.titlePageRepository = titlePageRepository;
    }

}
