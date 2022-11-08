package com.example.gameproject.puzzle_game.GameElements;


/**
 * Gateway that separates the interactor from database.
 */
public interface PuzzleGameDataGateway {
    void updateNumMoves();
    void clearNumMoves();
    int getNumMoves();
    void setScore(int score);
    int getScore();
    void updateNumCompleted();
    void updateNumSkipped();
    int getNumCompleted();
    int getNumSkipped();
    void setNumPuzzles(int numPuzzles);
    int getNumPuzzles();
}
