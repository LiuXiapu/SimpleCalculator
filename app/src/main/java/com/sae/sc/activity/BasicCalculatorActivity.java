package com.sae.sc.activity;

import android.os.Build;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.sae.sc.R;
import com.sae.sc.activity.base.AbstractCalculatorActivity;
import com.sae.sc.fragment.KeyboardFragment;
import com.sae.sc.listener.KeyboardListener;
import com.sae.sc.utils.Evaluator;
import com.sae.sc.view.CalculatorEditText;
import com.sae.sc.view.MathFormulaView;

public class BasicCalculatorActivity extends AbstractCalculatorActivity
        implements KeyboardListener, View.OnKeyListener, Evaluator.EvaluateCallback {
    public static final String TAG = BasicCalculatorActivity.class.getSimpleName();

    /**
     *
     *  @location activity_basic_calculator
     *  @id drawer_layout
     *  @desc 包含侧边Navigation 与顶端ToolBar的总面板
     */
    DrawerLayout mWholePanelDrawerLayout;
    /**
     *
     *  @location activity_basic_calculator -->  abs_bar_content -> abs_content -> display_panel
     *  @id txtDisplay
     *  @desc 多重嵌套，为输入框，禁止软键盘与PC键盘输入；不显示光标，只有通过click事件改变text
     */
    CalculatorEditText mInputDisplay;
    /**
     *
     *  @location activity_basic_calculator -->  abs_bar_content -> abs_content -> display_panel
     *  @id math_view
     *  @desc 多重嵌套，暂定为结果输出框
     */
    MathFormulaView mMathView;
    /**
     *
     *  @lcoation activity_basic_calculator -->  abs_bar_content -> abs_content -> display_panel
     *  @id the_clear_animation
     *  @desc 暂定为动画背景的view
     */
    ViewGroup mDisplayForeground;
    /**
     *
     * @desc 记录当前计算状态
     */
    CalculatorState mCalculatorState = CalculatorState.INPUT;
    /**
     *
     *
     */
    Evaluator mEvaluator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_calculator);

        initView();
        initInputDisplay();
        initKeyboard();
    }

    private void initView() {
        mWholePanelDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mInputDisplay = (CalculatorEditText) findViewById(R.id.txtDisplay);
        mMathView = (MathFormulaView) findViewById(R.id.math_view);
        mDisplayForeground = (ViewGroup) findViewById(R.id.the_clear_animation);

        mInputDisplay.setText(null);
        mMathView.setText(null);

        //实时计算输入的结果
        mInputDisplay.setOnKeyListener(this);
    }

    private void initInputDisplay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mInputDisplay.setShowSoftInputOnFocus(false);
        }
        //关闭软键盘和光标
        mInputDisplay.setInputType(InputType.TYPE_NULL);
        mInputDisplay.setFocusable(false);
        mInputDisplay.setAutoSuggestEnable(false);

        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mInputDisplay.getWindowToken(), 0);
    }

    private void initKeyboard() {
        KeyboardFragment keyboardFragment = KeyboardFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_keyboard, keyboardFragment, KeyboardFragment.TAG);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onInsert(String text) {
        if (mCalculatorState == CalculatorState.ERROR || mCalculatorState == CalculatorState.RESULT) {
            setState(CalculatorState.INPUT);
            mInputDisplay.clear();
        }
        mInputDisplay.insert(text);
    }

    @Override
    public void onDelete() {
        mInputDisplay.backspace();
    }

    @Override
    public void onClear() {
        mInputDisplay.clear();

    }

    @Override
    public void onEqual() {
        String text = mInputDisplay.getCleanText();
        setState(CalculatorState.EVALUATE);

        //mEvaluator.evaluate(text, BasicCalculatorActivity.this);
    }

    @Override
    public void onError(String errorResourceId) {
        Toast.makeText(this, "未实现", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        switch (keyCode) {
            //小键盘
            case KeyEvent.KEYCODE_NUMPAD_ENTER:
            case KeyEvent.KEYCODE_ENTER:
                //松开按钮
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    onEqual();
                }
                return true;
        }
        return false;
    }

    private void setState(CalculatorState state) {
        mCalculatorState = state;
    }

    @Override
    public void onEvaluated(String expr, String result, int errorResourceId) {
        if (errorResourceId == RESULT_OK) {
            if (mCalculatorState == CalculatorState.EVALUATE) {
                onResult(result);
            } else if (mCalculatorState == CalculatorState.INPUT) {
                if (result == null) {
                    mMathView.setText("");
                } else {
                    mMathView.setText(result);
                }
            }
        }
    }

    @Override
    public void onCalculateError(Exception e) {
        if (mCalculatorState == CalculatorState.INPUT) {
            mMathView.setText("");
        } else if (mCalculatorState == CalculatorState.EVALUATE) {
            onError("Error: " + e.getMessage());
        }
    }

    public void onResult(final String result) {
        mInputDisplay.post(new Runnable() {
            @Override
            public void run() {
                mInputDisplay.setText(result.replace("\\", "").replace("\n", ""));
            }
        });
        mMathView.setText("");
    }

    public enum CalculatorState {
        INPUT, RESULT, ERROR, EVALUATE
    }
}
