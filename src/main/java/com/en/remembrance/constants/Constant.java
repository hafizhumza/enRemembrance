package com.en.remembrance.constants;

public class Constant {

    /**
     * Constant values
     */
    public static final int TITLE_PAGE_NUM = 1;

    public static final int CONCLUSION_PAGE_NUM = 10;

    public static final int PAGES_ALLOWED = CONCLUSION_PAGE_NUM - 1 - TITLE_PAGE_NUM; // - 1 is for Conclusion Page

    /**
     * Keys
     */
    public static final String KEY_LOGIN_MODEL = "loginModel";

    public static final String KEY_REGISTER_MODEL = "registerModel";

    public static final String KEY_ERROR = "error";

    public static final String KEY_STORY_BOOK_ERROR = "storybookError";

    public static final String KEY_IMAGE_ERROR = "imageError";

    public static final String KEY_USER = "user";

    public static final String KEY_PROCESS_USER = "processUser";

    public static final String KEY_USERS = "users";

    public static final String KEY_ACTIVE_USERS_COUNT = "activeUsersCount";

    public static final String KEY_IN_ACTIVE_USERS_COUNT = "inActiveUsersCount";

    public static final String KEY_ALL_USERS = "allUsers";

    public static final String KEY_RESULT_MESSAGE = "ResultMessage";

    public static final String KEY_SESSION = "session";

    public static final String KEY_STORY_BOOKS = "storyBooks";

    public static final String KEY_STORY_BOOK = "storyBook";

    public static final String KEY_STORY_BOOK_ID = "storyBookId";

    public static final String KEY_SHARE_STORY_BOOK = "shareStoryBook";

    public static final String KEY_TITLE_PAGE_MODEL = "titlePageModel";

    public static final String KEY_PAGE_MODEL = "pageModel";

    public static final String KEY_CONCLUSION_PAGE_MODEL = "conclusionPageModel";

    public static final String KEY_STORIES_COUNT = "storiesCount";

    public static final String KEY_ALL_STORIES_COUNT = "allStoriesCount";

    public static final String KEY_ACTIVE_STORIES_COUNT = "activeStoriesCount";

    public static final String KEY_TRASHED_STORIES_COUNT = "trashedStoriesCount";

    public static final String KEY_CATEGORIES = "categories";

    public static final String KEY_CATEGORY_NAME = "categoryName";

    public static final String KEY_PAGE = "page";

    public static final String KEY_TITLE_PAGE_NUM = "titlePageNum";

    public static final String KEY_CONCLUSION_PAGE_NUM = "conclusionPageNum";

    public static final String KEY_MODE = "mode";

    public static final String KEY_TOKEN = "token";

    public static final String KEY_CHANGE_FORGOT_PASSWORD_MODEL = "changeForgotPassword";

    public static final String KEY_CHANGE_PASSWORD_MODEL = "changePassword";

    public static final String KEY_UPDATE_PROFILE_MODEL = "updateProfile";

    /**
     * Messages
     */
    public static final String MESSAGE_INVALID_LOGIN = "Invalid email or password";

    public static final String MESSAGE_ACCOUNT_LOCKED = "User account is locked";

    public static final String MESSAGE_LOGIN = "Please login to continue";

    /**
     * URLs
     */
    public static final String URL_AUTH_SERVER = "/aus/api/v1/";

    public static final String URL_ADMIN_SERVICE = "/as/api/v1/";

    public static final String URL_DOCUMENT_SERVICE = "/ds/api/v1/document/";

    /**
     * ROLE
     */
    public static final String ROLE_USER = "ROLE_USER";

    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    /**
     * SCOPE
     */
    public static final String SCOPE_WRITE = "WRITE";

    public static final String SCOPE_READ = "READ";

    public static final String SCOPE_DOCUMENT = "DOCUMENT";

    public static final String SCOPE_EMAIL = "EMAIL";

    /**
     * Other
     */
    public static final String FILE_EXTENSION_ENCRYPTED = "encrypted";

    /**
     * Session
     */
    public static final String SESSION_CREATE_STORY_BOOK = "StoryBookSession";

    //    public static final String SESSION_CURRENT_PAGE = "currentPage";

}
