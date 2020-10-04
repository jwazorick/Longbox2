package com.example.longbox2.Database;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.longbox2.Objects.Comic;
import com.example.longbox2.Objects.Creator;
import com.example.longbox2.Utils.EnumUtils;

import java.util.ArrayList;
import java.util.List;

class DBUtils {
    public static ContentValues createComicContentValues(Comic comic) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBConstants.COMIC_TITLE, comic.getComicTitle());
        contentValues.put(DBConstants.COMIC_VOLUME, comic.getComicVolume());
        contentValues.put(DBConstants.COMIC_ISSUE, comic.getComicIssue());
        contentValues.put(DBConstants.COMIC_PUBLISHER, comic.getComicPublisherID());
        contentValues.put(DBConstants.COMIC_COVER_PRICE, comic.getComicCoverPrice());
        contentValues.put(DBConstants.COMIC_COVER_IMAGE, comic.getComicCoverImage());
        contentValues.put(DBConstants.COMIC_CONDITION, comic.getComicConditionText());
        contentValues.put(DBConstants.COMIC_NOTES, comic.getComicNotes());
        contentValues.put(DBConstants.COMIC_FORMAT, comic.getComicFormat().toString());

        return contentValues;
    }

    public static ContentValues createCreatorContentValues(String creatorName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.CREATOR_NAME, creatorName);
        return contentValues;
    }

    public static ContentValues createJobContentValues(String jobName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.JOB_NAME, jobName);
        return contentValues;
    }

    public static ContentValues createIssueJobContentValues(Creator creator, int comicID) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.CREATOR_ID_FK, creator.getCreatorID());
        contentValues.put(DBConstants.JOB_ID_FK, creator.getCreatorJobID());
        contentValues.put(DBConstants.COMIC_ID_FK, comicID);
        return contentValues;
    }

    public static List<Comic> generateComicListFromResult(Cursor result) {
        List<Comic> comicList = new ArrayList<>();

        result.moveToFirst();
        for(int i = 0; i < result.getCount(); i++) {
            Comic comic = new Comic();

            comic.setComicID(result.getInt(result.getColumnIndex(DBConstants.COMIC_ID)));
            comic.setComicTitle(result.getString(result.getColumnIndex(DBConstants.COMIC_TITLE)));
            comic.setComicVolume(result.getString(result.getColumnIndex(DBConstants.COMIC_VOLUME)));
            comic.setComicIssue(result.getString(result.getColumnIndex(DBConstants.COMIC_ISSUE)));
            comic.setComicPublisherID(result.getInt(result.getColumnIndex(DBConstants.COMIC_PUBLISHER)));
            comic.setComicCoverPrice(result.getString(result.getColumnIndex(DBConstants.COMIC_COVER_PRICE)));
            comic.setComicCoverImage(result.getString(result.getColumnIndex(DBConstants.COMIC_COVER_IMAGE)));
            comic.setComicConditionText(result.getString(result.getColumnIndex(DBConstants.COMIC_CONDITION)));
            comic.setComicNotes(result.getString(result.getColumnIndex(DBConstants.COMIC_NOTES)));
            comic.setComicFormat(EnumUtils.getFormatFromString(result.getString(result.getColumnIndex(DBConstants.COMIC_FORMAT))));
            comicList.add(comic);
            result.moveToNext();
        }
        return comicList;
    }

    public static String generateRangeValues(List<Integer> comicIds) {
        String range = "(";
        range += comicIds.get(0).toString();

        for(int i = 1; i < comicIds.size(); i++) {
            range += ", " + comicIds.get(i).toString();
        }
        range += ")";
        return range;
    }

}
