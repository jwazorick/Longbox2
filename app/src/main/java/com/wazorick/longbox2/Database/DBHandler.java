package com.wazorick.longbox2.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wazorick.longbox2.Objects.Comic;
import com.wazorick.longbox2.Objects.Creator;
import com.wazorick.longbox2.Utils.EnumUtils;

import java.util.ArrayList;
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
                " TEXT, " + DBConstants.ACHIEVEMENT_UNLOCKED + " NUMERIC, " + DBConstants.ACHIEVEMENT_DATE + " INTEGER)");

        //Wishlist table
        db.execSQL("create table " + DBConstants.WISHLIST_TABLE + " (" + DBConstants.WISHLIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DBConstants.WISHLIST_TITLE + " TEXT, " + DBConstants.WISHLIST_ISSUE +
                " TEXT, " + DBConstants.WISHLIST_PUBLISHER_FK + " INTEGER, " + DBConstants.WISHLIST_PRIORITY + " TEXT)");

        //Weekly List table
        db.execSQL("create table " + DBConstants.WEEKLY_LIST_TABLE + " (" + DBConstants.WEEKLY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DBConstants.WEEKLY_TITLE + " TEXT, " + DBConstants.WEEKLY_ISSUE +
                " TEXT, " + DBConstants.WEEKLY_PUBLISHER + " INTEGER, " + DBConstants.WEEKLY_DATE_PUBLISHED + " INTEGER)");

        //ToDo: Load Settings, Condition, Achievement tables
        loadInitialPublishers(db);
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

        Cursor result = db.rawQuery("select * from " + DBConstants.COMIC_TABLE + " where " + DBConstants.COMIC_TITLE + " like '%" + title + "%'", null);
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

        Cursor result = db.rawQuery("select * from " + DBConstants.COMIC_TABLE + " where " + DBConstants.COMIC_PUBLISHER + " = " + pubId, null);
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

        Cursor result = db.rawQuery("select * from " + DBConstants.COMIC_TABLE + " where " + DBConstants.COMIC_FORMAT + " = '" + format + "'", null);
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
        Cursor result = db.rawQuery("select * from " + DBConstants.COMIC_TABLE + " where " + DBConstants.COMIC_ID + " in " + whereRange, null);
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
}
