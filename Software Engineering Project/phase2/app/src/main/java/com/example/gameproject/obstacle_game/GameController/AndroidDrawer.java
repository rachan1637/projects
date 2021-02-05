package com.example.gameproject.obstacle_game.GameController;

import com.example.gameproject.obstacle_game.GameElements.*;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * draws the items in the game to the given canvas
 */

class AndroidDrawer implements Drawer<Canvas>, Observer {
    /**
     * List containing all SpaceShips in this adventure.
     */
    private List<SpaceShip> spaceShipList;

    /**
     * List containing all obstacles in this adventure.
     */
    private List<Obstacle> spaceObstacles;

    /**
     * List containing all treasury box in this adventure.
     */
    private List<Obstacle> treasuryBoxList;

    /**
     * The width of the screen.
     */
    private int screenWidth;

    /**
     * The height of the screen.
     */
    private int screenHeight;

    /**
     * The time need to be counted down before the game start.
     */
    private int startGameCountDown;

    /**
     * The boolean to see whether the game is over.
     */
    private boolean gameOver;

    /**
     * The paint for ships.
     */
    private Paint shipPaint;

    /**
     * The paint for obstacles.
     */
    private Paint obstaclePaint;

    /**
     * The paint for reminder in the center of the screen.
     */
    private Paint reminderPaint;

    /**
     * The paint for treasury box.
     */
    private Paint treasuryBoxPaint;

    /**
     * A default drawer.
     */
    AndroidDrawer(int width, int height) {
        screenWidth = width;
        screenHeight = height;
        setShipPaint();
        setObstaclePaint();
        setReminderPaint();
        setTreasuryBoxPaint();
    }

    /**
     * Initializes the shipPaint.
     */
    private void setShipPaint() {
        shipPaint = new Paint();
        shipPaint.setTextSize(36);
        shipPaint.setTypeface(Typeface.DEFAULT_BOLD);
        shipPaint.setColor(Color.CYAN);
    }

    /**
     * Initializes the obstaclePaint.
     */
    private void setObstaclePaint() {
        obstaclePaint = new Paint();
        obstaclePaint.setColor(Color.rgb(255, 0, 0));
        obstaclePaint.setStrokeWidth(10);
    }

    /**
     * Initializes the reminderPaint.
     */
    private void setReminderPaint() {
        reminderPaint = new Paint();
        reminderPaint.setTextSize(100);
        reminderPaint.setColor(Color.MAGENTA);
    }

    /**
     * Initializes the treasuryBoxPaint.
     */
    private void setTreasuryBoxPaint() {
        treasuryBoxPaint = new Paint();
        treasuryBoxPaint.setColor(Color.rgb(255, 255, 0));
        obstaclePaint.setStrokeWidth(10);
    }

    /**
     * Updates the lists of space items.
     *
     * @param o   the observable object adventureManager.
     * @param arg the argument about the update.
     */
    @Override
    public void update(Observable o, Object arg) {
        AdventureManager adventureManager = (AdventureManager) o;
        spaceShipList = adventureManager.getSpaceShipList();
        spaceObstacles = adventureManager.getObstacles();
        startGameCountDown = adventureManager.getStartGameCountDown();
        gameOver = adventureManager.getGameOver();
        treasuryBoxList = adventureManager.getTreasuryBoxList();
    }

    /**
     * Draws all space items in the lists to the canvas
     *
     * @param canvas the canvas on which to draw the space items.
     */
    public void draw(Canvas canvas) {
        // draw the start game count down time.
        if (startGameCountDown > 0) {
            drawStartGameCountDown(canvas);
        }

        if (gameOver) {
            drawGameOver(canvas);
        }

        // draw the spaceship and relating objects
        for (int i = 0; i < spaceShipList.size(); i++) {
            SpaceShip s = spaceShipList.get(i);

            // draw spaceship.
            drawSpaceship(canvas, s);
            // draw lives
            drawLives(canvas, s, i);
            // draw the remaining invincible time
            drawInvincibleTime(canvas, s, i);
            // draw the out of Screen time
            drawOutOfScreenTime(canvas, s, i);
            // draw the notification if the spaceship gets a collection.
            drawGetCollection(canvas, s, i);
        }

        // draw the obstacles.
        drawObstacles(canvas, spaceObstacles, obstaclePaint);
        drawObstacles(canvas, treasuryBoxList, treasuryBoxPaint);
    }


    /**
     * Draws Count Down time before the game starts on the screen.
     *
     * @param canvas the canvas on which to draw count down time.
     */
    private void drawStartGameCountDown(Canvas canvas) {
        String text = String.valueOf(startGameCountDown / 30 + 1);
        canvas.drawText(text, screenWidth / 2, screenHeight / 2, reminderPaint);
    }

    /**
     * Draws "Game Over" on the screen.
     *
     * @param canvas the canvas on which to draw "Game Over".
     */
    private void drawGameOver(Canvas canvas) {
        canvas.drawText("Game Over", screenWidth / 2, screenHeight / 2, reminderPaint);
    }

    /**
     * Draws the spaceship on canvas.
     *
     * @param canvas the canvas on which to draw the spaceship.
     */
    private void drawSpaceship(Canvas canvas, SpaceShip s) {
        canvas.drawText("(--)ship(--)", s.getX(), s.getY(), shipPaint);
    }

    /**
     * Draws a single life on canvas.
     *
     * @param canvas the canvas on which to draw a life.
     * @param x      the first coordinate to draw the life.
     * @param y      the second coordinate to draw the life.
     */
    private void drawLife(Canvas canvas, int x, int y) {
        canvas.drawText(".", x, y, shipPaint);
    }

    /**
     * Draws the lives on canvas.
     *
     * @param canvas the canvas on which to draw the lives.
     * @param i      the number(index) of the spaceship
     */

    private void drawLives(Canvas canvas, SpaceShip s, int i) {
        int remainingLives = s.getLives();
        int distance = 125;
        canvas.drawText("Lives:", screenWidth / 30, (i + 1) * screenHeight / 18, shipPaint);
        while (remainingLives > 0) {
            drawLife(canvas, screenWidth / 30 + distance, (i + 1) * (screenHeight / 20));
            remainingLives--;
            distance += 75;
        }
    }

    /**
     * Draws the time on canvas.
     *
     * @param canvas the canvas on which to draw the time
     * @param time   the time.
     * @param x      the first coordinate to draw the time.
     */
    private void drawTime(Canvas canvas, int time, int x, int y, Paint paint) {
        String text = String.valueOf(time / 30 + 1);
        canvas.drawText(text, x, y, paint);
    }

    /**
     * Draws the invincible time on canvas.
     *
     * @param canvas the canvas on which to draw invincible time.
     */
    private void drawInvincibleTime(Canvas canvas, SpaceShip s, int i) {
        int invincibleTime = s.getInvincibleTime();
        if (invincibleTime != 0) {
            canvas.drawText("Remaining Invincible Time : ", screenWidth / 4, (i + 1) * screenHeight / 10, shipPaint);
            drawTime(canvas, invincibleTime, screenWidth / 2 - screenWidth / 50, (i + 1) * (screenHeight / 10 + 2), shipPaint);
        }
    }

    /**
     * Draws the out of screen time on canvas.
     *
     * @param canvas the canvas on which to draw out of screen time.
     */
    private void drawOutOfScreenTime(Canvas canvas, SpaceShip s, int i) {
        int outTime = s.getOutTime();
        if (outTime != 0) {
            canvas.drawText("You can still be out of screen for : ", screenWidth / 4, (i + 1) * screenHeight / 18, shipPaint);
            drawTime(canvas, outTime, screenWidth / 2 + screenWidth / 50, (i + 1) * (screenHeight / 18 + 2), shipPaint);
        }
    }

    /**
     * Draws the number of collections got on canvas.
     *
     * @param canvas the canvas on which to draw the number of collections you get.
     */
    private void drawGetCollection(Canvas canvas, SpaceShip s, int i) {
        int collection = s.getCollection();
        int collectionTime = s.getCollectionTime();
        if (collectionTime != 0) {
            canvas.drawText("The number of collection you get is: " + collection, screenWidth / 4, (i + 1) * screenHeight / 7, shipPaint);
        }
    }

    /**
     * Draws all the obstacles.
     *
     * @param canvas the canvas on which to draw obstacles.
     */
    private void drawObstacles(Canvas canvas, List<Obstacle> list, Paint paint) {
        for (Obstacle obstacle : list) {
            if (obstacle != null) {
                canvas.drawRect(obstacle.getHitBox(), paint);
            }
        }
    }
}
