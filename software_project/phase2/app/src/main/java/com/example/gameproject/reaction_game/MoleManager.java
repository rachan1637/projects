package com.example.gameproject.reaction_game;

import android.view.View;
import android.widget.ImageButton;

import java.util.Observable;

public class MoleManager extends Observable {
    /**
     * single mole object
     */
    private Moles moles;
    /**
     * the positions that can be chosen to hit
     */
    private ImageButton hitPosition;
    /**
     * the count down time
     */
    private TimeThread time;
    /**
     * the count down time for mole
     */
    private MoleThread generateMole;
    /**
     * behaviour when hit
     */
    private ClickImage clicker;
    /**
     * whether or not we hit a mole
     */
    private boolean hit;
    /**
     * how long has the game started
     */
    private int timer, currStep, running;

    public MoleManager(ReactionGameActivity reaction, int speed) {
        timer = 60;
        generateMole = new MoleThread();
        time = new TimeThread();
        clicker = new ClickImage();
        moles = new Moles(reaction, clicker, speed, 0);
        generateMole.setRunning(true);
        time.setRunning(true);
        clicker.setMovable(true);
        generateMole.setStep(1);
        generateMole.setActivity(reaction, this, moles);
        time.setActivity(reaction, this);
        clicker.setReaction(this);

    }

    /**
     * update the next mole that is going to appeaar onto the screen
     */
    public void updateScreen(int next, int step) {
        running = 1;
        moles.generateNextMole(next);
        currStep = step;
        setChanged();
        notifyObservers();
    }

    /**
     * situation when we hit a mole
     */
    public void ifHit(View v) {
        running = 3;
        if (moles.checkSame(v)) {
            moles.generateScore();
            hit = true;
        } else {
            hit = false;
            hitPosition = (ImageButton) v;
        }

        moles.setNext(0);
        setChanged();
        notifyObservers();
    }

    public void updateTime() {
        running = 2;
        setChanged();
        notifyObservers();
        timer--;
    }

    /**
     * return the duration since the game has started
     */
    public int getTimer() {
        return this.timer;
    }

    /**
     * return the current score
     */
    public int getScore() {
        return moles.getScore();
    }

    public int watRunning() {
        return running;
    }

    /**
     * the position that we hit
     */
    public ImageButton getHitPosition() {
        return this.hitPosition;
    }

    /**
     * whether or not we hit a mole
     */
    public boolean hitMole() {
        return this.hit;
    }

    /**
     * how many steps we have taken in total
     */
    public int getStep() {
        return this.currStep;
    }

    /**
     * find the next mole that is going to appear on to the screen
     */
    public ImageButton getNextMole() {
        return moles.getNextMole();
    }

    /**
     * the minimap for the moles
     */
    public ImageButton[] getAllMoles() {
        return moles.getAllMoles();
    }

    /**
     * pause the game(time thread, mole thread)
     */
    public void pause() {
        clicker.setMovable(false);
        generateMole.setRunning(false);
        time.setRunning(false);
    }

    /**
     * resume all timer that has been paused and start producing moles
     */
    public void resume() {
        clicker.setMovable(true);
        generateMole.setRunning(true);
        time.setRunning(true);
    }

    /**
     * start the game
     */
    public void start() {
        generateMole.start();
        time.start();
    }

    /**
     * stop current game
     */
    public void stop() {
        generateMole.interrupt();
        time.interrupt();
        clicker.setMovable(false);
        generateMole.setRunning(false);
        time.setRunning(false);
    }

}
