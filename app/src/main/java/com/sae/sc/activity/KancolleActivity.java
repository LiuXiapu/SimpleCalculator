package com.sae.sc.activity;

import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.sae.sc.R;
import com.sae.sc.activity.base.AbstractNavDrawerActivity;

public class KancolleActivity extends AbstractNavDrawerActivity
        implements AdapterView.OnItemSelectedListener, CheckBox.OnCheckedChangeListener {
    public static final String TAG = "KancolleActivity";

    Spinner kanmusuSelectSpinner;
    Spinner nowLvSelectSpinner;
    Spinner targetLvSelectSpinner;
    TextView nowExpTextView;
    TextView targetExpTextView;
    Spinner seaSelectSpinner;
    Spinner rateSelectSpinner;
    CheckBox flagshipSelectCheckBox;
    CheckBox mvpSelectCheckBox;
    TextView seaExpTextView;
    TextView attackTimesTextView;
    TextView nextLvExpTextView;

    Integer[] levels;
    String[] seas;
    String[] rates;
    int[] exps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kancolle);

        initData();
        initView();
    }

    private void initView() {
        kanmusuSelectSpinner = (Spinner) findViewById(R.id.kanmusu_select);
        nowLvSelectSpinner = (Spinner) findViewById(R.id.now_lv_select);
        targetLvSelectSpinner = (Spinner) findViewById(R.id.target_lv_select);
        nowExpTextView = (TextView) findViewById(R.id.now_exp);
        targetExpTextView = (TextView) findViewById(R.id.target_exp);
        seaSelectSpinner = (Spinner) findViewById(R.id.sea_select);
        rateSelectSpinner = (Spinner) findViewById(R.id.rate_select);
        flagshipSelectCheckBox = (CheckBox) findViewById(R.id.flagship_select);
        mvpSelectCheckBox = (CheckBox) findViewById(R.id.mvp_select);
        seaExpTextView = (TextView) findViewById(R.id.sea_exp);
        attackTimesTextView = (TextView) findViewById(R.id.attack_times);
        nextLvExpTextView = (TextView) findViewById(R.id.next_lv_exp);

        ArrayAdapter<CharSequence> adapter;

        adapter = ArrayAdapter.createFromResource(this, R.array.kanmusu, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        kanmusuSelectSpinner.setAdapter(adapter);
        kanmusuSelectSpinner.setOnItemSelectedListener(this);

        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, levels);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        nowLvSelectSpinner.setAdapter(adapter);
        nowLvSelectSpinner.setOnItemSelectedListener(this);
        targetLvSelectSpinner.setAdapter(adapter);
        targetLvSelectSpinner.setOnItemSelectedListener(this);

        adapter = ArrayAdapter.createFromResource(this, R.array.seas, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        seaSelectSpinner.setAdapter(adapter);
        seaSelectSpinner.setOnItemSelectedListener(this);

        adapter = ArrayAdapter.createFromResource(this, R.array.rates, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        rateSelectSpinner.setAdapter(adapter);
        rateSelectSpinner.setOnItemSelectedListener(this);

        flagshipSelectCheckBox.setOnCheckedChangeListener(this);
        mvpSelectCheckBox.setOnCheckedChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_kancolle);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void initData() {
        levels = new Integer[165];
        for (int i = 0; i < levels.length; ++i) {
            levels[i] = i + 1;
        }
        seas = getResources().getStringArray(R.array.seas);
        rates = getResources().getStringArray(R.array.rates);
        exps = getResources().getIntArray(R.array.exps);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
