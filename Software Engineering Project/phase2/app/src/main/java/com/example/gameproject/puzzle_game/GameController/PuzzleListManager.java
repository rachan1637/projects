package com.example.gameproject.puzzle_game.GameController;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import com.example.gameproject.R;

import java.util.ArrayList;

class PuzzleListManager {
    private ArrayList<Bitmap> puzzles;
    private Resources resources;
    private PuzzleRequester puzzleRequester;
    private PuzzleGenerator puzzleGenerator;
    private ImageSplitter imageSplitter;

    void setPuzzles(ArrayList<Bitmap> puzzles) {
        this.puzzles = puzzles;
        if (this.puzzles.size() == 0) {
            addDefaultImagesToList(this.puzzles);
        }
    }

    PuzzleListManager(Resources resources) {
        this.resources = resources;
    }

    private void addDefaultImagesToList(ArrayList<Bitmap> puzzles) {
        Bitmap default1 = BitmapFactory.decodeResource(resources,
                R.drawable.default1);
        puzzles.add(default1);
        Bitmap default2 = BitmapFactory.decodeResource(resources,
                R.drawable.default2);
        puzzles.add(default2);
    }

    void setPuzzleRequester(PuzzleRequester puzzleRequester) {
        this.puzzleRequester = puzzleRequester;
    }

    void setPuzzleGenerator(PuzzleGenerator puzzleGenerator) {
        this.puzzleGenerator = puzzleGenerator;
    }

    void setImageSplitter(int numColumns) {
        imageSplitter = new ImageSplitter(numColumns);
    }

    /**
     * show the next puzzle.
     */
    void showNextPuzzle(int numCompleted) {
        puzzleGenerator.randomize();
        Bitmap nextPuzzle = puzzles.get(numCompleted);
        ArrayList<Bitmap> dividedImages = imageSplitter.splitImage(nextPuzzle);
        BitmapDrawable[] dividedDrawableImages = new BitmapDrawable[dividedImages.size()];
        for (int i = 0; i < dividedImages.size(); i++) {
            BitmapDrawable bDrawable = new BitmapDrawable(resources,dividedImages.get(i));
            dividedDrawableImages[i] = bDrawable;
        }
        puzzleRequester.setPuzzlePieces(dividedDrawableImages);
        puzzleRequester.updatePuzzle();
    }
}
