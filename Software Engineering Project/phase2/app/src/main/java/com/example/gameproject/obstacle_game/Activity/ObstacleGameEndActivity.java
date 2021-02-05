package com.example.gameproject.obstacle_game.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.gameproject.R;

import androidx.appcompat.app.AppCompatActivity;


public class ObstacleGameEndActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obstacle_game_end);
        setTitle("Obstacle Game Result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Bundle bundle = getIntent().getExtras();

        String score = bundle.getString("score");
        score = "Your Score is " + score;
        String collection = bundle.getString("collection");
        collection = "The number of collections you get is " + collection;

        final TextView thanks = findViewById(R.id.thanks_for_playing_obstacle);
        final TextView scoreText = findViewById(R.id.obstacle_game_score);
        final TextView collectionText = findViewById(R.id.obstacle_game_collection);

        thanks.setText(R.string.thanks_obstacle_game);
        scoreText.setText(score);
        collectionText.setText(collection);
    }


    public void onBackPressed() {
        Intent intent = new Intent(this,
                ObstacleGameIntroActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

    }
}
