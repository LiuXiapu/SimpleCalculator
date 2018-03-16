package com.sae.sc;

import android.content.Intent;
import android.os.Handler;
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
        final Intent intent = new Intent(SplashActivity.this, BasicCalculatorActivity.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                SplashActivity.this.finish();
            }
        }, 2000);
    }
}
