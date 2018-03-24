package com.sae.sc.activity;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sae.sc.R;
import com.sae.sc.activity.base.AbstractNavDrawerActivity;
import com.sae.sc.utils.CalendarSelector;

import java.util.HashMap;

public class CalendarActivity extends AbstractNavDrawerActivity
        implements CalendarSelector.ICalendarSelectorCallBack {
    public static final String TAG = "CalendarActivity";

    private TextView detailTextView;
    private CalendarSelector mCalendarSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        this.setTitle(R.string.calendar_title);

        detailTextView = (TextView) findViewById(R.id.text);
        View content = findViewById(R.id.calendar_content);

        mCalendarSelector = new CalendarSelector(this, 0, this);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCalendarSelector.show(detailTextView);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_calendar);
    }

    @Override
    public void transmitPeriod(HashMap<String, String> result) {
    }
}
