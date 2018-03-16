package com.sae.sc.activity;

import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sae.sc.R;
import com.sae.sc.activity.base.AbstractNavDrawerActivity;

public class KancolleActivity extends AbstractNavDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kancolle);
    }

    @Override
    public void onResume() {
        super.onResume();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_kancolle);
    }
}
