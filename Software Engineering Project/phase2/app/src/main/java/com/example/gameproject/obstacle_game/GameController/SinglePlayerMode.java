package com.example.gameproject.obstacle_game.GameController;

import com.example.gameproject.obstacle_game.GameElements.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class SinglePlayerMode implements Mode {
    /**
     * Adds one spaceship to adventure manager based on the difficulty.
     *
     * @param manager    the manager for which to add spaceship.
     * @param difficulty the difficulty of the game.
     */
    public void addSpaceShip(Manager manager, int difficulty, int width, int height) {
        AdventureManager adventureManager = (AdventureManager) manager;
        if (difficulty == 3) {
            adventureManager.addSpaceShip(new SpaceShip(3, width, height));
        } else {
            adventureManager.addSpaceShip(new SpaceShip(4, width, height));
        }
    }

    /**
     * Makes manager respond to the touch in the game.
     *
     * @param event   the motion event.
     * @param manager the manager for which to respond.
     */
    public void respondTouch(MotionEvent event, Manager manager) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            ((AdventureManager) manager).respondTouch(0);
        }
    }

    /**
     * Sets up the bundle to transfer scores and collections to ObstacleGameEndActivity.
     *
     * @param intent  the intent for which to put the bundle
     * @param manager the manager of this game.
     */
    public void setUpBundle(Intent intent, Manager manager) {
        Bundle bundle = new Bundle();
        bundle.putString("mode", "SinglePlayerMode");
        for (SpaceShip s : ((AdventureManager) manager).getSpaceshipGarbageCart()) {
            bundle.putString("score", String.valueOf(s.getScore() / 60 * 100));
            bundle.putString("collection", String.valueOf(s.getCollection()));
        }
        intent.putExtras(bundle);
    }
}
