package com.example.gameproject.obstacle_game.GameElements;

import java.util.Random;

/**
 * A strategy of auto-generating obstacles
 */
public class ObstacleGenerator implements ItemGenerator<Obstacle> {
    /**
     * The x coordinate that all obstacles would generate (their left side) from.(presumably out of the screen)
     */
    int generationLine;
    /**
     * the height of the screen
     */
    private int screenHeight;
    /**
     * The minimum distance an obstacle can generate from the last obstacle
     */
    private int minDistance;
    /**
     * The maximum distance an obstacle can generate from the last obstacle
     */
    private int maxDistance;

    /**
     * The width of the obstacles we are generating
     */
    private int obstacleWidth;
    /**
     * The height of the obstacles we are generating
     */
    private int obstacleHeight;
    /**
     * the speed of the obstacles that we will generate
     */
    private int obstacleSpeed;
    /**
     * whether we have generated the first item or not
     */
    private boolean isFirstItem = true;
    /**
     * The last obstacle generated
     */
    private Obstacle lastObstacle;
    /**
     * the random Generator used
     */
    private Random randGenerator;

    /**
     * set up the generator by screen width and screen height, the distance and obstacle parameters
     * will be automatically set up, used for regular Obstacles
     *
     * @param screenWidth  The screen width
     * @param screenHeight The screen height
     */
    public ObstacleGenerator(int screenWidth, int screenHeight, int difficulty) {
        minDistance = screenWidth / (4 * difficulty);
        maxDistance = screenWidth / (2 * difficulty);
        obstacleWidth = screenWidth / 15;
        obstacleHeight = screenHeight / 15;
        generationLine = screenWidth * 2;
        this.screenHeight = screenHeight;
        this.obstacleSpeed = screenWidth / 100;
        randGenerator = new Random();
    }

    /**
     * set up the generator with all customizable parameters
     *
     * @param screenWidth    the screen width
     * @param screenHeight   the screen height
     * @param obstacleWidth  the width of each obstacle
     * @param obstacleHeight the height of each obstacle
     * @param minDistance    the minimum distance between obstacles
     * @param maxDistance    the maximum distance between obstacles
     */
    ObstacleGenerator(int screenWidth, int screenHeight, int obstacleWidth, int obstacleHeight, int obstacleSpeed, int minDistance, int maxDistance) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.obstacleWidth = obstacleWidth;
        this.obstacleHeight = obstacleHeight;
        generationLine = screenWidth * 2;
        this.obstacleSpeed = obstacleSpeed;
        this.screenHeight = screenHeight;
        randGenerator = new Random();
    }

    public Obstacle checkGeneration() {
        //generate the upper height of the rectangle, then use it to construct a new obstacle
        int randHeight = randGenerator.nextInt(screenHeight - obstacleHeight);
        //Obstacle testObstacle = new Obstacle(generationLine, randHeight, generationLine + obstacleWidth, randHeight + obstacleHeight, obstacleSpeed);
        if (isFirstItem) {
            lastObstacle = new Obstacle(generationLine, randHeight, generationLine + obstacleWidth, randHeight + obstacleHeight, obstacleSpeed);
            isFirstItem = false;
            return lastObstacle;
        } else {

            if (generationLine - lastObstacle.getX() >= minDistance) {
                // spawn obstacle immediately if max distance is reached
                if (generationLine - lastObstacle.getX() >= maxDistance) {
                    lastObstacle = new Obstacle(generationLine, randHeight, generationLine + obstacleWidth, randHeight + obstacleHeight, obstacleSpeed);
                    return lastObstacle;
                } else {
                    int roll = randGenerator.nextInt((maxDistance - minDistance) / obstacleSpeed);
                    if (roll == 1) {
                        lastObstacle = new Obstacle(generationLine, randHeight, generationLine + obstacleWidth, randHeight + obstacleHeight, obstacleSpeed);
                        return lastObstacle;
                    }
                }

            }

        }
        return null;
    }
}