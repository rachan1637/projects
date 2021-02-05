package com.example.gameproject.puzzle_game.GameController;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

/**
 * Database for storing customized images.
 */
class CustomImageInteractor{

    private static final String IMAGE_DIR_FILE_NAME = "customImages";

    /**
     * Save a list of Bitmap images each assigned a unique filename.
     * @param imageHashMap HashMap containing all images and their assigned filename.
     * @param context context from which the custom images file directory is retrieved/created.
     */
    static void saveImageList(HashMap<String, Bitmap> imageHashMap, Context context) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(IMAGE_DIR_FILE_NAME, Context.MODE_PRIVATE);
        for (String pathname : imageHashMap.keySet()) {
            String childPathname = pathname + ".jpg";
            File myPath = new File(directory, childPathname);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(myPath);
                // Use the compress method on the BitMap object to write image to the OutputStream
                Objects.requireNonNull(imageHashMap.get(pathname)).compress(
                        Bitmap.CompressFormat.JPEG, 100, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    assert fos != null;
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static void deleteImageList(String[] fileNameList, Context context) {

        File[] fileList = getFilesFromExternalStorage(fileNameList, context);
        for (File file : fileList) {
            file.delete();
        }
    }

    /**
     * Get a list of bitmap images using a list of file names.
     * @param pathNames an array of file names.
     * @param context context used to create/retrieve custom images file directory.
     * @return list of Bitmap images retrieved from the directory.
     */
    static Bitmap[] getImageList(String[] pathNames, Context context) {
        Bitmap[] imageList = new Bitmap[pathNames.length];
        File[] fileList = getFilesFromExternalStorage(pathNames, context);
        for (int i = 0; i < imageList.length; i++) {
            try {
                Bitmap image = BitmapFactory.decodeStream(new FileInputStream(fileList[i]));
                imageList[i] = image;
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
                imageList[i] = null;
            }
        }
        return imageList;
    }

    private static File[] getFilesFromExternalStorage(String[] pathNames, Context context) {
        for (int i = 0; i < pathNames.length; i++) {
            String childPathname = pathNames[i] + ".jpg";
            pathNames[i] = childPathname;
        }
        File[] fileList = new File[pathNames.length];
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(IMAGE_DIR_FILE_NAME, Context.MODE_PRIVATE);
        for (int i = 0; i < pathNames.length; i++) {
            fileList[i] = new File(directory, pathNames[i]);
        }
        return fileList;
    }
}
