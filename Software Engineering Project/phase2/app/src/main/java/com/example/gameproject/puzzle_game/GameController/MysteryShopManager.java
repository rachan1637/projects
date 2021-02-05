package com.example.gameproject.puzzle_game.GameController;

import android.content.Context;
import android.widget.Toast;

import com.example.gameproject.puzzle_game.GameElements.PuzzleGameDataGateway;

class MysteryShopManager {
    static final String smallHint = "SMALL_HINT";
    static final String bigHint = "bigHint";
    static final String skipPuzzle = "SKIP_PUZZLE";

    private PuzzleRequester puzzleRequester;
    private PuzzleGameDataGateway puzzleGameDataGateway;
    private PuzzleGenerator puzzleGenerator;
    private PuzzleListManager puzzleListManager;
    private Context context;

    MysteryShopManager(PuzzleRequester puzzleRequester, PuzzleGameDataGateway
            puzzleGameDataGateway, PuzzleGenerator puzzleGenerator, Context context,
                       PuzzleListManager puzzleListManager) {
        this.puzzleRequester = puzzleRequester;
        this.puzzleGameDataGateway = puzzleGameDataGateway;
        this.puzzleGenerator = puzzleGenerator;
        this.context = context;
        this.puzzleListManager = puzzleListManager;
    }


    void buyItem(String item) {
        int smallHintCost = 600;
        int bigHintCost = 1400;
        switch (item) {
            case smallHint:
                buyHint(1, smallHintCost);
                break;
            case bigHint:
                buyHint(3, bigHintCost);
                break;
            case skipPuzzle:
                skipPuzzle();
                break;
            default:
                break;
        }
    }

    private void buyHint(int hintNum, int cost) {
        int currHintNum = 0;
        int score = puzzleGameDataGateway.getScore();
        if (score >= cost) {
            String[] tileList = puzzleGenerator.getCurrentPosition();
            for (int i = 0; i < tileList.length; i++) {
                if (!tileList[i].equals(String.valueOf(i))) {
                    for (int j = 0; j < tileList.length; j++) {
                        if (tileList[j].equals((String.valueOf(i)))) {
                            String temp = tileList[i];
                            tileList[i] = tileList[j];
                            tileList[j] = temp;
                            currHintNum++;
                        }
                    }
                }
                if (currHintNum == hintNum) {
                    break;
                }
            }
            score -= cost;
            puzzleGameDataGateway.setScore(score);
            puzzleRequester.updatePuzzle();
        } else {
            Toast.makeText(context, "Not Enough Score!", Toast.LENGTH_SHORT).show();
        }
    }

    private void skipPuzzle() {
        int numCompleted = puzzleGameDataGateway.getNumCompleted();
        int numSkipped = puzzleGameDataGateway.getNumSkipped();
        int numPuzzles = puzzleGameDataGateway.getNumPuzzles();
        int score = puzzleGameDataGateway.getScore();
        int skipPuzzleCost = 800;
        if (score >= skipPuzzleCost){
            if (numCompleted + numSkipped < numPuzzles) {
                puzzleListManager.showNextPuzzle(numCompleted + numSkipped);
                puzzleGameDataGateway.updateNumSkipped();
                score -= skipPuzzleCost;
                puzzleGameDataGateway.setScore(score);
            } else {
                Toast.makeText(context, "NO NEW PUZZLES, SORRY!", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(context, "Not Enough Score!", Toast.LENGTH_SHORT).show();
        }
    }

}
