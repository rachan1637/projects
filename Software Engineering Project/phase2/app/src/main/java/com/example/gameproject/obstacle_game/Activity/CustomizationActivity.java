package com.example.gameproject.obstacle_game.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.gameproject.R;

import androidx.appcompat.app.AppCompatActivity;

public class CustomizationActivity extends AppCompatActivity {
    private int gameDifficulty = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obstacle_game_customization);
        setTitle("Obstacle Game Customization");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Spinner spinner = findViewById(R.id.difficulty_level);
        String[] levels = new String[]{"Difficulty", "Easy", "Medium", "Hard"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, levels);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        final Spinner spinner2 = spinner;


        Button customizationButton;
        customizationButton = (Button) findViewById(R.id.confirm_customization);
        customizationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String difficulty = spinner2.getSelectedItem().toString();
                if (difficulty == "Difficulty" | difficulty == "Medium") {
                    gameDifficulty = 2;
                } else if (difficulty == "Easy") {
                    gameDifficulty = 1;
                } else if (difficulty == "Hard") {
                    gameDifficulty = 3;
                }
                Intent resultIntent = new Intent();
                resultIntent.putExtra("difficulty", String.valueOf(gameDifficulty));
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
