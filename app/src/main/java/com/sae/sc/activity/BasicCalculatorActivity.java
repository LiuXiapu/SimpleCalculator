package com.sae.sc.activity;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.sae.sc.R;
import com.sae.sc.activity.base.AbstractCalculatorActivity;
import com.sae.sc.fragment.KeyboardFragment;
import com.sae.sc.listener.AnimationFinishedListener;
import com.sae.sc.listener.KeyboardListener;
import com.sae.sc.utils.Evaluator;
import com.sae.sc.view.CalculatorEditText;
import com.sae.sc.view.MathFormulaView;
import com.sae.sc.view.RevealView;

import static com.sae.sc.utils.Evaluator.INPUT_EMPTY;

public class BasicCalculatorActivity extends AbstractCalculatorActivity
        implements KeyboardListener, TextWatcher, Evaluator.EvaluateCallback {
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
    TextView mMathView;
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

    Integer defaultColor;

    boolean isEqualPressed = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_calculator);

        mEvaluator = Evaluator.newInstance();

        initView();
        initInputDisplay();
        initKeyboard();
    }

    private void initView() {
        mWholePanelDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mInputDisplay = (CalculatorEditText) findViewById(R.id.txtDisplay);
        mMathView = (TextView) findViewById(R.id.math_view);
        mDisplayForeground = (ViewGroup) findViewById(R.id.the_clear_animation);

        mInputDisplay.setText(null);
        mMathView.setText(null);

        defaultColor = mMathView.getCurrentTextColor();
        //实时计算输入的结果
        mInputDisplay.addTextChangedListener(this);
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
        Log.i("main", "onInsert()");
        if (mCalculatorState == CalculatorState.ERROR) {
            setState(CalculatorState.INPUT);
            mMathView.setText(null);

            mMathView.setTextColor(defaultColor);
        }
        mInputDisplay.insert(text);
    }

    @Override
    public void onDelete() {
        mInputDisplay.backspace();
        setState(CalculatorState.INPUT);

        if (mInputDisplay.getEditableText().length() == 0) {
            mMathView.setText(null);
        }
    }

    @Override
    public void onClear() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = this.getTheme();
            theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
            int color = typedValue.data;
            animateRipple(mDisplayForeground, null, color, new AnimationFinishedListener() {
                @Override
                public void onAnimationFinished() {
                    setState(CalculatorState.INPUT);
                    mInputDisplay.clear();
                    mMathView.setText(null);
                }
            }, true);
        } else {
            mInputDisplay.clear();
            mMathView.setText(null);
            setState(CalculatorState.INPUT);
        }
    }

    @Override
    public void onEqual() {
        onEqual(false);
    }

    @Override
    public void onEqual(boolean isPressEqual) {
        isEqualPressed  = isPressEqual;

        Log.i("main", "onEqual()");
        String text = mInputDisplay.getCleanText();
        setState(CalculatorState.EVALUATE);

        mEvaluator.evaluate(text, BasicCalculatorActivity.this);
    }

    @Override
    public void onError(final String error) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = this.getTheme();
            theme.resolveAttribute(R.attr.colorResultError, typedValue, true);
            int color = typedValue.data;
            animateRipple(mDisplayForeground, null, color, new AnimationFinishedListener() {
                @Override
                public void onAnimationFinished() {
                    setState(CalculatorState.ERROR);
                    mMathView.setTextColor(Color.RED);
                    mMathView.setText(error);
                }
            }, true);
        } else {
            setState(CalculatorState.ERROR);
            mMathView.setTextColor(Color.RED);
            mMathView.setText(error);
        }
    }

    private void setState(CalculatorState state) {
        mCalculatorState = state;
    }

    @Override
    public void onEvaluated(String expr, String result, int errorResourceId) {
        Log.i("main", "onEvaluated() " + mCalculatorState.name());

        if (errorResourceId == RESULT_OK) {
            if (mCalculatorState == CalculatorState.EVALUATE) {
                onResult(result);
                setState(CalculatorState.RESULT);

                if (isEqualPressed) {
                    mMathView.setText(null);
                }
            }
        } else if (errorResourceId == INPUT_EMPTY) {
            setState(CalculatorState.INPUT);
        }
    }

    @Override
    public void onCalculateError(String errorString) {
        if (mCalculatorState == CalculatorState.INPUT) {
            mMathView.setText("");
        } else if (mCalculatorState == CalculatorState.EVALUATE) {
            onError(errorString);
        }
    }

    public void onResult(final String result) {
        mMathView.setText(result);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateRipple(final ViewGroup foreground,
                               @Nullable View sourceView,
                               int color, final Animator.AnimatorListener listener, final boolean out) {
        if (color == -1) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = this.getTheme();
            theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
            color = typedValue.data;
        } else if (color == -2) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = this.getTheme();
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
            color = typedValue.data;
        }
        //create new reveal view
        final RevealView revealView = new RevealView(this);
        revealView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        revealView.setRevealColor(color);
        //add to viewgroup
        foreground.addView(revealView);

        final Animator revealAnimator;
        final int[] clearLocation = new int[2];
        if (sourceView != null) {
            sourceView.getLocationInWindow(clearLocation);
            clearLocation[0] += sourceView.getWidth() / 2;
            clearLocation[1] += sourceView.getHeight() / 2;
        } else {
            clearLocation[0] = foreground.getWidth() / 2;
            clearLocation[1] = foreground.getHeight() / 2;
        }
        final int revealCenterX = clearLocation[0] - revealView.getLeft();
        final int revealCenterY = clearLocation[1] - revealView.getTop();

        final double x1_2 = Math.pow(revealView.getLeft() - revealCenterX, 2);
        final double x2_2 = Math.pow(revealView.getRight() - revealCenterX, 2);
        final double y_2 = Math.pow(revealView.getTop() - revealCenterY, 2);
        final float revealRadius = (float) Math.max(Math.sqrt(x1_2 + y_2), Math.sqrt(x2_2 + y_2));

        if (out)
            revealAnimator = ViewAnimationUtils.createCircularReveal(
                    revealView, revealCenterX, revealCenterY, 0.0f, revealRadius);
        else
            revealAnimator = ViewAnimationUtils.createCircularReveal(
                    revealView, revealCenterX, revealCenterY, revealRadius, 0f);

        revealAnimator.setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
        if (listener != null) revealAnimator.addListener(listener);
        revealAnimator.addListener(new AnimationFinishedListener() {
            @Override
            public void onAnimationFinished() {
                foreground.removeView(revealView);
            }
        });
        revealAnimator.start();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        onEqual();
        setState(CalculatorState.INPUT);
    }

    @Override
    public void onResume() {
        super.onResume();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_calculator);
    }

    public enum CalculatorState {
        INPUT, RESULT, ERROR, EVALUATE
    }
}
