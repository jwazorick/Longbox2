package com.wazorick.longbox2.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wazorick.longbox2.Objects.Achievement;
import com.wazorick.longbox2.Objects.Comic;
import com.wazorick.longbox2.Objects.Creator;
import com.wazorick.longbox2.Objects.WeeklyItem;
import com.wazorick.longbox2.Objects.WishlistItem;
import com.wazorick.longbox2.Utils.EnumUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

//ToDo: Convert all raw queries to safe methods
public class DBHandler extends SQLiteOpenHelper {
    private static final String DBNAME =  "longbox.db";

    public DBHandler(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creating tables

        //Settings table
        db.execSQL("create table " + DBConstants.SETTINGS_TABLE + " (" + DBConstants.SETTINGS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DBConstants.SETTINGS_NAME + " TEXT, " + DBConstants.SETTINGS_VALUE + " TEXT)");

        //Comic table
        db.execSQL("create table " + DBConstants.COMIC_TABLE + " (" + DBConstants.COMIC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DBConstants.COMIC_TITLE + " TEXT, " + DBConstants.COMIC_ISSUE + " TEXT, " +
                DBConstants.COMIC_VOLUME + " TEXT, " + DBConstants.COMIC_PUBLISHER + " INTEGER, " + DBConstants.COMIC_COVER_PRICE + " TEXT, " + DBConstants.COMIC_COVER_IMAGE + " TEXT, " + DBConstants.COMIC_CONDITION +
                " TEXT, " + DBConstants.COMIC_NOTES + " TEXT, " + DBConstants.COMIC_FORMAT + " TEXT)");

        //Comic Condition table
        db.execSQL("create table " + DBConstants.CONDITION_TABLE + " (" + DBConstants.CONDITION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DBConstants.CONDITION_NAME + " TEXT, " + DBConstants.CONDITION_VALUE + " NUMBER)");

        //Creator table
        db.execSQL("create table " + DBConstants.CREATOR_TABLE + " (" + DBConstants.CREATOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DBConstants.CREATOR_NAME + " TEXT)");

        //Publisher table
        db.execSQL("create table " + DBConstants.PUBLISHER_TABLE + " (" + DBConstants.PUBLISHER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DBConstants.PUBLISHER_NAME + " TEXT)");

        //Job table
        db.execSQL("create table " + DBConstants.JOB_TABLE +  "(" + DBConstants.JOB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DBConstants.JOB_NAME + " TEXT)");

        //Creator Job Issue Table
        db.execSQL("create table " + DBConstants.CREATOR_JOB_ISSUE_TABLE + "(" + DBConstants.CREATOR_JOB_ISSUE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DBConstants.CREATOR_ID_FK + " INTEGER, " + DBConstants.JOB_ID_FK + " INTEGER, " + DBConstants.COMIC_ID_FK + " INTEGER)");

        //Achievement table
        db.execSQL("create table " + DBConstants.ACHIEVEMENT_TABLE + " (" + DBConstants.ACHIEVEMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DBConstants.ACHIEVEMENT_NAME + " TEXT, " + DBConstants.ACHIEVEMENT_IMAGE +
                " TEXT, " + DBConstants.ACHIEVEMENT_UNLOCKED + " NUMERIC, " + DBConstants.ACHIEVEMENT_DATE + " INTEGER, " + DBConstants.ACHIEVEMENT_DESC + " TEXT, " + DBConstants.ACHIEVEMENT_TOTAL + " INTEGER, " +
                DBConstants.ACHIEVEMENT_PROGRESS + " INTEGER)");

        //Wishlist table
        db.execSQL("create table " + DBConstants.WISHLIST_TABLE + " (" + DBConstants.WISHLIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DBConstants.WISHLIST_TITLE + " TEXT, " + DBConstants.WISHLIST_ISSUE +
                " TEXT, " + DBConstants.WISHLIST_PUBLISHER_FK + " INTEGER, " + DBConstants.WISHLIST_PRIORITY + " TEXT)");

        //Weekly List table
        db.execSQL("create table " + DBConstants.WEEKLY_LIST_TABLE + " (" + DBConstants.WEEKLY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DBConstants.WEEKLY_TITLE + " TEXT, " + DBConstants.WEEKLY_ISSUE +
                " TEXT, " + DBConstants.WEEKLY_PUBLISHER + " INTEGER, " + DBConstants.WEEKLY_DATE_PUBLISHED + " INTEGER)");

        loadInitialPublishers(db);
        loadAchievements(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void loadInitialPublishers(SQLiteDatabase db) {
        ContentValues dc = new ContentValues();
        ContentValues marvel = new ContentValues();
        dc.put(DBConstants.PUBLISHER_NAME, "DC");
        marvel.put(DBConstants.PUBLISHER_NAME, "Marvel");

        db.insert(DBConstants.PUBLISHER_TABLE, null, dc);
        db.insert(DBConstants.PUBLISHER_TABLE, null, marvel);
    }

    private void loadAchievements(SQLiteDatabase db) {
        ContentValues beginner = DBUtils.createAchievementContentValues("Beginner Collector", "Add 10 comics to your collection", "badge_beginner", 10);
        ContentValues seasoned = DBUtils.createAchievementContentValues("Seasoned Collector", "Add 100 comics to your collection", "badge_seasoned", 100);
        ContentValues superCollector = DBUtils.createAchievementContentValues("Super Collector", "Add 500 comics to your collection", "badge_super", 500);
        ContentValues ultra = DBUtils.createAchievementContentValues("Ultra Collector", "Add 1000 comics to your collection", "badge_ultra", 1000);
        ContentValues mega = DBUtils.createAchievementContentValues("Mega Collector", "Add 5000 comics to your collection", "badge_mega", 5000);
        ContentValues bigTwo = DBUtils.createAchievementContentValues("The Big Two", "Have at least 50 comics from Marvel and DC each", "badge_bigtwo", 100);
        ContentValues wellRounded = DBUtils.createAchievementContentValues("Well Rounded Collection", "Have at least 25 comics from 6 different publishers", "badge_wellrounded", 150);
        ContentValues batFan = DBUtils.createAchievementContentValues("Bat Fan", "Add 200 Batman and Batman-related comics", "badge_batman", 200);
        ContentValues ironFan = DBUtils.createAchievementContentValues("I Am Iron Fan", "Add 200 Ironman and Ironman-related comics", "badge_ironman", 200);
        ContentValues superFan = DBUtils.createAchievementContentValues("Super Fan", "Add 200 Superman and Superman-related comics", "badge_superman", 200);
        ContentValues realAmerican = DBUtils.createAchievementContentValues("Real American", "Add 200 Captain America comics to your collection", "badge_captainamerica", 200);
        ContentValues spiderFan = DBUtils.createAchievementContentValues("Spider-Fan", "Add 200 Spiderman and Spiderman-related comics to your collection", "badge_spiderman", 200);
        ContentValues greenLantern = DBUtils.createAchievementContentValues("Green Lantern", "Add 200 Green Lantern and Green Lantersn-related comics to your collection", "badge_greenlantern", 200);
        ContentValues fantasticFan = DBUtils.createAchievementContentValues("Fan-tastic Fan", "Add 200 Fantastic Four and Fantastic Four related comics to your collection", "badge_fantasticfour", 200);
        ContentValues xFan = DBUtils.createAchievementContentValues("X-Fan", "Add 200 X-Men and X-Men related comics to your collection", "badge_xmen", 200);
        ContentValues whereNoFan = DBUtils.createAchievementContentValues("Where No Fan Has Gone Before", "Add 200 Star Trek items to your collection", "badge_startrek", 200);
        ContentValues holocron = DBUtils.createAchievementContentValues("Enough To Fill a Holocron", "Add 200 Star Wars items to your collection", "badge_starwars", 200);
        ContentValues fandom = DBUtils.createAchievementContentValues("Fandom Fan", "Add 100 Star Wars and Star Trek items each to your collection", "badge_fandom", 100);
        ContentValues giFan = DBUtils.createAchievementContentValues("G.I. Fan", "Add 100 G.I. Joe itesm to your collection", "badge_gijoe", 100);
        ContentValues speedReader = DBUtils.createAchievementContentValues("Speed Reader", "Add 200 Flash comics to your collection", "badge_flash", 200);
        ContentValues hulkedOut = DBUtils.createAchievementContentValues("Hulked Out", "Add 200 Hulk and Hulk-related comics to your collection", "badge_hulk", 200);
        ContentValues fullQuiver = DBUtils.createAchievementContentValues("Full Quiver", "Add 200 Green Arrow or Hawkeye comics to your collection", "badge_arrow", 200);
        ContentValues deadTree = DBUtils.createAchievementContentValues("Dead Tree Reader", "Add 500 physical items to your collection", "badge_tree", 500);
        ContentValues digital = DBUtils.createAchievementContentValues("Digital Maven", "Add 500 digital items to your collection", "badge_digital", 500);
        ContentValues powerConverter = DBUtils.createAchievementContentValues("Power Converter", "Convert 100 Weekly List items to your collection", "badge_converter", 100);
        ContentValues wishMaster = DBUtils.createAchievementContentValues("Wish Master", "Convert 50 Wishlist items to your collection", "badge_wish", 50);

        db.beginTransaction();
        try {
            db.insert(DBConstants.ACHIEVEMENT_TABLE, null, beginner);
            db.insert(DBConstants.ACHIEVEMENT_TABLE, null, seasoned);
            db.insert(DBConstants.ACHIEVEMENT_TABLE, null, superCollector);
            db.insert(DBConstants.ACHIEVEMENT_TABLE, null, ultra);
            db.insert(DBConstants.ACHIEVEMENT_TABLE, null, mega);
            db.insert(DBConstants.ACHIEVEMENT_TABLE, null, bigTwo);
            db.insert(DBConstants.ACHIEVEMENT_TABLE, null, wellRounded);
            db.insert(DBConstants.ACHIEVEMENT_TABLE, null, batFan);
            db.insert(DBConstants.ACHIEVEMENT_TABLE, null, ironFan);
            db.insert(DBConstants.ACHIEVEMENT_TABLE, null, superFan);
            db.insert(DBConstants.ACHIEVEMENT_TABLE, null, realAmerican);
            db.insert(DBConstants.ACHIEVEMENT_TABLE, null, spiderFan);
            db.insert(DBConstants.ACHIEVEMENT_TABLE, null, greenLantern);
            db.insert(DBConstants.ACHIEVEMENT_TABLE, null, fantasticFan);
            db.insert(DBConstants.ACHIEVEMENT_TABLE, null, xFan);
            db.insert(DBConstants.ACHIEVEMENT_TABLE, null, whereNoFan);
            db.insert(DBConstants.ACHIEVEMENT_TABLE, null, holocron);
            db.insert(DBConstants.ACHIEVEMENT_TABLE, null, fandom);
            db.insert(DBConstants.ACHIEVEMENT_TABLE, null, giFan);
            db.insert(DBConstants.ACHIEVEMENT_TABLE, null, speedReader);
            db.insert(DBConstants.ACHIEVEMENT_TABLE, null, hulkedOut);
            db.insert(DBConstants.ACHIEVEMENT_TABLE, null, fullQuiver);
            db.insert(DBConstants.ACHIEVEMENT_TABLE, null, deadTree);
            db.insert(DBConstants.ACHIEVEMENT_TABLE, null, digital);
            db.insert(DBConstants.ACHIEVEMENT_TABLE, null, powerConverter);
            db.insert(DBConstants.ACHIEVEMENT_TABLE, null, wishMaster);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    //Get all comics in the collection, specifically for the View Collection fragment
    public List<Comic> getAllComicsForGallery() {
        List<Comic> comicList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor result = db.rawQuery("Select * from " + DBConstants.COMIC_TABLE + " Order by " + DBConstants.COMIC_TITLE + ", " + DBConstants.COMIC_VOLUME + ", " + DBConstants.COMIC_ISSUE, null);

        if(result.getCount() == 0) {
            //No comics in the collection
            result.close();
            return comicList;
        }

        result.moveToFirst();
        for(int i = 0; i < result.getCount(); i++) {
            //Load the array list
            Comic comic = new Comic();
            comic.setComicID(result.getInt(result.getColumnIndex(DBConstants.COMIC_ID)));
            comic.setComicTitle(result.getString(result.getColumnIndex(DBConstants.COMIC_TITLE)));
            comic.setComicVolume(result.getString(result.getColumnIndex(DBConstants.COMIC_VOLUME)));
            comic.setComicIssue(result.getString(result.getColumnIndex(DBConstants.COMIC_ISSUE)));
            comic.setComicCoverImage(result.getString(result.getColumnIndex(DBConstants.COMIC_COVER_IMAGE)));
            comicList.add(comic);
            result.moveToNext();
        }

        result.close();
        return comicList;
    }

    public Comic getComicById(int comicId) {
        Comic comic = new Comic();
        SQLiteDatabase db = this.getReadableDatabase();

        //Need to join comic with publisher and condition table
        Cursor result = db.rawQuery("select * from " + DBConstants.COMIC_TABLE + " left join " + DBConstants.PUBLISHER_TABLE + " on " + DBConstants.COMIC_TABLE + "." + DBConstants.COMIC_PUBLISHER + " = " +
                DBConstants.PUBLISHER_TABLE + "." + DBConstants.PUBLISHER_ID + " left join " + DBConstants.CONDITION_TABLE + " on " + DBConstants.COMIC_TABLE + "." + DBConstants.COMIC_CONDITION + " = " +
                DBConstants.CONDITION_TABLE + "." + DBConstants.CONDITION_ID + " where " + DBConstants.COMIC_TABLE + "." + DBConstants.COMIC_ID + " = " + comicId, null);

        if(result.getCount() == 0) {
            result.close();
            return comic;
        }

        result.moveToFirst();
        comic.setComicID(result.getInt(result.getColumnIndex(DBConstants.COMIC_ID)));
        comic.setComicTitle(result.getString(result.getColumnIndex(DBConstants.COMIC_TITLE)));
        comic.setComicVolume(result.getString(result.getColumnIndex(DBConstants.COMIC_VOLUME)));
        comic.setComicIssue(result.getString(result.getColumnIndex(DBConstants.COMIC_ISSUE)));
        comic.setComicPublisherID(result.getInt(result.getColumnIndex(DBConstants.COMIC_PUBLISHER)));
        comic.setComicPublisherName(result.getString(result.getColumnIndex(DBConstants.PUBLISHER_NAME)));
        comic.setComicCoverPrice(result.getString(result.getColumnIndex(DBConstants.COMIC_COVER_PRICE)));
        comic.setComicConditionText(result.getString(result.getColumnIndex(DBConstants.COMIC_CONDITION)));
        comic.setComicFormat(EnumUtils.getFormatFromString(result.getString(result.getColumnIndex(DBConstants.COMIC_FORMAT))));
        comic.setComicCoverImage(result.getString(result.getColumnIndex(DBConstants.COMIC_COVER_IMAGE)));
        comic.setComicNotes(result.getString(result.getColumnIndex(DBConstants.COMIC_NOTES)));

        //Creators
        comic.setComicCreators(getComicCreators(comicId));
        result.close();
        return comic;
    }

    //Get all publishers from database
    public List<String> getAllPublishers() {
        List<String> publishers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor result = db.rawQuery("Select * from " + DBConstants.PUBLISHER_TABLE + " ORDER BY " + DBConstants.PUBLISHER_NAME + " ASC", null);

        if(result.getCount() == 0) {
            return publishers;
        }

        result.moveToFirst();
        for(int i = 0; i < result.getCount(); i++) {
            //String publisher = "";
            String publisher = result.getString(result.getColumnIndex(DBConstants.PUBLISHER_NAME));
            publishers.add(publisher);
            result.moveToNext();
        }
        result.close();
        return publishers;
    }

    //Add a publisher
    public boolean addPublisher(String publisherName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long result;

        contentValues.put(DBConstants.PUBLISHER_NAME, publisherName);

        result = db.insert(DBConstants.PUBLISHER_TABLE, null, contentValues);
        return result > 0;
    }

    //Add a comic
    public boolean addComic(Comic comic) {
        SQLiteDatabase db = this.getWritableDatabase();

        //long comicID = -1;

        //Add the comic
        long comicID = addNewComic(comic, db);

        if(comicID == -1) {
            return false;
        }

        //Add the creators
        List<Creator> addedCreators = addNewCreators(comic.getComicCreators());
        comic.setComicCreators(addedCreators);

        //Add the issue/creator/job entry
        for(int i = 0; i < comic.getComicCreators().size(); i++) {
            addIssueJobCreator(comic.getComicCreators().get(i), (int)comicID, db);
        }

        return true;
    }

    //Update a comic
    public boolean updateComic(Comic comic) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = DBUtils.createComicContentValues(comic);

        //Update the comic listing
        int comicResult = updateComicEntry(contentValues, db, comic.getComicID());
        if(comicResult == 0) {
            //Nothing got updated. Something happened
            return false;
        }

        //Check if the creators are already stored in the db. Get their IDs or add them
        List<Creator> creators = addNewCreators(comic.getComicCreators());
        comic.setComicCreators(creators);

        //Remove all the existing creators for the issue
        db.delete(DBConstants.CREATOR_JOB_ISSUE_TABLE, DBConstants.COMIC_ID_FK + " = " + comic.getComicID(), null);

        //Add them back in
        for(int i = 0; i < comic.getComicCreators().size(); i++) {
            addIssueJobCreator(comic.getComicCreators().get(i), comic.getComicID(), db);
        }
        return true;
    }

    //Search by title
    public List<Comic> searchByTitle(String title) {
        List<Comic> comicList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor result = db.rawQuery("select * from " + DBConstants.COMIC_TABLE + " where " + DBConstants.COMIC_TITLE + " like '%" + title + "%'" + DBConstants.SEARCH_ORDER_BY, null);
        if(result.getCount() == 0) {
            result.close();
            return comicList;
        }
        comicList = DBUtils.generateComicListFromResult(result);
        result.close();
        return comicList;
    }

    //Search by publisher
    public List<Comic> searchByPublisher(String publisher) {
        List<Comic> comicList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor pubResult = db.rawQuery("select " + DBConstants.PUBLISHER_ID + " from " + DBConstants.PUBLISHER_TABLE + " where " + DBConstants.PUBLISHER_NAME + " like '%" + publisher + "%'", null);
        if(pubResult.getCount() == 0) {
            pubResult.close();
            return comicList;
        }

        pubResult.moveToFirst();
        int pubId = pubResult.getInt(pubResult.getColumnIndex(DBConstants.PUBLISHER_ID));
        pubResult.close();

        Cursor result = db.rawQuery("select * from " + DBConstants.COMIC_TABLE + " where " + DBConstants.COMIC_PUBLISHER + " = " + pubId + DBConstants.SEARCH_ORDER_BY, null);
        if(result.getCount() == 0) {
            result.close();
            return comicList;
        }

        result.moveToFirst();
        comicList = DBUtils.generateComicListFromResult(result);
        result.close();
        return comicList;
    }

    //Search for comics by their format
    public List<Comic> searchByFormat(String format) {
        List<Comic> comicList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor result = db.rawQuery("select * from " + DBConstants.COMIC_TABLE + " where " + DBConstants.COMIC_FORMAT + " like '%" + format + "%'" + DBConstants.SEARCH_ORDER_BY, null);
        if(result.getCount() == 0) {
            result.close();
            return comicList;
        }

        result.moveToFirst();
        comicList = DBUtils.generateComicListFromResult(result);
        result.close();
        return comicList;
    }

    //Search for comics based on their creators
    public List<Comic> searchByCreator(String creatorName) {
        List<Comic> comicList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        //List of comic issues with a creator name similar to what was passed in
        Cursor creatorResult = db.rawQuery("select * from " + DBConstants.CREATOR_TABLE + " left join " + DBConstants.CREATOR_JOB_ISSUE_TABLE + " on " + DBConstants.CREATOR_TABLE + "." + DBConstants.CREATOR_ID +
                " = " + DBConstants.CREATOR_JOB_ISSUE_TABLE + "." + DBConstants.CREATOR_ID_FK + " where " + DBConstants.CREATOR_TABLE + "." + DBConstants.CREATOR_NAME + " like '%" + creatorName + "%'", null);
        if(creatorResult.getCount() == 0) {
            return comicList;
        }

        creatorResult.moveToFirst();
        List<Integer> comicIds = new ArrayList<>();
        for(int i = 0; i < creatorResult.getCount(); i++) {
            comicIds.add(creatorResult.getInt(creatorResult.getColumnIndex(DBConstants.COMIC_ID_FK)));
            creatorResult.moveToNext();
        }

        String whereRange = DBUtils.generateRangeValues(comicIds);
        Cursor result = db.rawQuery("select * from " + DBConstants.COMIC_TABLE + " where " + DBConstants.COMIC_ID + " in " + whereRange + DBConstants.SEARCH_ORDER_BY, null);
        if(result.getCount() == 0) {
            result.close();
            return comicList;
        }

        result.moveToFirst();
        comicList = DBUtils.generateComicListFromResult(result);
        creatorResult.close();
        result.close();
        return comicList;
    }

    //Search for a creator

    //Get all creators
    public List<String> getAllCreators() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> creatorNames = new ArrayList<>();

        Cursor result = db.rawQuery("select * from " + DBConstants.CREATOR_TABLE, null);
        if(result.getCount() == 0) {
            return creatorNames;
        }

        result.moveToFirst();
        while(!result.isAfterLast()) {
            creatorNames.add(result.getString(result.getColumnIndex(DBConstants.CREATOR_NAME)));
            result.moveToNext();
        }
        result.close();
        return creatorNames;
    }

    //Add a creator
    public long addNewCreator(String creatorName, SQLiteDatabase db) {
        //long creatorID = -1;
        ContentValues contentValues = DBUtils.createCreatorContentValues(creatorName);
        //long creatorID = db.insert(DBConstants.CREATOR_TABLE, null, contentValues);
        //return creatorID;
        return db.insert(DBConstants.CREATOR_TABLE, null, contentValues);
    }

    //Get all jobs

    //Add a job
    public long addNewJob(String jobName, SQLiteDatabase db) {
        //long jobID = -1;
        ContentValues contentValues = DBUtils.createJobContentValues(jobName);
        //jobID = db.insert(DBConstants.JOB_TABLE, null, contentValues);
        //return jobID;
        return db.insert(DBConstants.JOB_TABLE, null, contentValues);
    }

    //Add a comic/creator/job entry

    //Get creators for comic
    public List<Creator> getComicCreators(int comicId) {
        List<Creator> creators = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select * from " + DBConstants.CREATOR_JOB_ISSUE_TABLE + " left join " + DBConstants.CREATOR_TABLE + " on " + DBConstants.CREATOR_JOB_ISSUE_TABLE + "." +
                DBConstants.CREATOR_ID_FK + " = " + DBConstants.CREATOR_ID + " left join " + DBConstants.JOB_TABLE + " on " + DBConstants.JOB_TABLE + "." + DBConstants.JOB_ID + " = " + DBConstants.CREATOR_JOB_ISSUE_TABLE +
                "." + DBConstants.JOB_ID_FK + " where " + DBConstants.CREATOR_JOB_ISSUE_TABLE + "." + DBConstants.COMIC_ID_FK + " = " + comicId, null);

        if(result.getCount() == 0) {
            result.close();
            return creators;
        }

        result.moveToFirst();
        while (!result.isAfterLast()) {
            Creator creator = new Creator();
            creator.setCreatorID(result.getInt(result.getColumnIndex(DBConstants.CREATOR_ID_FK)));
            creator.setCreatorName(result.getString(result.getColumnIndex(DBConstants.CREATOR_NAME)));
            creator.setCreatorJobID(result.getInt(result.getColumnIndex(DBConstants.JOB_ID)));
            creator.setCreatorJob(result.getString(result.getColumnIndex(DBConstants.JOB_NAME)));
            creators.add(creator);
            result.moveToNext();
        }
        result.close();
        return creators;
    }

    //Delete a comic's issue from the database and remove the associated creator/job/issue record(s)
    public boolean deleteComic(int comicId) {
        SQLiteDatabase db = this.getWritableDatabase();

        long comicResult = db.delete(DBConstants.COMIC_TABLE, DBConstants.COMIC_ID + " = " + comicId, null);
        long creatorResult = db.delete(DBConstants.CREATOR_JOB_ISSUE_TABLE, DBConstants.COMIC_ID_FK + " = " + comicId, null);
        return comicResult > 0 && creatorResult > 0;
    }

    //Get all the wishlist items currently stored
    public List<WishlistItem> getAllWishlistItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<WishlistItem> wishlistItems = new ArrayList<>();

        //Get the publisher info for later
        //ToDo: test this again
        HashMap<Integer, String> publisherInfo = getPublisherHashMap(db);

        Cursor result = db.rawQuery("select * from " + DBConstants.WISHLIST_TABLE + " order by " + DBConstants.WISHLIST_TITLE + ", " + DBConstants.WISHLIST_ISSUE, null);

        if(result.getCount() == 0) {
            return wishlistItems;
        }

        result.moveToFirst();
        while(!result.isAfterLast()) {
            WishlistItem wishlistItem = new WishlistItem();
            wishlistItem.setComicTitle(result.getString(result.getColumnIndex(DBConstants.WISHLIST_TITLE)));
            wishlistItem.setComicIssue(result.getString(result.getColumnIndex(DBConstants.WISHLIST_ISSUE)));
            wishlistItem.setWishlistPriority(result.getString(result.getColumnIndex(DBConstants.WISHLIST_PRIORITY)));
            wishlistItem.setWishlistID(result.getInt(result.getColumnIndex(DBConstants.WISHLIST_ID)));
            wishlistItem.setComicPublisherName(publisherInfo.get(result.getInt(result.getColumnIndex(DBConstants.WISHLIST_PUBLISHER_FK))));
            wishlistItems.add(wishlistItem);
            result.moveToNext();
        }

        result.close();
        return wishlistItems;
    }

    //Add a new wishlist item
    public boolean addWishlistItem(WishlistItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.WISHLIST_TITLE, item.getComicTitle());
        contentValues.put(DBConstants.WISHLIST_ISSUE, item.getComicIssue());
        contentValues.put(DBConstants.WISHLIST_PRIORITY, item.getWishlistPriority());
        contentValues.put(DBConstants.WISHLIST_PUBLISHER_FK, getPublisherID(item.getComicPublisherName(), db));

        long result = db.insert(DBConstants.WISHLIST_TABLE, null, contentValues);
        return result > 0;
    }

    //Delete wishlist item(s). Returns true if everything deleted successfully
    public boolean deleteWishlistItems(List<WishlistItem> wishlistItems) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean failure = false;

        for (WishlistItem item: wishlistItems) {
            //Delete the item
            long result = db.delete(DBConstants.WISHLIST_TABLE, DBConstants.WISHLIST_ID + " = " + item.getWishlistID(), null);

            //If it fails, set failure to true
            if (result < 0) {
                failure = true;
            }
        }

        return !failure;
    }

    //Update the wishlist item
    public boolean updateWishlistItem(WishlistItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.WISHLIST_TITLE, item.getComicTitle());
        contentValues.put(DBConstants.WISHLIST_ISSUE, item.getComicIssue());
        contentValues.put(DBConstants.WISHLIST_PUBLISHER_FK, getPublisherID(item.getComicPublisherName(), db));
        contentValues.put(DBConstants.WISHLIST_PRIORITY, item.getWishlistPriority());

        int result = db.update(DBConstants.WISHLIST_TABLE, contentValues, DBConstants.WISHLIST_ID + " = " + item.getWishlistID(), null);
        return result > 0;
    }

    //Transfer wishlist item to collection
    public boolean transferWishlistItems(List<WishlistItem> transferItems) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean failure = false;

        for(WishlistItem item: transferItems) {
            //Start a transaction
            try {
                db.beginTransaction();

                boolean added = addComic(item);
                boolean deleted = deleteWishlistItems(Collections.singletonList(item));

                if(added && deleted) {
                    //Both succeeded
                    db.setTransactionSuccessful();
                } else {
                    failure = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
        }
        return !failure;
    }

    //Get all Weekly List items
    public List<WeeklyItem> getAllWeeklyListItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<WeeklyItem> weeklyItems = new ArrayList<>();

        Cursor result = db.rawQuery("select * from " + DBConstants.WEEKLY_LIST_TABLE + " order by " + DBConstants.WEEKLY_DATE_PUBLISHED + ", " + DBConstants.WEEKLY_TITLE + ", " + DBConstants.WEEKLY_ISSUE, null);
        if(result.getCount() == 0) {
            return weeklyItems;
        }

        HashMap<Integer, String> publisherInfo = getPublisherHashMap(db);

        result.moveToFirst();
        while(!result.isAfterLast()) {
            WeeklyItem item = new WeeklyItem();
            item.setComicTitle(result.getString(result.getColumnIndex(DBConstants.WEEKLY_TITLE)));
            item.setComicIssue(result.getString(result.getColumnIndex(DBConstants.WEEKLY_ISSUE)));
            item.setWeeklyId(result.getInt(result.getColumnIndex(DBConstants.WEEKLY_ID)));
            item.setComicPublisherName(publisherInfo.get(result.getInt(result.getColumnIndex(DBConstants.WEEKLY_PUBLISHER))));
            item.setDatePublished(result.getLong(result.getColumnIndex(DBConstants.WEEKLY_DATE_PUBLISHED)));
            weeklyItems.add(item);
            result.moveToNext();
        }

        result.close();
        return weeklyItems;
    }

    //Add a new weekly item
    public boolean addWeeklyItem(WeeklyItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        //ContentValues contentValues = new ContentValues();
        ContentValues contentValues = DBUtils.createWeeklyListContentValues(item);

        long result = db.insert(DBConstants.WEEKLY_LIST_TABLE, null, contentValues);
        return result > 0;
    }

    public boolean updateWeeklyItem(WeeklyItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = DBUtils.createWeeklyListContentValues(item);
        int result = db.update(DBConstants.WEEKLY_LIST_TABLE, contentValues, DBConstants.WEEKLY_ID + " = " + item.getWeeklyId(), null);
        return result > 0;
    }

    public boolean deleteWeeklyListItems(List<WeeklyItem> weeklyItems) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean failure = false;

        for(WeeklyItem item: weeklyItems) {
            long result = db.delete(DBConstants.WEEKLY_LIST_TABLE, DBConstants.WEEKLY_ID + " = " + item.getWeeklyId(), null);

            //If it fails, set failure to true
            if (result < 0) {
                failure = true;
            }
        }
        return !failure;
    }

    public boolean transferWeeklyItems(List<WeeklyItem> selectedItems) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean failure = false;

        for(WeeklyItem item: selectedItems) {
            try {
                db.beginTransaction();

                boolean added = addComic(item);
                boolean deleted = deleteWeeklyListItems(Collections.singletonList(item));

                if(added && deleted) {
                    db.setTransactionSuccessful();
                } else {
                    failure = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
        }
        return !failure;
    }

    public List<Achievement> getAllOrderedAchievements() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Achievement> achievements = new ArrayList<>();

        Cursor result = db.rawQuery("select * from " + DBConstants.ACHIEVEMENT_TABLE + " ORDER BY " + DBConstants.ACHIEVEMENT_UNLOCKED + " DESC", null);

        if(result.getCount() == 0) {
            result.close();
            return achievements;
        }

        result.moveToFirst();
        while(!result.isAfterLast()) {
            Achievement cheevo = new Achievement();
            cheevo.setAchievementID(result.getInt(result.getColumnIndex(DBConstants.ACHIEVEMENT_ID)));
            cheevo.setAchievementName(result.getString(result.getColumnIndex(DBConstants.ACHIEVEMENT_NAME)));
            cheevo.setAchievementImage(result.getString(result.getColumnIndex(DBConstants.ACHIEVEMENT_IMAGE)));
            cheevo.setAchievementUnlocked(result.getInt(result.getColumnIndex(DBConstants.ACHIEVEMENT_UNLOCKED)) == 1);
            cheevo.setAchievementDateUnlocked(result.getLong(result.getColumnIndex(DBConstants.ACHIEVEMENT_DATE)));
            cheevo.setAchievementDescription(result.getString(result.getColumnIndex(DBConstants.ACHIEVEMENT_DESC)));
            cheevo.setTotal(result.getInt(result.getColumnIndex(DBConstants.ACHIEVEMENT_TOTAL)));
            cheevo.setProgress(result.getInt(result.getColumnIndex(DBConstants.ACHIEVEMENT_PROGRESS)));
            achievements.add(cheevo);
            result.moveToNext();
        }

        result.close();
        return achievements;
    }

    //**********************************************************************************************
    private long addNewComic(Comic comic, SQLiteDatabase db) {
        comic.setComicPublisherID((int)getPublisherID(comic.getComicPublisherName(), db));

        ContentValues contentValues = DBUtils.createComicContentValues(comic);

        return db.insert(DBConstants.COMIC_TABLE, null, contentValues);
    }

    private int updateComicEntry(ContentValues contentValues, SQLiteDatabase db, int comicID) {
        return db.update(DBConstants.COMIC_TABLE, contentValues, DBConstants.COMIC_ID + " = " + comicID, null);
    }

    private long getPublisherID(String publisherName, SQLiteDatabase db) {

        Cursor result = db.rawQuery("select * from " + DBConstants.PUBLISHER_TABLE + " where " + DBConstants.PUBLISHER_NAME + " = '" + publisherName + "'", null);
        if (result.getCount() == 0) {
            return -1;
        }

        result.moveToFirst();
        long pubID = result.getLong(result.getColumnIndex(DBConstants.PUBLISHER_ID));
        result.close();
        return pubID;
    }

    private List<Creator> addNewCreators(List<Creator> creatorList) {
        SQLiteDatabase db = this.getWritableDatabase();

        for(int i = 0; i < creatorList.size(); i++) {
            //Check if creator exists
            long creatorID = findExistingCreator(creatorList.get(i).getCreatorName(), db);
            if(creatorID == -1) {
                //Need to add it
                creatorID = addNewCreator(creatorList.get(i).getCreatorName(), db);
            }
            creatorList.get(i).setCreatorID((int)creatorID);

            //Now do the same for job
            long jobID = findExistingJob(creatorList.get(i).getCreatorJob(), db);
            if(jobID == -1) {
                //Add it
                jobID = addNewJob(creatorList.get(i).getCreatorJob(), db);
            }
            creatorList.get(i).setCreatorJobID((int)jobID);
        }
        return creatorList;
    }

    private long findExistingCreator(String creatorName, SQLiteDatabase db) {
        long creatorid = -1;
        Cursor result = db.rawQuery("select * from " + DBConstants.CREATOR_TABLE + " where " + DBConstants.CREATOR_NAME + " = '" + creatorName + "'", null);

        if(result.getCount() == 0) {
            return creatorid;
        }

        result.moveToFirst();
        creatorid = result.getLong(result.getColumnIndex(DBConstants.CREATOR_ID));
        result.close();
        return creatorid;
    }

    private long findExistingJob(String jobName, SQLiteDatabase db) {
        long jobID = -1;
        Cursor result = db.rawQuery("select * from " + DBConstants.JOB_TABLE + " where " + DBConstants.JOB_NAME + " = '" + jobName + "'", null);

        if(result.getCount() == 0) {
            return jobID;
        }

        result.moveToFirst();
        jobID = result.getLong(result.getColumnIndex(DBConstants.JOB_ID));
        result.close();
        return jobID;
    }

    private long addIssueJobCreator(Creator creator, int comicID, SQLiteDatabase db) {
        ContentValues contentValues = DBUtils.createIssueJobContentValues(creator, comicID);

        return db.insert(DBConstants.CREATOR_JOB_ISSUE_TABLE, null, contentValues);
    }

    private HashMap<Integer, String> getPublisherHashMap(SQLiteDatabase db) {
        Cursor publishers = db.rawQuery("select * from " + DBConstants.PUBLISHER_TABLE, null);
        HashMap<Integer, String> publisherInfo = new HashMap<>();
        publishers.moveToFirst();
        while(!publishers.isAfterLast()) {
            publisherInfo.put(publishers.getInt(publishers.getColumnIndex(DBConstants.PUBLISHER_ID)), publishers.getString(publishers.getColumnIndex(DBConstants.PUBLISHER_NAME)));
            publishers.moveToNext();
        }
        publishers.close();
        return publisherInfo;
    }
}
