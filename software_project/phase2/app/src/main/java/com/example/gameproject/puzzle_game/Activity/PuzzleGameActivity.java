package com.example.gameproject.puzzle_game.Activity;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import android.view.Gravity;
import android.widget.Toast;

import com.example.gameproject.R;
import com.example.gameproject.User;
import com.example.gameproject.puzzle_game.GameController.CustomImageManager;
import com.example.gameproject.puzzle_game.GameController.PuzzleGamePresenter;

import java.util.ArrayList;
import java.util.Objects;

public class PuzzleGameActivity extends AppCompatActivity implements PuzzleGameView {

    private PuzzleGamePresenter presenter;

    private static final String TAG = "Puzzle Game Activity";

    private TextView textViewTime;
    private TextView textViewpuzzleComp;
    private TextView textViewScore;
    private TextView textViewMoves;

    private PopupWindow popupWindow1;
    private PopupWindow popupWindow2;

    private User currentUser;

    private PopupWindow popupWindow;

    //Time given to complete the puzzles
    private long countDownInMillis = 120000;

    static final String smallHint = PuzzleGamePresenter.smallHint;
    static final String bigHint = PuzzleGamePresenter.bigHint;
    static final String skipPuzzle = PuzzleGamePresenter.skipPuzzle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_game);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        presenter = new PuzzleGamePresenter(this, getApplicationContext());
        presenter.setGestureDetectGridView(findViewById(R.id.grid));

        currentUser = (User) getIntent().getSerializableExtra("user");
        boolean isBonusPuzzle = getIntent().getBooleanExtra("bonus_mode", false);
        if (isBonusPuzzle) {
            countDownInMillis = 600000;
            setBackgroundColour();
            presenter.setNumColumns(3);
            ArrayList<Bitmap> puzzles = new ArrayList<>();
            Bitmap achievementPuzzle = BitmapFactory.decodeResource(getResources(),
                    R.drawable.achievement_puzzle);
            puzzles.add(achievementPuzzle);
            presenter.setPuzzles(puzzles);
        } else {
            setCustomizedFeatures();
        }

        textViewTime = findViewById(R.id.time);
        textViewpuzzleComp = findViewById(R.id.puzzle);
        textViewScore = findViewById(R.id.score);
        textViewMoves = findViewById(R.id.move);

        createOptionsButton();

        startCountDown();
        presenter.setDimensions();
        Log.i(TAG, "Game has Created.");
    }

    private void setCustomizedFeatures() {
        setCountDownTime();
        setBackgroundColour();
        setNumColumns();
        setPuzzles();
    }

    private void setCountDownTime() {
        String countDownStr = currentUser.get("puzzle_game_countDownTime");
        if (countDownStr == null) {
            countDownStr = "Normal";
        }
        switch (countDownStr){
            case "Easy":
                countDownInMillis = 240000;
                break;
            case "Hard":
                countDownInMillis = 60000;
                break;
            case "Normal":
                countDownInMillis = 120000;
                break;
        }
    }

    private void setBackgroundColour() {
        String userBackground = currentUser.get("puzzle_game_background");
        if(userBackground == null){
            currentUser.set("puzzle_game_background", "#FFFFFF");
            currentUser.write();
            userBackground = "#FFFFFF";
        }
        //background colour for the game screen, default is white
        String backgroundColour = userBackground;

        RelativeLayout currentLayout = findViewById(R.id.puzzle_game);
        currentLayout.setBackgroundColor(Color.parseColor(backgroundColour));
    }

    private void setNumColumns() {
        int columns;
        try {
            columns = Integer.parseInt(currentUser.get("puzzle_game_numColumns"));
        } catch (Exception e){
            columns = 3;
        }
        presenter.setNumColumns(columns);
    }

    private void setPuzzles() {
        String customImagesKeys = currentUser.get("puzzle_game_custom_images");
        ArrayList<Bitmap> puzzles = new ArrayList<>();
        for (Bitmap image : CustomImageManager.getImageList(customImagesKeys, getApplicationContext())) {
            if (image != null) {
                puzzles.add(image);
            }
        }
        presenter.setPuzzles(puzzles);
    }

    public void startCountDown() {
        presenter.startCountDown(countDownInMillis);
    }

    @Override
    public void showCountDownText(String text) {
        textViewTime.setText(text);
    }

    @Override
    public void showNumMoves(String text) {
        textViewMoves.setText(text);
    }

    @Override
    public void showNumCompleted(String text) {
        textViewpuzzleComp.setText(text);
    }

    @Override
    public void showScore(String text) {
        textViewScore.setText(text);
    }

    @Override
    public void setBackgroundClickable(boolean backgroundClickable) {
        findViewById(R.id.puzzle_game_options_button).setClickable(backgroundClickable);
    }

    @Override
    public void updateAchievement(){
        int achievementNum = Integer.parseInt(currentUser.get("collectible progress"));
        achievementNum += 1;
        currentUser.set("collectible progress", Integer.toString(achievementNum));
        currentUser.write();
    }

    private void pause() {
        presenter.pauseGame();
    }

    private void resume() {
        presenter.resumeGame();
    }

    private void buyItem(String item){presenter.buyItem(item);}

    /**
     * To create the options button.
     */
    private void createOptionsButton() {
        Button optionsButton;

        optionsButton = findViewById(R.id.puzzle_game_options_button);

        optionsButton.setOnClickListener(view -> {
            pause();

            // inflate the layout of the popup window
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            @SuppressLint("InflateParams") View popupView = inflater.inflate(
                    R.layout.puzzle_game_options_window, null);

            // create the popup window
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            popupWindow1 = new PopupWindow(popupView, width, height, false);

            // show the popup window
            // which view you pass in doesn't matter, it is only used for the window tolken
            popupWindow1.showAtLocation(view, Gravity.CENTER, 0, 0);

            Button returnToGameButton, toShopGameButton, exitGameButton;

            returnToGameButton = popupView.findViewById(R.id.return_to_game_button);
            toShopGameButton = popupView.findViewById(R.id.to_game_shop);
            exitGameButton = popupView.findViewById(R.id.exit_game_button);

            returnToGameButton.setOnClickListener(view1 -> {
                popupWindow1.dismiss();
                resume();
            });

            toShopGameButton.setOnClickListener(view2 -> {
                // inflate the layout of the popup window
//                LayoutInflater inflater2 = (LayoutInflater)
//                        getSystemService(LAYOUT_INFLATER_SERVICE);
//                assert inflater2 != null;
                @SuppressLint("InflateParams") View popupView2 = inflater.inflate(
                        R.layout.puzzle_game_shop, null);

                // create the popup window
                int width2 = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height2 = LinearLayout.LayoutParams.WRAP_CONTENT;
                popupWindow2 = new PopupWindow(popupView2, width2, height2, false);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window token
                popupWindow2.showAtLocation(view, Gravity.CENTER, 0, 0);

                RadioGroup items;
                Button buyButton, backButton;

                items = popupView2.findViewById(R.id.items_game_shop);

                buyButton = popupView2.findViewById(R.id.buy_game_shop);
                backButton = popupView2.findViewById(R.id.back_game_shop);

                buyButton.setOnClickListener(v -> {
                    int itemId = items.getCheckedRadioButtonId();

                    if (itemId == R.id.item1_game_shop){
                        buyItem(smallHint);
                    }
                    else if (itemId == R.id.item2_game_shop){
                        buyItem(bigHint);
                    }
                    else if (itemId == R.id.item3_game_shop){
                        buyItem(skipPuzzle);
                    }
                    else{
                        Toast toast = Toast.makeText(this,
                                "Invalid selection, please select again.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

                backButton.setOnClickListener(v2 -> popupWindow2.dismiss());
            });

            exitGameButton.setOnClickListener(view3 -> {
                popupWindow1.dismiss();
                finish();
            });
        });
    }


    public int getStatusBarHeight() {
        int result = 0;
        Context context = getApplicationContext();
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");

        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    @Override
    public void showFinalScore(int score) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("InflateParams") View popupView = inflater.inflate(
                R.layout.puzzle_game_final_score_window, null);
        TextView textViewFinalScore = popupView.findViewById(R.id.score);
        textViewFinalScore.setText(String.valueOf(score));

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        // Taps outside the popup does not dismiss it
        popupWindow = new PopupWindow(popupView, width, height, false);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window token
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        Button exitButton;

        exitButton = popupView.findViewById(R.id.exit_button);

        exitButton.setOnClickListener(view -> finish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pause();
        //Need to make sure that the popup windows are dismissed before leaving this activity to \
        //prevent memory leak.
        try {
            popupWindow1.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            popupWindow2.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            popupWindow.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
