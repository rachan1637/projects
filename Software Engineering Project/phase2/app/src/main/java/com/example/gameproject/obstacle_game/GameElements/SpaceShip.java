package com.example.gameproject.obstacle_game.GameElements;

import android.graphics.Rect;

public class SpaceShip extends SpaceItem {
    /**
     * The JumpHeight of this spaceship per frame of jumping.
     * (Still have to test after finish the coordinate system, not a certain value)
     */
    private int jumpHeight;
    /**
     * The units this ship would drop by default each frame
     */
    private int dropHeight;
    /**
     * The max number of frames this ship would keep jumping for
     */
    private int maxJumpDuration = 4;
    /**
     * The current number of frames this ship still have left to jump
     */
    private int jumpDurationLeft = 0;

    /**
     * The number of lives in a single game.
     */
    private int lives;

    /**
     * The boolean for checking whether the spaceship enter the invincible time.
     */
    private int invincibleTime = 0;

    /**
     * The time a spaceship can be out of screen.
     */
    private int outTime = 0;

    /**
     * The number of collections this spaceship collects in this game.
     */
    private int collection = 0;

    /**
     * The time to notify the spaceship gets a collection.
     */
    private int collectionTime = 0;

    /**
     * The score this player gets in this game.
     */
    private int score = 0;

    /**
     * The boolean checks whether the spaceship is Game Over or not.
     */
    private boolean gameOver = false;

    /**
     * Constructs a new Spaceship with default jumpHeight, dropHeight, maxJumpDuration.
     * (The default location should be somewhere in the middle width and low bottom of the screen.)
     */
    public SpaceShip(int lives, int screenWidth, int screenHeight) {
        super(new Rect(0, 0, screenWidth / 10, screenHeight / 50));
        setHitBoxTo(screenWidth / 10, screenHeight / 2);
        setHeight(screenHeight);
        setLives(lives);
    }

    public SpaceShip(int lives, int x, int screenWidth, int screenHeight) {
        super(new Rect(0, 0, screenWidth / 30, screenHeight / 50));
        setHitBoxTo(screenWidth / x, screenHeight / 2);
        setHeight(screenHeight);
        setLives(lives);
    }

    /**
     * Sets the height needed for spaceship based on the devices.
     *
     * @param screenHeight the height of device.
     */
    private void setHeight(int screenHeight) {
        jumpHeight = screenHeight / 30;
        dropHeight = screenHeight / 50;
    }

    /**
     * Gets the remaining invincible time.
     *
     * @return the remaining invincible time.
     */
    public int getInvincibleTime() {
        return invincibleTime;
    }

    /**
     * Sets the length of invincible time.
     *
     * @param i the length of time we want to set.
     */
    public void setInvincibleTime(int i) {
        invincibleTime = i;
    }

    /**
     * Gets the number of lives.
     *
     * @return the number of lives.
     */
    public int getLives() {
        return lives;
    }

    /**
     * Sets the number of lives.
     *
     * @param life the number of lives.
     */
    private void setLives(int life) {
        this.lives = life;
    }

    /**
     * Gets the number of collections.
     *
     * @return the number of collections.
     */
    public int getCollection() {
        return this.collection;
    }

    /**
     * Gets the time to display this spaceship gets a collection.
     *
     * @return the remaining time to display this spaceship gets a collection.
     */
    public int getCollectionTime() {
        return collectionTime;
    }

    /**
     * Sets the time to display this spaceship gets a collection.
     *
     * @param time the new time to display.
     */
    public void setCollectionTime(int time) {
        collectionTime = time;
    }

    /**
     * Gets the score of the spaceship.
     *
     * @return the score of the spaceship.
     */
    public int getScore() {
        return score;
    }

    /**
     * Updates the score of this spaceship.
     */
    public void updateScore() {
        score++;
    }

    /**
     * Checks whether the game is over
     *
     * @return the checker of game over.
     */
    public boolean getGameOver() {
        return gameOver;
    }

    /**
     * Updates the out time.
     *
     * @param time the new out time.
     */
    private void setOutTime(int time) {
        this.outTime = time;
    }

    /**
     * Gets the out time.
     *
     * @return the out time of this screen.
     */
    public int getOutTime() {
        return this.outTime;
    }


    /**
     * Checks whether the spaceship hits an obstacle.
     * If yes, then deduct the lives, enter invincible time and return true
     *
     * @param obstacle the obstacle to be checked.
     * @return true if the spaceship hits the obstacle. Otherwise, return false.
     */
    public boolean checkHit(Obstacle obstacle) {
        Rect r1 = this.getHitBox();
        Rect r2 = obstacle.getHitBox();
        if (r1.intersect(r2)) {
            lives--;
            setInvincibleTime(90);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks whether the spaceship gets a treasury box.
     *
     * @param treasuryBox the treasury box to be checked.
     * @return true if the spaceship gets the treasury box. Otherwise, return false.
     */
    public boolean checkGetTreasury(Obstacle treasuryBox) {
        Rect r1 = this.getHitBox();
        Rect r2 = treasuryBox.getHitBox();
        if (r1.intersect(r2)) {
            collection++;
            setCollectionTime(30 * 3);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks whether the game is over.
     */
    public void checkGameOver() {
        if (lives == 0 | outTime == 1) {
            gameOver = true;
        }
    }

    /**
     * Automatically the time which spaceship is out of screen.
     */
    public void outOfScreen() {
        int y = getY();
        if (y < 0 | y > jumpHeight * 30) {
            if (outTime == 0) {
                setOutTime(90);
            } else {
                setOutTime(outTime - 1);
            }
        } else {
            setOutTime(0);
        }
    }

    /**
     * Moves the spaceship upward for one unit.
     */
    private void moveUp() {
        setHitBoxTo(getX(), getY() - jumpHeight);
    }

    /**
     * Moves the spaceship downward for one unit.
     */

    private void moveDown() {
        setHitBoxTo(getX(), getY() + dropHeight);
    }

    /**
     * responds to the event where the ship jumps
     */
    public void jump() {
        jumpDurationLeft = maxJumpDuration;
    }

    /**
     * Moves the spaceship until it reach the JumpHeight or go back to the default position
     * (Still have to test and change about the function, it's only the beginning version.)
     */
    public void move() {
        if (jumpDurationLeft > 0) {
            moveUp();
            jumpDurationLeft--;
        } else {
            moveDown();
        }
    }
}
