package com.en.remembrance.services;

import com.en.remembrance.repositories.ConclusionPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ConclusionPageService {

    private final ConclusionPageRepository conclusionPageRepository;

    @Autowired
    public ConclusionPageService(ConclusionPageRepository conclusionPageRepository) {
        this.conclusionPageRepository = conclusionPageRepository;
    }

}
