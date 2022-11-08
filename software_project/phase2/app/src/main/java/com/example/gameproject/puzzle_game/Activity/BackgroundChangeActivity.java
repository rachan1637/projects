package com.example.gameproject.puzzle_game.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gameproject.R;
import com.example.gameproject.User;

import java.util.ArrayList;
import java.util.Objects;

public class BackgroundChangeActivity extends AppCompatActivity {
    String backgroundColour;
    String[] colours;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_game_background_change);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        currentUser = (User) getIntent().getSerializableExtra("user");
        colours = this.getResources().getStringArray(R.array.colourOptions);

        createOptionButtons();

        Button buttonConfirm, buttonCancel;
        buttonConfirm = findViewById(R.id.confirm_background);
        buttonCancel = findViewById(R.id.cancel_background);
        buttonConfirm.setOnClickListener(v -> {
            assert currentUser != null;
            saveBackground();
        });
        buttonCancel.setOnClickListener(v -> cancelBackground());

        final TextView introTextView = findViewById(R.id.background_option);
        introTextView.setText(R.string.colour_choose);
    }

    private void createOptionButtons() {
        ArrayList<ImageButton> options = new ArrayList<>();
        for (int i = 0; i < colours.length; i++) {
            int resourceId = this.getResources().
                    getIdentifier("imageButton" + (i+1), "id", this.getPackageName());
            options.add(findViewById(resourceId));
            int finalI = i;
            options.get(i).setOnClickListener(v -> backgroundColour = colours[finalI]);
        }
    }

    private void saveBackground(){
        currentUser.set("puzzle_game_background", backgroundColour);
        currentUser.write();
        finish();
    }

    private void cancelBackground(){
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, PuzzleGameIntroActivity.class);
        intent.putExtra("user", currentUser);
        intent.putExtra("continue_customization", true);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
