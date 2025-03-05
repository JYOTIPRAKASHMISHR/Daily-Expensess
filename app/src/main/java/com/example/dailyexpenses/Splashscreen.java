package com.example.dailyexpenses;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class Splashscreen extends AppCompatActivity {

    private static final int SPLASH_DELAY = 3000; // 3 seconds delay

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        new Handler().postDelayed(() -> {
            // Intent to start the second activity
            Intent intent = new Intent(Splashscreen.this, SecondActivity.class);
            startActivity(intent);
            finish(); // Close the splash screen so the user can't return to it
        }, SPLASH_DELAY);
    }
}
