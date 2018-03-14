package com.sae.sc.activity;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.sae.sc.R;
import com.sae.sc.activity.base.AbstractCalculatorActivity;
import com.sae.sc.fragment.KeyboardFragment;
import com.sae.sc.listener.KeyboardListener;

public class BasicCalculatorActivity extends AbstractCalculatorActivity
        implements KeyboardListener {
    public static final String TAG = BasicCalculatorActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_calculator);

        initView();
        initKeyboard();

    }

    @Override
    public void insertText(String s) {

    }

    @Override
    public void insertOperator(String s) {

    }

    @Override
    public String getTextClean() {
        return null;
    }

    @Override
    public void setTextDisplay(String text) {

    }

    private void initView() {

    }

    private void initKeyboard() {
        Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
        KeyboardFragment keyboardFragment = KeyboardFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_keyboard, keyboardFragment, KeyboardFragment.TAG);
        ft.commitAllowingStateLoss();
    }


    @Override
    public void onResult(String result) {

    }

    @Override
    public void onError(String errorResourceId) {

    }

    @Override
    public void onDelete() {

    }


    @Override
    public void onEqual() {

    }

    @Override
    public void clickClear() {

    }
}
