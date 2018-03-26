package com.sae.sc.activity;

import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.sae.sc.R;
import com.sae.sc.activity.base.AbstractCalculatorActivity;
import com.sae.sc.fragment.BaseCalculatorKeyboardFragment;
import com.sae.sc.fragment.KeyboardFragment;
import com.sae.sc.listener.AnimationFinishedListener;
import com.sae.sc.listener.BaseCalculatorKeyboardListener;
import com.sae.sc.utils.Evaluator;
import com.sae.sc.view.CalcButton;
import com.sae.sc.view.CalculatorEditText;

public class BaseCalculatorActivity extends AbstractCalculatorActivity
        implements BaseCalculatorKeyboardListener, TextWatcher, Evaluator.EvaluateCallback {

    Evaluator mEvaluator;

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
     *  @lcoation activity_basic_calculator -->  abs_bar_content -> abs_content -> display_panel
     *  @id the_clear_animation
     *  @desc 暂定为动画背景的view
     */
    ViewGroup mDisplayForeground;

    CalcButton dotButton;

    Boolean isResultStatue = false;
    String nowBaseStatue = "dec";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_calculator);

        this.setTitle(R.string.base_title);

        mEvaluator = Evaluator.newInstance(this);

        initView();
        initInputDisplay();
        initKeyboard();
    }

    private void initView() {
        mWholePanelDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mInputDisplay = (CalculatorEditText) findViewById(R.id.txtDisplay);
        mDisplayForeground = (ViewGroup) findViewById(R.id.the_clear_animation);

        mInputDisplay.setText(null);
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
        BaseCalculatorKeyboardFragment keyboardFragment = BaseCalculatorKeyboardFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_keyboard, keyboardFragment, BaseCalculatorKeyboardFragment.TAG);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onResume() {
        super.onResume();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_base);
    }



    @Override
    public void afterTextChanged(Editable s) {
        if (dotButton == null) {
            dotButton = findViewById(R.id.btn_dot);
        } else {
            if (mInputDisplay.getText().toString().contains(".")) {
                dotButton.setEnabled(false);
            } else {
                dotButton.setEnabled(true);
            }
        }
    }

    @Override
    public void onInsert(String text) {
        if (isResultStatue) {
            mInputDisplay.clear();
            isResultStatue = false;
        }
        mInputDisplay.insert(text);
    }

    @Override
    public void onDelete() {
        mInputDisplay.backspace();
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
                    mInputDisplay.clear();
                }
            }, true);
        } else {
            mInputDisplay.clear();
        }
    }

    @Override
    public void onTransfer(final String base) {
        mEvaluator.transfer(mInputDisplay.getCleanText(), nowBaseStatue, base, this);

        nowBaseStatue = base;
    }

    @Override
    public void onEvaluated(String expr, String result, int errorResourceId) {
        mInputDisplay.clear();
        mInputDisplay.insert(result);

        isResultStatue = true;
    }

    @Override
    public void onCalculateError(String errorString) {
        Toast.makeText(this, "进制计算器一般不会出现错误", Toast.LENGTH_SHORT).show();
        mInputDisplay.clear();
        mInputDisplay.insert("?");

        isResultStatue = true;
    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
}
