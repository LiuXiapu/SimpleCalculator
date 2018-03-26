package com.sae.sc.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.sae.sc.R;
import com.sae.sc.i.ICalculatorInput;
import com.sae.sc.view.base.ResizingEditText;

import java.util.Arrays;
import java.util.List;

public class CalculatorEditText extends ResizingEditText implements ICalculatorInput {
    public static final String TAG = "CalculatorEditText";

    private List<String> mKeywords;

    public CalculatorEditText(Context context) {
        super(context);
        setUp(context, null);
    }

    public CalculatorEditText(Context context, AttributeSet attr) {
        super(context, attr);
        setUp(context, attr);
    }

    private void setUp(Context context, @Nullable AttributeSet attrs) {
        invalidateKeywords(context);
    }

    public void invalidateKeywords(Context context) {
        mKeywords = Arrays.asList(
                context.getString(R.string.arcsin) + "(",
                context.getString(R.string.arccos) + "(",
                context.getString(R.string.arctan) + "(",
                context.getString(R.string.fun_sin) + "(",
                context.getString(R.string.fun_cos) + "(",
                context.getString(R.string.fun_tan) + "(",
                context.getString(R.string.fun_arccsc) + "(",
                context.getString(R.string.fun_arcsec) + "(",
                context.getString(R.string.fun_arccot) + "(",
                context.getString(R.string.fun_csc) + "(",
                context.getString(R.string.fun_sec) + "(",
                context.getString(R.string.fun_cot) + "(",
                context.getString(R.string.fun_log) + "(",
                context.getString(R.string.mod) + "(",
                context.getString(R.string.fun_ln) + "(",
                context.getString(R.string.op_cbrt) + "(",
                context.getString(R.string.tanh) + "(",
                context.getString(R.string.cosh) + "(",
                context.getString(R.string.sinh) + "(",
                context.getString(R.string.log2) + "(",
                context.getString(R.string.log10) + "(",
                context.getString(R.string.abs) + "(",
                context.getString(R.string.sgn) + "(",
                context.getString(R.string.floor) + "(",
                context.getString(R.string.ceil) + "(",
                context.getString(R.string.arctanh) + "(",
                context.getString(R.string.sum) + "(",
                context.getString(R.string.avg) + "(",
                context.getString(R.string.vari) + "(",
                context.getString(R.string.stdi) + "(",
                context.getString(R.string.mini) + "(",
                context.getString(R.string.maxi) + "(",
                context.getString(R.string.min) + "(",
                context.getString(R.string.max) + "(",
                context.getString(R.string.std) + "(",
                context.getString(R.string.mean) + "(",
                context.getString(R.string.sqrt_sym) + "(",
                context.getString(R.string.log2) + "(",
                context.getString(R.string.log10) + "(",
                context.getString(R.string.cot) + "(",
                context.getString(R.string.exp) + "(",
                context.getString(R.string.sign) + "(",
                context.getString(R.string.arg) + "(",
                context.getString(R.string.gcd_up) + "(",
                context.getString(R.string.log2) + "(",
                context.getString(R.string.ln) + "(",
                context.getString(R.string.ln) + "(",
                context.getString(R.string.log2) + "(",
                context.getString(R.string.arcsinh) + "(",
                context.getString(R.string.arccosh) + "(",
                context.getString(R.string.arctanh) + "(",
                context.getString(R.string.op_cbrt) + "(",
                context.getString(R.string.permutations) + "(",
                context.getString(R.string.binomial) + "(",
                context.getString(R.string.trunc) + "(",
                context.getString(R.string.max) + "(",
                context.getString(R.string.min) + "(",
                context.getString(R.string.mod) + "(",
                context.getString(R.string.gcd) + "(",
                context.getString(R.string.lcm) + "(",
                context.getString(R.string.sign) + "(",
                context.getString(R.string.rnd) + "("
        );
    }

    @Override
    public void clear() {
        setText(null);
    }

    @Override
    public void insert(String delta) {
        String currentText = getText().toString();

        getText().insert(getSelectionStart(), delta);
        setSelection(getText().length());

        for (String word : mKeywords) {
            if ((delta + "(").equals(word)) {
                getText().insert(getSelectionStart(), "(");
                setSelection(getText().length());
            }
        }
    }

    @Override
    public void backspace() {
        String currentText = getText().toString();
        int selectionHandle = getSelectionStart();

        //如果有关键词
        for (String word: mKeywords) {
            if (currentText.endsWith(word)) {
                int deletionLength = word.length();
                String newText = currentText.substring(0, currentText.length() - deletionLength);
                setText(newText);
                setSelection(getText().length());
                return;
            }
        }

        //非关键词
        if (selectionHandle != 0) {
            setText(currentText.substring(0, currentText.length() - 1));
        }

        setSelection(getText().length());
    }
}
