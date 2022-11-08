package com.example.gameproject.puzzle_game.GameController;

import android.graphics.drawable.BitmapDrawable;

public interface PuzzleRequester {
    void setPuzzlePieces(BitmapDrawable[] dividedDrawableImages);
    void updatePuzzle();
}
