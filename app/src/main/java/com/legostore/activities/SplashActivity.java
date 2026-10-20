package com.legostore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.legostore.MainActivity;
import com.legostore.R;
import com.legostore.utils.UserSessionManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UserSessionManager session = new UserSessionManager(SplashActivity.this);
                if (session.isLoggedIn()) {
                    if (session.isAdmin()) {
                        startActivity(new Intent(SplashActivity.this, AdminDashboardActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish();
            }
        }, 2000);
    }
}