package com.example.gameproject.obstacle_game.GameElements;

public class TreasuryBoxGenerator extends ObstacleGenerator {
    private int maxBoxes;
    private int currentBoxNum = 0;
    private int generationDelay = 0;
    private int currentTime = 0;

    public TreasuryBoxGenerator(int width, int height, int maxBoxes) {
        super(width, height, width / 15, height / 15, width / 100, 30 * 8 * width / 100, 30 * 10 * width / 100);
        this.generationLine = 30 * 10 * width / 100 + 10;
        this.maxBoxes = maxBoxes;
    }

    @Override
    public Obstacle checkGeneration() {
        if (currentTime < generationDelay) {
            currentTime++;
            return null;
        } else {
            Obstacle result = super.checkGeneration();
            if (result != null) {
                currentBoxNum++;
            }
            if (currentBoxNum > maxBoxes) {
                return null;
            } else {
                return result;
            }
        }
    }

}
