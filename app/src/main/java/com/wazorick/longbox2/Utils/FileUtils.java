package com.wazorick.longbox2.Utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;

public class FileUtils {
    public static File getStorageDirectory(Activity activity) {
        return activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    }

    public static Bitmap getCoverImage(String coverName, Activity activity) {
        String coverLocation = getStorageDirectory(activity).getAbsolutePath() + "/" + coverName;
        return BitmapFactory.decodeFile(coverLocation);
    }
}
