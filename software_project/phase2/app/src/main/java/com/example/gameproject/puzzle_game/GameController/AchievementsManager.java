package com.example.gameproject.puzzle_game.GameController;

import com.example.gameproject.puzzle_game.GameElements.PuzzleGameDataGateway;

class AchievementsManager {

    private PuzzleGameDataGateway puzzleGameDataGateway;
    private CountDownGenerator countDownGenerator;

    AchievementsManager(PuzzleGameDataGateway puzzleGameDataGateway,
                        CountDownGenerator countDownGenerator) {
        this.puzzleGameDataGateway = puzzleGameDataGateway;
        this.countDownGenerator = countDownGenerator;
    }

    boolean checkTimeAchievement(){
        //Threshold time of solving one puzzle for getting time achievement
        long achievementTime = 30000;
        return countDownGenerator.getTotalTime() - countDownGenerator.getCurrentTime() <=
                achievementTime;
    }

    boolean checkLevelScoreAchievement(Integer currentPuzzleScore){
        //Threshold score of finishing one puzzle for getting level score achievement
        Integer levelScore = 70;
        return currentPuzzleScore >= levelScore;
    }

    boolean checkTotalScoreAchievement(){
        //Threshold score of finishing the game for getting total score achievement
        int totalScore = 150;
        return puzzleGameDataGateway.getScore() >= totalScore;
    }

}
