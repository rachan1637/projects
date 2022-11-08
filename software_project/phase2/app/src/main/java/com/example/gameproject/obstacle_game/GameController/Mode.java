package com.example.gameproject.obstacle_game.GameController;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;


public interface Mode {
    void addSpaceShip(Manager manager, int difficulty, int width, int height);

    void respondTouch(MotionEvent event, Manager manager);

    void setUpBundle(Intent intent, Manager manager);
}
