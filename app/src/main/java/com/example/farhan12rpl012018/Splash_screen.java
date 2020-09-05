package com.example.farhan12rpl012018;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class Splash_screen extends AppCompatActivity {

    private static int SplashScreen = 1150;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        SharedPreferences sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
        id = sharedPreferences.getString("id", "");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (id.isEmpty()) {
                    Intent intent = new Intent(Splash_screen.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(Splash_screen.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SplashScreen);

    }
}
