package com.example.gameproject.puzzle_game.GameController;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.UUID;

public class CustomImageManager {

    /**
     * save a list of bitmap images to external storage and get code for retrieving these images.
     * @param imageList list of bitmap images to save.
     * @param originalFileNames code for retrieving previously saved images.
     * @param context application context for getting directory.
     * @return a String of code for retrieving saved images.
     */
    public static String saveImageList(Bitmap[] imageList, String originalFileNames,
                                       Context context) {
        deleteImageList(originalFileNames, context);
        StringBuilder newFileNames;
        newFileNames = new StringBuilder();
        HashMap<String, Bitmap> imageHashMap = new HashMap<>();
        for (int i = 0; i < imageList.length; i++) {
            //String pathname = Integer.toString(i + numOriginalFiles);
            String uniqueId = UUID.randomUUID().toString();
            String pathname = uniqueId.replace("-", "");
            imageHashMap.put(pathname, imageList[i]);
            newFileNames.append(pathname);
            if (i < (imageList.length - 1)) {
                newFileNames.append("_");
            }
        }
        CustomImageInteractor.saveImageList(imageHashMap, context);
        return newFileNames.toString();
    }

    /**
     * delete files from external storage of the device.
     * @param fileNames code for accessing files.
     * @param context application context
     */
    private static void deleteImageList(String fileNames, Context context) {
        String[] fileNameList = decode(fileNames);
        CustomImageInteractor.deleteImageList(fileNameList, context);
    }

    /**
     * Get a list of bitmap images using a code and context that provides directory.
     * @param code encodes the path names of image files.
     * @param context application context.
     * @return list of retrieved bitmap images.
     */
    public static Bitmap[] getImageList(String code, Context context) {
        return CustomImageInteractor.getImageList(decode(code), context);
    }

    private static String[] decode(String code) {
        String[] pathnames;
        if (code == null) {
            pathnames = new String[0];
        } else {
            pathnames = code.split("_");
        }
        return pathnames;
    }
}
