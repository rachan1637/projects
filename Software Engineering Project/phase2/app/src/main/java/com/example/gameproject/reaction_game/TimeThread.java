package com.example.gameproject.reaction_game;

import android.widget.Toast;


class TimeThread extends Thread {
    /**
     * whether or not the game is running
     */
    private boolean running;
    /**
     * using class reaction game activity to pause/resume/end the game and pause the time thread
     */
    private ReactionGameActivity reaction;
    /**
     * if running tell the molemanager to keep producing moles
     */
    private MoleManager moleManager;

    public void run() {
        while (moleManager.getTimer() >= 0) {
            try {
                reaction.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (running) {
                            moleManager.updateTime();
                        }
                        if (moleManager.getTimer() == -1) {
                            Toast.makeText(reaction, "Time Up", Toast.LENGTH_SHORT).show();
                            reaction.endGame();
                            reaction.onStop();
                        }
                    }
                });
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            } catch (Exception e) {
            }
        }
    }

    /**
     * set whether or not the game is running
     */
    public void setRunning(boolean setRunning) {
        this.running = setRunning;
    }

    public void setActivity(ReactionGameActivity action, MoleManager moleManager) {
        this.reaction = action;
        this.moleManager = moleManager;
    }
}
