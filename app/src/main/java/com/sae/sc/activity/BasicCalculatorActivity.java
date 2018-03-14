package com.sae.sc.activity;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.sae.sc.R;
import com.sae.sc.activity.base.AbstractCalculatorActivity;
import com.sae.sc.fragment.KeyboardFragment;
import com.sae.sc.listener.KeyboardListener;
import com.sae.sc.view.ResizingEditText;

public class BasicCalculatorActivity extends AbstractCalculatorActivity
        implements KeyboardListener {
    public static final String TAG = BasicCalculatorActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_calculator);

        initView();
        hideSoftKeyboard();
        initKeyboard();

    }

    private void hideSoftKeyboard() {
        final ResizingEditText inputView = (ResizingEditText) findViewById(R.id.txtDisplay);
        //关闭软键盘和光标
        inputView.setInputType(InputType.TYPE_NULL);

        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inputView.getWindowToken(), 0);

        //禁止所有输入，只能通过按钮更改edittext
        inputView.setFocusable(false);
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
