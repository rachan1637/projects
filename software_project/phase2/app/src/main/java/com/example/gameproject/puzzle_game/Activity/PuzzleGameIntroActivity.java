package com.example.gameproject.puzzle_game.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.gameproject.MainActivity;
import com.example.gameproject.R;
import com.example.gameproject.User;

import java.util.Objects;

public class PuzzleGameIntroActivity extends AppCompatActivity {

    /**
     * The spinner menu items.
     */
    public final static String THREE_BY_THREE = "3 by 3 puzzle";
    public final static String FOUR_BY_FOUR = "4 by 4 puzzle";
    public final static String FIVE_BY_FIVE = "5 by 5 puzzle";
    public final static String FOUR_MIN = "Easy: 4 minutes";
    public final static String TWO_MIN = "Normal: 2 minutes";
    public final static String ONE_MIN = "Hard 1 minutes";
    private Spinner dimSpinner, timeSpinner;

    private PopupWindow popupWindow;

    /**
     * The list of choices for the spinner that allows the user to choose which
     * Java feature to demonstrate.
     */
    private final String[] puzzleDimensions = {THREE_BY_THREE, FOUR_BY_FOUR, FIVE_BY_FIVE};
    private final String[] countDownTimeChoice = {TWO_MIN, FOUR_MIN, ONE_MIN};

    private User currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_game_intro);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        Intent intent = getIntent();
        currentUser = (User) intent.getSerializableExtra("user");

        String userBackground = "#FFFFFF";
        if (currentUser != null) {
            userBackground = checkCustomization(currentUser);
        }

        RelativeLayout currentLayout = findViewById(R.id.intro_layout);
        currentLayout.setBackgroundColor(Color.parseColor(userBackground));

        Button startPuzzleGameButton, customizePuzzleGameButton, backButton;

        startPuzzleGameButton = findViewById(R.id.start_puzzle_game_button);

        startPuzzleGameButton.setOnClickListener(v -> {
            finish();
            Intent toGame = new Intent(v.getContext(), PuzzleGameActivity.class);
            toGame.putExtra("user", currentUser);
            startActivity(toGame);
        });

        customizePuzzleGameButton = findViewById(R.id.customize_puzzle_game_button);
        customizePuzzleGameButton.setOnClickListener(this::createCustomizationPopup);

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            finish();
            /*Intent backIntent = new Intent(this, MainActivity.class);
            backIntent.putExtra("user", currentUser);
            startActivity(backIntent);*/
        });

        if (intent.getBooleanExtra("continue_customization", false)) {
            createCustomizationPopup(findViewById(R.id.customize_puzzle_game_button));
        }
    }

    private void createCustomizationPopup(View view) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("InflateParams") View popupView = inflater.inflate(
                R.layout.customize_puzzle_game_window, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        // Taps outside the popup does not dismiss it
        popupWindow = new PopupWindow(popupView, width, height, false);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        view.post(() -> popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0));

        Button addImageButton, changeBackgroundButton, confirmButton, cancelButton;
        addImageButton = popupView.findViewById(R.id.add_picture_button);
        changeBackgroundButton = popupView.findViewById(R.id.background_button);
        confirmButton = popupView.findViewById(R.id.confirm_button);
        cancelButton = popupView.findViewById(R.id.cancel_button);

        addImageButton.setOnClickListener(view1 -> {
            finish();
            Intent toImage = new Intent(this, ImageActivity.class);
            toImage.putExtra("user", currentUser);
            startActivity(toImage);
        });

        changeBackgroundButton.setOnClickListener(v -> {
            finish();
            Intent toBackground = new Intent(v.getContext(), BackgroundChangeActivity.class);
            toBackground.putExtra("user", currentUser);
            startActivity(toBackground);
        });

        confirmButton.setOnClickListener(view12 -> {
            switch (dimSpinner.getSelectedItem().toString()) {
                case THREE_BY_THREE:
                    currentUser.set("puzzle_game_numColumns", "3");
                    currentUser.write();
                    break;
                case FOUR_BY_FOUR:
                    currentUser.set("puzzle_game_numColumns", "4");
                    currentUser.write();
                    break;
                case FIVE_BY_FIVE:
                    currentUser.set("puzzle_game_numColumns", "5");
                    currentUser.write();
                    break;
            }

            switch (timeSpinner.getSelectedItem().toString()) {
                case TWO_MIN:
                    currentUser.set("puzzle_game_countDownTime", "Normal");
                    currentUser.write();
                    break;
                case FOUR_MIN:
                    currentUser.set("puzzle_game_countDownTime", "Easy");
                    currentUser.write();
                    break;
                case ONE_MIN:
                    currentUser.set("puzzle_game_countDownTime", "Hard");
                    currentUser.write();
                    break;
            }
            popupWindow.dismiss();
        });

        cancelButton.setOnClickListener(view13 -> {
            popupWindow.dismiss();
            //does not save changes
        });

        setUpSpinner(popupWindow.getContentView());
    }

    private void setUpSpinner(View view) {
        // Set up the puzzleDimensions spinner.
        dimSpinner = view.findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, puzzleDimensions);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dimSpinner.setAdapter(adapter1);

        // Set up the countDownTime spinner
        timeSpinner = view.findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, countDownTimeChoice);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(adapter2);
    }

    public String checkCustomization(User currentUser){

        assert currentUser != null;
        String userCountDownTime = currentUser.get("puzzle_game_countDownTime");
        String userBackground = currentUser.get("puzzle_game_background");

        if(userCountDownTime == null){
            currentUser.set("puzzle_game_countDownTime", "Normal");
            currentUser.write();
        }

        if(userBackground == null){
            currentUser.set("puzzle_game_background", "#FFFFFF");
            currentUser.write();
            userBackground = "#FFFFFF";
        }

        return userBackground;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
