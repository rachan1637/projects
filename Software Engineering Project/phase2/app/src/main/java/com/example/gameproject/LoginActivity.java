package com.example.gameproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        EditText userNameEditText = findViewById(R.id.username);
        EditText passWordEditText = findViewById(R.id.password);
        Button signUp = findViewById(R.id.sign_up_button);
        Button login = findViewById(R.id.login_button);
        TextView errorMsg = findViewById(R.id.textView);

        login.setOnClickListener(v -> {
            String userName = userNameEditText.getText().toString();
            String passWord = passWordEditText.getText().toString();
            if (userName.equals("") || passWord.equals("")) {
                errorMsg.setText(R.string.log_in_warning);
            } else {
                User potentialUser = User.getUser(userName, passWord);
                if (potentialUser == null) {
                    Log.i("info", "error detected");
                    errorMsg.setText(R.string.log_in_warning);
                } else {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("user", potentialUser);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });

        signUp.setOnClickListener(v -> startActivityForResult(new Intent(v.getContext(),
                SignUpActivity.class), 1));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("user", data.getSerializableExtra("user"));
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        }
    }
}
