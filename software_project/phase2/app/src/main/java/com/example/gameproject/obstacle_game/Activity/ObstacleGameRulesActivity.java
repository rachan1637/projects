package com.example.gameproject.obstacle_game.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gameproject.R;

public class ObstacleGameRulesActivity extends AppCompatActivity {
    private TextView textView;
    private ImageView image;
    private boolean first_page = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obstacle_game_rules);
        textView = findViewById(R.id.textView);
        image = findViewById(R.id.imageView);
        Button button = findViewById(R.id.next_page);

        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                first_page = !first_page;
                if(first_page){
                    textView.setText("To start the game, make sure you switch your phone to landsacpe mode. Then" +
                            " you can set the customization option in the menu, or simply get started right away using default customizations." +
                            " Before the game starts, there would be a 5 seconds count down, after which you can start controlling the spaceship" +
                            " to doge obstacles by tapping on the screen.");
                    image.setImageResource(R.drawable.obstacle_game_rule1);
                }
                else{
                    textView.setText("In each game you have 3 lives, and your lives will decrease by 1 whenever you" +
                            " hit a red obstacle, after you hit the obstacle each time your will not lose further lives in the next 3 seconds. You lose" +
                            " the game by having your lives reduce to 0 or staying out of the screen for too long. The obstacles are infinite so survive" +
                            " for as long as you can. Also, pick up your achievments by hitting yellow obstacles. Have fun!");
                    image.setImageResource(R.drawable.obstacle_game_rule3);
                }

            }
        });
    }
}