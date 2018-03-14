package com.sae.sc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sae.sc.activity.BasicCalculatorActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        startMainApplication();
    }

    private void startMainApplication() {
        Intent intent = new Intent(SplashActivity.this, BasicCalculatorActivity.class);
        startActivity(intent);
    }
}
