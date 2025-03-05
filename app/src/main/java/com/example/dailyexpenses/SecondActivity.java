package com.example.dailyexpenses;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    Button loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(view -> {
            Intent intent = new Intent(SecondActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        registerButton.setOnClickListener(view -> {
            Intent intent = new Intent(SecondActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
