package com.example.gameproject.reaction_game;

import android.view.View;


class ClickImage implements View.OnClickListener {
    private boolean movable;
    private MoleManager moleManager;

    @Override
    public void onClick(View v) {
        if (movable) {
            moleManager.ifHit(v);
        }
    }

    public void setMovable(boolean movable) {
        this.movable = movable;
    }

    public void setReaction(MoleManager moleManager) {
        this.moleManager = moleManager;
    }

}
