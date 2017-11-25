package com.oxilo.mrsafer.utility;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ericbasendra on 06/07/16.
 */

public class FileUtils {
    static String mCurrentPhotoPath;

    static String mCurrentVideoPath;

    public static String getmCurrentVideoPath() {
        return mCurrentVideoPath;
    }

    public static String getmCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    public static void setmCurrentPhotoPath(String mCurrentPhotoPath) {
        FileUtils.mCurrentPhotoPath = mCurrentPhotoPath;
    }

    public static void setmCurrentVideoPath(String mCurrentVideoPath) {
        FileUtils.mCurrentVideoPath = mCurrentVideoPath;
    }

    public static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()){
            storageDir.mkdir();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        setmCurrentPhotoPath(mCurrentPhotoPath);
        return image;
    }

    public static File createVideoFile() throws IOException {
        // Create an video file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "VID_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()){
            storageDir.mkdir();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".mp4",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentVideoPath = image.getAbsolutePath();
        setmCurrentVideoPath(mCurrentVideoPath);
        return image;
    }

}
