package com.example.gameproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gameproject.puzzle_game.GameController.ImageSplitter;
import com.example.gameproject.puzzle_game.Activity.PuzzleGameActivity;

import java.util.ArrayList;
import java.util.Objects;

public class AchievementsActivity extends AppCompatActivity {

    private User currentUser;
    private int progress;
    private Bitmap[] puzzlePieces = new Bitmap[9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        Intent intent = getIntent();
        currentUser = (User) intent.getSerializableExtra("user");

        Button backButton;

        backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(v -> finish());

        if (currentUser.get("collectible progress") == null) {
            progress = 0;
        } else {
            progress = Integer.parseInt(currentUser.get("collectible progress"));
        }
        setPuzzlePieces();
        if (progress >= 9) {
            Toast.makeText(getApplicationContext(), "YOU UNLOCKED A BONUS PUZZLE!",
                    Toast.LENGTH_SHORT).show();
            unlockBonus();
        }

        GridView achievementGrid = findViewById(R.id.achievements_grid);
        AchievementsAdapter achievementsAdapter = new AchievementsAdapter(getApplicationContext(),
                puzzlePieces);
        achievementGrid.setAdapter(achievementsAdapter);
    }

    private void setPuzzlePieces() {
        Bitmap puzzle = BitmapFactory.decodeResource(getResources(),
                R.drawable.achievement_puzzle);
        ImageSplitter imageSplitter = new ImageSplitter(3);
        ArrayList<Bitmap> imageList = imageSplitter.splitImage(puzzle);
        int unlockedPieces;
        if (progress > 9) {
            unlockedPieces = 9;
        } else {
            unlockedPieces = progress;
        }
        for (int i = 0; i < unlockedPieces; i++) {
            puzzlePieces[i] = imageList.get(i);
        }

    }

    private void unlockBonus() {
        RelativeLayout layout = findViewById(R.id.achievements);
        Button bonusButton = new Button(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.back_button);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.topMargin = (int) (10 * getResources().getDisplayMetrics().density);
        bonusButton.setLayoutParams(params);
        bonusButton.setText(R.string.unlock);
        layout.addView(bonusButton);
        bonusButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, PuzzleGameActivity.class);
            intent.putExtra("bonus_mode", true);
            intent.putExtra("user", currentUser);
            startActivity(intent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent backIntent = new Intent(this, MainActivity.class);
            backIntent.putExtra("user", currentUser);
            startActivity(backIntent);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
