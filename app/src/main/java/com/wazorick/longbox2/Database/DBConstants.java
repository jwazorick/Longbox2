package com.example.longbox2.Database;

class DBConstants {
    static String SETTINGS_TABLE = "settings";
    static String SETTINGS_ID = "_settingsid";
    static String SETTINGS_NAME = "settingsname";
    static String SETTINGS_VALUE = "settingsvalue";

    static String COMIC_TABLE = "comic";
    static String COMIC_ID = "_comicid";
    static String COMIC_TITLE = "comictitle";
    static String COMIC_VOLUME = "comicvolume";
    static String COMIC_ISSUE = "comicissue";
    static String COMIC_PUBLISHER = "comicpublisher";
    static String COMIC_COVER_PRICE = "comiccoverprice";
    static String COMIC_CONDITION = "comiccondition";
    static String COMIC_NOTES = "comicnotes";
    static String COMIC_COVER_IMAGE = "comiccoverimage";
    static String COMIC_FORMAT = "comicformat";

    static String CONDITION_TABLE = "condition";
    static String CONDITION_ID = "_conditionid";
    static String CONDITION_NAME = "conditionname";
    static String CONDITION_VALUE = "conitionvalue";

    static String PUBLISHER_TABLE = "publisher";
    static String PUBLISHER_ID = "_publisherid";
    static String PUBLISHER_NAME = "publishername";

    static String JOB_TABLE = "job";
    static String JOB_ID = "_jobid";
    static String JOB_NAME = "jobname";

    static String CREATOR_TABLE = "creator";
    static String CREATOR_ID = "_creatorid";
    static String CREATOR_NAME = "creatorname";

    static String CREATOR_JOB_ISSUE_TABLE = "creatorjobissuexref";
    static String CREATOR_JOB_ISSUE_ID = "_creatorjobissueid";
    static String CREATOR_ID_FK = "creatorid";
    static String JOB_ID_FK = "jobid";
    static String COMIC_ID_FK = "comicid";

    static String ACHIEVEMENT_TABLE = "achievement";
    static String ACHIEVEMENT_ID = "_achievementid";
    static String ACHIEVEMENT_NAME = "achievementname";
    static String ACHIEVEMENT_UNLOCKED = "achievementunlocked";
    static String ACHIEVEMENT_IMAGE = "achievementimage";
    static String ACHIEVEMENT_DATE = "achievementdate";

    static String WISHLIST_TABLE = "wishlist";
    static String WISHLIST_ID = "_wishlistid";
    static String WISHLIST_TITLE = "wishlisttitle";
    static String WISHLIST_ISSUE = "wishlistissue";
    static String WISHLIST_PUBLISHER_FK = "wishlistpublisher";
    static String WISHLIST_PRIORITY = "wishlistpriority";

    static String WEEKLY_LIST_TABLE = "weeklylist";
    static String WEEKLY_ID = "_weeklyid";
    static String WEEKLY_TITLE = "weeklytitle";
    static String WEEKLY_ISSUE = "weeklyissue";
    static String WEEKLY_PUBLISHER = "weeklypublisher";
    static String WEEKLY_DATE_PUBLISHED = "weeklydatepublished";
}
