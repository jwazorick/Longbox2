package com.wazorick.longbox2.Utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

public class CameraHelper {
    public static boolean checkIfCameraAvailable(Context context) {
        PackageManager packageManager = context.getPackageManager();
        if(!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            Toast.makeText(context, "This device has no camera", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static File createImage(Context context) throws Exception{
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(uuid, ".jpg", storageDir);
    }

    public static void compressImage(String imageLocation){
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bmp = BitmapFactory.decodeFile(imageLocation, options);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            int newWidth = Math.round((float) options.outWidth *.20f);
            int newHeight = Math.round((float) options.outHeight * .20f);
            Bitmap newBitmap = Bitmap.createScaledBitmap(bmp, newWidth, newHeight, true);

            FileOutputStream image = new FileOutputStream(imageLocation);
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 85, image);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean saveImage(Bitmap bitmap, String imageLocation, Context context) {
        try {
            FileOutputStream fs = new FileOutputStream(new File(imageLocation));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fs);
            fs.flush();
            fs.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Bitmap rotateBitmapIfNecessary(Bitmap img) {
        if(img.getWidth() > img.getHeight()) {
            return rotateImage(img, 90);
        }
        return img;
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }
}
