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
import android.widget.Toast;

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

    float[] grade_rates;
    int[] sea_exps;

    int[] lv_exps;

    private int nowLv;
    private int targetLv;

    /**
     *
     *  @desc 旗舰 MVP 补正
     */
    private float correction;

    /**
     *
     *  @desc 胜利程度补正，S A B C D
     */
    private float rate_correction;

    /**
     *
     *  @desc 不同海域经验值
     */
    private int sea_exp;

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

        correction = 1.0F;

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_kancolle);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.now_lv_select:
                nowLv = position + 1;
                break;
            case R.id.target_lv_select:
                targetLv = position + 1;
                break;
            case R.id.rate_select:
                rate_correction = grade_rates[position];
                break;
            case R.id.sea_select:
                sea_exp = sea_exps[position];
                break;

        }

        calculateExp();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void initData() {
        levels = new Integer[165];
        for (int i = 0; i < levels.length; ++i) {
            levels[i] = i + 1;
        }

        sea_exps = getResources().getIntArray(R.array.exps);
        grade_rates = new float[]{1.2F, 1.0F, 1.0F, 0.8F, 0.7F};

        int[] lv_exps = {
                0, 100, 300, 600, 1000, 1500, 2100, 2800, 3600, 4500,
                5500, 6600, 7800, 9100, 10500, 12000, 13600, 15300, 17100, 19000,
                21000, 23100, 25300, 27600, 30000, 32500, 35100, 37800, 40600, 43500,
                46500, 49600, 52800, 56100, 59500, 63000, 66600, 70300, 74100, 78000,
                82000, 86100, 90300, 94600, 99000, 103500, 108100, 112800, 117600, 122500,
                127500, 132700, 138100, 143700, 149500, 155000, 161700, 168100, 174700, 181500, };
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.flagship_select:
                if (isChecked) {
                    correction *= 1.5;
                } else {
                    correction /= 1.5;
                }
                break;
            case R.id.mvp_select:
                if (isChecked) {
                    correction *= 2;
                } else {
                    correction /= 2;
                }
        }

        calculateExp();
    }

    private void calculateExp() {
        int nowExp = 0;
        int targetExp = 0;
        int seaExp = this.sea_exp;
        int attackTimes = 0;
        int remainExp = 0;


        reviseUI(nowExp, targetExp, seaExp, attackTimes, remainExp);
    }

    private void reviseUI(int nowExp, int targetExp, int seaExp, int attackTimes, int remainExp) {
        nowExpTextView.setText(nowExp + "");
        targetExpTextView.setText(targetExp + "");
        seaExpTextView.setText(seaExp + "");
        attackTimesTextView.setText(attackTimes + "");
        nextLvExpTextView.setText(remainExp + "");
    }
}
