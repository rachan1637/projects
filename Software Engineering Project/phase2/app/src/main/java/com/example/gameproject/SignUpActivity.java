package com.example.gameproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        EditText userNameEditText = findViewById(R.id.username);
        EditText passWordEditText = findViewById(R.id.password);
        Button signUp = findViewById(R.id.sign_up_button);
        TextView errorMsg = findViewById(R.id.textView);

        signUp.setOnClickListener(v -> {
            String userName = userNameEditText.getText().toString();
            String passWord = passWordEditText.getText().toString();
            if (userName.equals("") || passWord.equals("")) {
                errorMsg.setText(R.string.signup_error);
            }
            //using these characters will mess up encoding process in User, so they are not allowed
            else if(passWord.contains("-") || passWord.contains(":") || passWord.contains(";")){
                errorMsg.setText(R.string.special_characters_found);
            }
            else if(User.getUser(userName, passWord) != null)
            {
                errorMsg.setText(R.string.redundant_user);
            }
            else {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("user", new User(userName, passWord));
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });


    }
}
