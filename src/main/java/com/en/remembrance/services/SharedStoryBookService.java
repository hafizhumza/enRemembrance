package com.en.remembrance.services;

import com.en.remembrance.domain.SharedStoryBook;
import com.en.remembrance.domain.StoryBook;
import com.en.remembrance.exceptions.StoryBookExpiredException;
import com.en.remembrance.exceptions.StoryBookNotFoundException;
import com.en.remembrance.properties.ApplicationProperties;
import com.en.remembrance.repositories.SharedStoryBookRepository;
import com.en.remembrance.utilities.UrlBuilderUtil;
import com.en.remembrance.utilities.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class SharedStoryBookService extends BaseService {

    private final SharedStoryBookRepository sharedStoryBookRepository;

    private final ApplicationProperties applicationProperties;

    private final UrlBuilderUtil urlBuilderUtil;

    @Autowired
    public SharedStoryBookService(SharedStoryBookRepository sharedStoryBookRepository, FileService fileService, ApplicationProperties applicationProperties, UrlBuilderUtil urlBuilderUtil) {
        this.sharedStoryBookRepository = sharedStoryBookRepository;
        this.applicationProperties = applicationProperties;
        this.urlBuilderUtil = urlBuilderUtil;
    }

    public String save(StoryBook storyBook) {
        String token = Util.getRandomUUIDWithSalt();

        SharedStoryBook sharedStoryBook = new SharedStoryBook();
        sharedStoryBook.setToken(token);
        sharedStoryBook.setStoryBook(storyBook);
        sharedStoryBookRepository.save(sharedStoryBook);

        return urlBuilderUtil.createUrlWithToken("shared/storybooks/", token); // TODO: Hardcoded URL value.path(token);
    }

    public StoryBook getSharedStoryBook(String token) {
        SharedStoryBook sharedStoryBook = sharedStoryBookRepository.findByToken(token).orElseThrow(StoryBookNotFoundException::new);

        StoryBook storyBook = sharedStoryBook.getStoryBook();

        if (storyBook.getRecycled()) {
            throw new StoryBookExpiredException();
        }

        // TODO: Shared storybooks expired code is commented
        /*if (Util.isExpired(applicationProperties.security().getSharedStorybooksExpiryDays(), sharedStoryBook.getCreateDate())) {
            throw new StoryBookExpiredException();
        }*/

        return storyBook;
    }

}
