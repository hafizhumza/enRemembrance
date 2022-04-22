package com.en.remembrance.services;

import com.en.remembrance.constants.Constant;
import com.en.remembrance.constants.PagePosition;
import com.en.remembrance.domain.AuthUser;
import com.en.remembrance.domain.Category;
import com.en.remembrance.domain.ConclusionPage;
import com.en.remembrance.domain.Page;
import com.en.remembrance.domain.Segment;
import com.en.remembrance.domain.StoryBook;
import com.en.remembrance.domain.TitlePage;
import com.en.remembrance.dtos.ConclusionPageModel;
import com.en.remembrance.dtos.PageModel;
import com.en.remembrance.dtos.SegmentModel;
import com.en.remembrance.dtos.ShareStoryBookRequest;
import com.en.remembrance.dtos.ShareStoryEmailDto;
import com.en.remembrance.dtos.StoryBookListModel;
import com.en.remembrance.dtos.StoryBookModel;
import com.en.remembrance.dtos.TitlePageModel;
import com.en.remembrance.dtos.UserDto;
import com.en.remembrance.exceptions.StoryBookNotFoundException;
import com.en.remembrance.repositories.StoryBookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


@Slf4j
@Service
public class StoryBookService extends BaseService {

    private final UserService userService;

    private final CategoryService categoryService;

    private final FileService fileService;

    private final SharedStoryBookService sharedStoryBookService;

    private final EmailService emailService;

    private final StoryBookRepository storyBookRepository;

    @Autowired
    public StoryBookService(UserService userService, CategoryService categoryService, StoryBookRepository storyBookRepository, FileService fileService, SharedStoryBookService sharedStoryBookService, EmailService emailService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.fileService = fileService;
        this.sharedStoryBookService = sharedStoryBookService;
        this.storyBookRepository = storyBookRepository;
        this.emailService = emailService;
    }

    public List<StoryBookListModel> findStoryBookListModelByUserId(Long userId) {
        List<StoryBook> storyBooks = storyBookRepository.findByUser_IdEqualsAndRecycledIsFalse(userId);
        return getStoryBookListModels(storyBooks);
    }

    public StoryBookModel findByIdAndUserId(Long storybookId, Long userId) {
        StoryBook storyBook = storyBookRepository.findByIdAndUser_IdAndRecycledIsFalse(storybookId, userId).orElseThrow(StoryBookNotFoundException::new);
        return getStoryBookModel(storyBook);
    }

    public StoryBookModel getStoryBookModel(StoryBook storyBook) {
        if (storyBook == null) {
            return null;
        }

        TitlePage titlePage = storyBook.getTitlePage();
        ConclusionPage conclusionPage = storyBook.getConclusionPage();

        try {
            titlePage.setBase64Image(fileService.convertToBase64(titlePage.getImagePath()));
            conclusionPage.setBase64Image(fileService.convertToBase64(conclusionPage.getImagePath()));
        } catch (IOException e) {
            log.error("Exception occurred while converting base64 image: ", e);
        }

        List<Page> pages = storyBook.getPages();

        if (!CollectionUtils.isEmpty(pages)) {
            for (Page page : pages) {
                List<Segment> segments = page.getSegments();

                if (!CollectionUtils.isEmpty(segments)) {
                    for (Segment segment : segments) {
                        try {
                            segment.setBase64Image(fileService.convertToBase64(segment.getImagePath()));
                        } catch (IOException e) {
                            log.error("Exception occurred while converting base64 image: ", e);
                        }
                    }
                }
            }
        }

        return new StoryBookModel(storyBook);
    }

    public StoryBook save(StoryBookModel storyBookModel) throws IOException {
        StoryBook storyBook = new StoryBook();
        storyBook.setRecycled(false);

        Category category = categoryService.getByIdOrElseThrow(storyBookModel.getTitlePage().getCategoryId());
        storyBook.setCategory(category);

        AuthUser user = userService.getByIdOrElseThrow(storyBookModel.getUserId());
        storyBook.setUser(user);

        TitlePageModel titlePageModel = storyBookModel.getTitlePage();
        String titleImagePath = fileService.saveBase64File(titlePageModel.getBase64Image(), titlePageModel.getImageType());

        TitlePage titlePage = new TitlePage(titlePageModel);
        titlePage.setImagePath(titleImagePath);
        titlePage.setStoryBook(storyBook);
        storyBook.setTitlePage(titlePage);

        ConclusionPageModel conclusionPageModel = storyBookModel.getConclusionPage();
        String conclusionImagePath = fileService.saveBase64File(conclusionPageModel.getBase64Image(), conclusionPageModel.getImageType());

        ConclusionPage conclusionPage = new ConclusionPage(conclusionPageModel);
        conclusionPage.setImagePath(conclusionImagePath);
        conclusionPage.setStoryBook(storyBook);
        storyBook.setConclusionPage(conclusionPage);

        List<PageModel> pageModels = storyBookModel.getPages();
        List<Page> pages = pageModels.stream().map(p -> {
            Page page = new Page();

            SegmentModel topLeftSegment = p.getTopLeftSegment();
            SegmentModel topRightSegment = p.getTopRightSegment();
            SegmentModel bottomLeftSegment = p.getBottomLeftSegment();
            SegmentModel bottomRightSegment = p.getBottomRightSegment();

            String tlSegmentImagePath = null;
            String trSegmentImagePath = null;
            String blSegmentImagePath = null;
            String brSegmentImagePath = null;

            try {
                tlSegmentImagePath = fileService.saveBase64File(topLeftSegment.getBase64Image(), topLeftSegment.getImageType());
                trSegmentImagePath = fileService.saveBase64File(topRightSegment.getBase64Image(), topRightSegment.getImageType());
                blSegmentImagePath = fileService.saveBase64File(bottomLeftSegment.getBase64Image(), bottomLeftSegment.getImageType());
                brSegmentImagePath = fileService.saveBase64File(bottomRightSegment.getBase64Image(), bottomRightSegment.getImageType());
            } catch (IOException e) {
                log.error("Exception while saving image: ", e);
            }

            Segment tlSegment = new Segment(topLeftSegment, PagePosition.TOP_LEFT);
            tlSegment.setImagePath(tlSegmentImagePath);
            tlSegment.setPage(page);

            Segment trSegment = new Segment(topRightSegment, PagePosition.TOP_RIGHT);
            trSegment.setImagePath(trSegmentImagePath);
            trSegment.setPage(page);

            Segment blSegment = new Segment(bottomLeftSegment, PagePosition.BOTTOM_LEFT);
            blSegment.setImagePath(blSegmentImagePath);
            blSegment.setPage(page);

            Segment brSegment = new Segment(bottomRightSegment, PagePosition.BOTTOM_RIGHT);
            brSegment.setImagePath(brSegmentImagePath);
            brSegment.setPage(page);

            page.addSegment(tlSegment);
            page.addSegment(trSegment);
            page.addSegment(blSegment);
            page.addSegment(brSegment);

            page.setStoryBook(storyBook);
            return page;
        }).collect(Collectors.toList());

        storyBook.setPages(pages);
        return storyBookRepository.save(storyBook);
    }

    public long countByUserId(Long userId) {
        return storyBookRepository.countByUser_IdAndRecycledIsFalse(userId);
    }

    public boolean share(ShareStoryBookRequest request, UserDto userDto) throws MessagingException, ExecutionException, InterruptedException {
        StoryBook storyBook = storyBookRepository.findById(request.getId()).orElseThrow(StoryBookNotFoundException::new);

        if (!userDto.getId().equals(storyBook.getUser().getId())) {
            invalidAccess("This Storybook doesn't belong to you.");
        }

        String url = sharedStoryBookService.save(storyBook);
        ShareStoryEmailDto dto = new ShareStoryEmailDto(request.getReceiverEmail(), userDto.getName(), url);

        Boolean result = emailService.emailSharedStory(dto).get();
        return result != null && result;
    }

    // TODO: Async commented for now due to Uri builder exception.
    //@Async
    public void share(List<ShareStoryBookRequest> requests, UserDto userDto) throws MessagingException, ExecutionException, InterruptedException {
        for (ShareStoryBookRequest request : requests) {
            try {
                this.share(request, userDto);
            } catch (StoryBookNotFoundException e) {
                throw e;
            } catch (Exception e) {
                log.error("Error while sharing storybook: ", e);
            }
        }
    }

    private List<StoryBookListModel> getStoryBookListModels(List<StoryBook> storyBooks) {
        if (!CollectionUtils.isEmpty(storyBooks)) {
            return storyBooks.stream().map(StoryBookListModel::new).collect(Collectors.toList());
        }

        return null;
    }

    // Admin Methods
    public StoryBookModel findById(Long storybookId) {
        StoryBook storyBook = storyBookRepository.findById(storybookId).orElseThrow(StoryBookNotFoundException::new);
        return getStoryBookModel(storyBook);
    }

    public long countAllStories() {
        return storyBookRepository.countByIdIsNotNull();
    }

    public long countActiveStories() {
        return storyBookRepository.countByRecycledIsFalse();
    }

    public long countTrashedStories() {
        return storyBookRepository.countByRecycledIsTrue();
    }

    public List<StoryBookListModel> findAllStoryBookListModel() {
        List<StoryBook> storyBooks = storyBookRepository.findAll();
        return getStoryBookListModels(storyBooks);
    }

    public List<StoryBookListModel> findActiveStoryBookListModel() {
        List<StoryBook> storyBooks = storyBookRepository.findByRecycledIsFalse();
        return getStoryBookListModels(storyBooks);
    }

    public List<StoryBookListModel> findInActiveStoryBookListModel() {
        List<StoryBook> storyBooks = storyBookRepository.findByRecycledIsTrue();
        return getStoryBookListModels(storyBooks);
    }

    public boolean recycle(long storyBookId) {
        StoryBook storyBook = storyBookRepository.findById(storyBookId).orElseThrow(StoryBookNotFoundException::new);
        storyBook.setRecycled(true);
        storyBookRepository.save(storyBook);
        return true;
    }

    public boolean restore(long storyBookId) {
        StoryBook storyBook = storyBookRepository.findById(storyBookId).orElseThrow(StoryBookNotFoundException::new);
        storyBook.setRecycled(false);
        storyBookRepository.save(storyBook);
        return true;
    }

    public boolean delete(long storyBookId) {
        StoryBook storyBook = storyBookRepository.findById(storyBookId).orElseThrow(StoryBookNotFoundException::new);

        if (!storyBook.getRecycled()) {
            invalidAccess();
        }

        List<String> imagePaths = Optional.ofNullable(storyBook.getPages())
                .orElseGet(Collections::emptyList)
                .stream().flatMap(p -> Optional.ofNullable(p.getSegments())
                                            .orElseGet(Collections::emptyList)
                                            .stream()
                                            .map(Segment::getImagePath)
                                            .filter(StringUtils::hasText)).collect(Collectors.toList());

        imagePaths.add(storyBook.getTitlePage().getImagePath());
        if (StringUtils.hasText(storyBook.getConclusionPage().getImagePath())) {
            imagePaths.add(storyBook.getConclusionPage().getImagePath());
        }

        fileService.deleteIfExists(imagePaths);
        storyBookRepository.delete(storyBook);
        return true;
    }
}
