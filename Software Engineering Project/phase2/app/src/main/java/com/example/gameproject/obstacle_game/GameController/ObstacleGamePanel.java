package com.example.gameproject.obstacle_game.GameController;

import com.example.gameproject.*;
import com.example.gameproject.obstacle_game.Activity.ObstacleGameEndActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.Observable;
import java.util.Observer;

/*
 * a game panel
 * a sightly tweaked version of code found on https://www.youtube.com/watch?v=OojQitoAEXs&t=1234s
 */

public class ObstacleGamePanel extends GamePanel {
    /**
     * The game player.
     */
    private User player;

    /**
     * The manager that deals with all backend data.
     */
    private Manager adventureManger;

    /**
     * the drawer that would transfer all backend data to visual representations.
     */
    private Drawer drawer;

    /**
     * The player mode of this game.
     */
    private Mode playerMode;

    /**
     * A compromise solution to solve a problem when ending the game, see comments in update() for details
     */
    private boolean gameOverCalled = false;


    public ObstacleGamePanel(Context context, int difficulty, Mode mode, User currentUser) {
        super(context);

        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        adventureManger = new AdventureManager(screenWidth, screenHeight, difficulty);

        this.playerMode = mode;
        mode.addSpaceShip(adventureManger, difficulty, screenWidth, screenHeight);

        drawer = new AndroidDrawer(screenWidth, screenHeight);
        ((Observable) adventureManger).addObserver((Observer) drawer);

        player = currentUser;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        super.surfaceCreated(surfaceHolder);
    }

    @Override
    public void update() {
        adventureManger.update();

        //when game ends,
        //The if statement is suppose to run only once, but in reality it is called many times,
        //for unknown reasons the update loop continues even after startActivity(intent) is called
        //so the boolean gameOverCalled is used make sure it only runs once
        boolean gameOver = ((AdventureManager) adventureManger).getGameOver();
        int endGameCount = ((AdventureManager) adventureManger).getEndGameCountDown();
        if (gameOver && endGameCount == 0 && !gameOverCalled) {
            // record collectible progress
            int currentProgress = Integer.parseInt(player.get("collectible progress"));
            currentProgress += ((AdventureManager) adventureManger).getCollections().get(0);
            player.set("collectible progress", String.valueOf(currentProgress));
            player.write();

            gameOverCalled = true;
            Context myContext = getContext();
            Intent intent = new Intent(myContext, ObstacleGameEndActivity.class);
            playerMode.setUpBundle(intent, adventureManger);
            myContext.startActivity(intent);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawer.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        playerMode.respondTouch(event, adventureManger);
        return true;
    }
}
