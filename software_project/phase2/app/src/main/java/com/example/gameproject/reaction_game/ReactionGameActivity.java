package com.example.gameproject.reaction_game;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gameproject.R;
import com.example.gameproject.User;

import java.util.Observer;


public class ReactionGameActivity extends AppCompatActivity {
    /**
     * user object that stores all the preferences of the user.
     */
    private User user;
    /**
     * the button to pause or resume the game
     */
    private ImageButton pause_or_resume;
    /**
     * true if the player have set the game to random speed.
     */
    protected boolean random;
    /**
     * boolean that represents whether or not the game has been paused before finished,
     * if paused then data will not be stored
     */
    protected boolean pause_before;
    /**
     * boolean that represents whether or not the game has been paused
     */
    private boolean pause = false;
    /**
     * class that in charge of checking whether or not a mole has been hit, if hit increase the point
     */
    private MoleManager moleManager;
    /**
     * class that responsible for drawing the mole from a moleManager
     */
    private MoleDrawer moleDrawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaction_game);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        user = ReactionGameMain.currentUser;
        moleManager = new MoleManager(this, getSpeed());
        moleDrawer = new MoleDrawer(this);
        moleManager.addObserver((Observer) moleDrawer);
        initButton();
        pause_before = false;

        setRandom();
    }

    /**
     * get the speed that the user set from the customize or by default value 750
     */
    private int getSpeed() {
        int speed;
        try {
            speed = Integer.parseInt(user.get("reaction_game_speed"));
        } catch (Exception e) {
            speed = 750;
        }
        return speed;
    }

    /**
     * if the player customized the speed to be random
     */
    private void setRandom() {
        try {
            random = Boolean.parseBoolean(user.get("reaction_game_random"));
        } catch (Exception e) {
            random = false;
        }
    }

    /**
     * while the game is paused, resume the game by pressing the pause button one more time
     */
    @Override
    protected void onResume() {
        super.onResume();
        pause_or_resume.setBackgroundResource(R.drawable.pause);
        moleManager.start();
    }

    /**
     * set the pause and resume button
     */
    private void initButton() {
        pause_or_resume = findViewById(R.id.pause_or_resume);

        pause_or_resume.setOnClickListener(view -> {//to pause
            if (!pause) {
                pause_or_resume.setBackgroundResource(R.drawable.resume);
                pause = true;
                moleManager.pause();
                pauseGame();
            }
        });
    }

    /**
     * function to pause the game, freeze all the thread and pause mole from generating
     */
    private void pauseGame() {
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View popupView = inflater.inflate(R.layout.pause_and_resume, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        // Taps outside the popup does not dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, false);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window token
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        Button exitButton, resumeButton;

        exitButton = popupView.findViewById(R.id.save);
        resumeButton = popupView.findViewById(R.id.resume_reaction);

        exitButton.setOnClickListener(view -> {
            popupWindow.dismiss();
            finish();
        });
        resumeButton.setOnClickListener(view -> {
            popupWindow.dismiss();
            pause_or_resume.setBackgroundResource(R.drawable.pause);
            pause_before = true;
            pause = false;
            moleManager.resume();
        });
    }

    /**
     * the end game method
     */
    public void endGame() {
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View popupView = inflater.inflate(R.layout.reaction_end, null);
        TextView textViewFinalScore = popupView.findViewById(R.id.score_reaction);
        textViewFinalScore.setText(String.valueOf(moleManager.getScore()));

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        // Taps outside the popup does not dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, false);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window token
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        Button exitButton;

        exitButton = popupView.findViewById(R.id.exit_reaction);

        exitButton.setOnClickListener(view -> {
            popupWindow.dismiss();
            int currentProgress = Integer.parseInt(user.get("collectible progress"));
            if (moleManager.getScore() >= 5000)
                currentProgress += 1;
            user.set("collectible progress", String.valueOf(currentProgress));
            user.write();
            finish();
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        moleManager.stop();
    }

    public void onBackPressed() {
        moleManager.stop();
        finish();
    }
}
