package com.example.mangurkaur.employeeattendancepro.Activties;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mangurkaur.employeeattendancepro.R;

public class SplashActivity extends AppCompatActivity {


    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent main = new Intent(SplashActivity.this,MainActivity.class);

            SplashActivity.this.startActivity(main);
            SplashActivity.this.finish();
            }
        },

SPLASH_DISPLAY_LENGTH);

    }
}
