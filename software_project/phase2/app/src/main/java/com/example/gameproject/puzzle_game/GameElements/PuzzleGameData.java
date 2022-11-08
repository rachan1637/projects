package com.example.gameproject.puzzle_game.GameElements;

public class PuzzleGameData implements PuzzleGameDataGateway {

    //total number of puzzles in the game
    private int numPuzzles = 2;

    //number of puzzle completed
    private int numCompleted = 0;

    //number of puzzle skipped
    //starts with 1 since the one they are currently doing counts as skipped
    private int numSkipped = 1;

    //number of moves
    private int numMoves = 0;

    //current score
    private int score = 0;

    @Override
    public void updateNumMoves() {
        numMoves++;
    }

    @Override
    public void clearNumMoves() {
        numMoves = 0;
    }

    @Override
    public int getNumMoves() {
        return numMoves;
    }

    @Override
    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int getScore() {
        return this.score;
    }

    @Override
    public void updateNumCompleted() {
        numCompleted++;
    }

    @Override
    public void updateNumSkipped() { numSkipped++; }

    @Override
    public int getNumCompleted() {
        return numCompleted;
    }

    @Override
    public int getNumSkipped() { return numSkipped; }

    @Override
    public void setNumPuzzles(int numPuzzles) {
        this.numPuzzles = numPuzzles;
    }

    @Override
    public int getNumPuzzles() {
        return numPuzzles;
    }

}
