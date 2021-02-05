package com.example.gameproject.puzzle_game.GameController;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Class responsible for cutting ImageView images
 */
public class ImageSplitter {
    private int numColumns;

    public ImageSplitter(int numColumns) {
        this.numColumns = numColumns;
    }

    /**
     * Splits the source image and show them all into a grid in a new activity
     *
     * @param image        The source image to split.
     * @return A bitmap ArrayList of divided images.
     */
    public ArrayList<Bitmap> splitImage(Bitmap image) {

        //For height and width of the small image chunks
        int chunkLength;

        //To store all the small image chunks in bitmap format in this list
        ArrayList<Bitmap> dividedImages = new ArrayList<>(numColumns * numColumns);

        //Getting the scaled bitmap of the source image
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(image, image.getWidth(),
                image.getHeight(), true);
        chunkLength = image.getHeight() / numColumns;
        //xCoordinate and yCoordinate are the pixel positions of the image chunks
        int yCoordinate = 0;
        for (int x = 0; x < numColumns; x++) {
            int xCoordinate = 0;
            for (int y = 0; y < numColumns; y++) {
                dividedImages.add(Bitmap.createBitmap(scaledBitmap, xCoordinate, yCoordinate,
                        chunkLength, chunkLength));
                xCoordinate += chunkLength;
            }
            yCoordinate += chunkLength;
        }
        return dividedImages;
    }
}
