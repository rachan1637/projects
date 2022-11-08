package com.example.gameproject.reaction_game;

import android.widget.ImageButton;
import android.widget.TextView;

import com.example.gameproject.R;

import java.util.Observable;
import java.util.Observer;

public class MoleDrawer implements Observer {
    /**
     * the score and time that's going to be displayed while playing
     */
    private TextView t_score, t_timer;

    /**
     * initialize object in layout
     */
    public MoleDrawer(ReactionGameActivity reaction) {
        super();
        t_score = reaction.findViewById(R.id.score);
        t_timer = reaction.findViewById(R.id.timer);
        t_score.setText("0");
    }

    /**
     * update the mole information if a mole got hit
     */
    @Override
    public void update(Observable o, Object arg) {
        MoleManager moleManager = (MoleManager) o;

        if (moleManager.watRunning() == 1) {
            if (moleManager.getStep() == 2)
                setMole(moleManager);
            else
                resetScreen(moleManager);
        } else if (moleManager.watRunning() == 2) {
            setTime(moleManager);
        } else {
            if (moleManager.hitMole())
                setHit(moleManager);
            else
                setNotHit(moleManager);
        }

    }

    /**
     * change the time to specific value
     */
    private void setTime(MoleManager moleManager) {
        String ts = "" + moleManager.getTimer();
        t_timer.setText(ts);
    }

    /**
     * reset all buttons to hole
     */
    private void resetScreen(MoleManager moleManager) {
        ImageButton[] buttons = moleManager.getAllMoles();
        for (int i = 0; i < 9; i++)
            buttons[i].setBackgroundResource(R.drawable.hole);
    }

    /**
     * a mole appears in one of nine position
     */
    private void setMole(MoleManager moleManager) {
        resetScreen(moleManager);
        moleManager.getNextMole().setBackgroundResource(R.drawable.mole);
    }

    /**
     * when we hit a mole, we update information from the existing moles and update the score
     */
    private void setHit(MoleManager moleManager) {
        moleManager.getNextMole().setBackgroundResource(R.drawable.hit);
        String ts = "" + moleManager.getScore();
        t_score.setText(ts);
    }

    /**
     * if we didn't hit the mole, score doesn't change
     */
    private void setNotHit(MoleManager moleManager) {
        resetScreen(moleManager);
        moleManager.getHitPosition().setBackgroundResource(R.drawable.miss);
    }

}
