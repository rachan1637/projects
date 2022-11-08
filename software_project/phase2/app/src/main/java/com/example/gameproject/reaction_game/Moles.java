package com.example.gameproject.reaction_game;

import android.view.View;
import android.widget.ImageButton;

import java.util.HashMap;

public class Moles {
    /**
     * the speed that a mole appears
     */
    private final int daultSpeed = 750;
    /**
     * list of image buttons which represent 9 moles
     */
    private ImageButton[] buttons = new ImageButton[9];
    /**
     * a hashmap that represent the game field
     */
    private HashMap<Integer, ImageButton> nextToMole = new HashMap<>();
    /**
     * a hashmap represent whether or not a mole appears at specific location
     */
    private HashMap<ImageButton, Integer> moleToID = new HashMap<>();
    /**
     * the next mole that is going to appear
     */
    private ImageButton nextMole;
    /**
     * the duration of the appearance is going to be refreshed
     */
    private int refreshTime;
    /**
     * the score that keep track of the number of mole the player has hit in total
     */
    private int score, next;


    public Moles(ReactionGameActivity reaction, ClickImage clicker, int refreshTime, int score) {
        this.refreshTime = refreshTime;
        this.score = score;
        for (int i = 0; i < 9; i++) {
            int id = reaction.getResources().getIdentifier("btn_" + (i + 1), "id", reaction.getPackageName());
            buttons[i] = reaction.findViewById(id);
            buttons[i].setOnClickListener(clicker);
            nextToMole.put(i + 1, buttons[i]);
            moleToID.put(buttons[i], i + 1);
        }
    }

    /**
     * return buttons of all moles
     */
    public ImageButton[] getAllMoles() {
        return buttons;
    }

    /**
     * display the score onto game board
     */
    public void generateScore() {
        score += ((double) daultSpeed / getRefreshTime()) * 100;
    }

    /**
     * set the refresh time for the mole to be displayed
     */
    public void generateRefreshTime() {
        refreshTime = (int) (Math.random() * 751) + 250;
    }

    public boolean checkSame(View v) {
        return this.next == moleToID.get(v);
    }

    public void setNext(int next) {
        this.next = next;
    }

    public void generateNextMole(int next) {
        this.next = next;
        nextMole = nextToMole.get(next);
    }

    /**
     * find the refresh time for the game
     */
    public int getRefreshTime() {
        return refreshTime;
    }

    /**
     * find the total moles that has been hit
     */
    public int getScore() {
        return score;
    }

    public ImageButton getNextMole() {
        return nextMole;
    }

}
