package com.sae.sc.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.sae.sc.i.ICalculatorInput;
import com.sae.sc.view.base.ResizingEditText;

public class CalculatorEditText extends ResizingEditText implements ICalculatorInput {
    public static final String TAG = "CalculatorEditText";

    public CalculatorEditText(Context context) {
        super(context);
    }

    public CalculatorEditText(Context context, AttributeSet attr) {
        super(context, attr);
    }


    @Override
    public void clear() {

    }

    @Override
    public void insert() {

    }

    @Override
    public void backspace() {

    }
}
