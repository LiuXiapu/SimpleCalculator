package com.sae.sc.activity.base;

import android.support.annotation.Nullable;
import android.os.Bundle;

import com.sae.sc.i.ICalculator;

public abstract class AbstractCalculatorActivity extends AbstractNavDrawerActivity implements ICalculator {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * insert text to display - not clear screen
     * <p/>
     * use only for  calculator (base, science, complex)
     *
     * @param s - text
     */
    public abstract void insertText(String s);

    /**
     * insert operator to display
     * not clear display
     *
     * @param s - operator
     */
    public abstract void insertOperator(String s);

    /**
     * get text input display
     * <p/>
     * use only for  calculator (base, science, complex)
     *
     * @return - string text input
     */
    public abstract String getTextClean();


    /**
     * clear screen
     * <p/>
     * set text display - use only for  calculator (base, science, complex)
     * <p/>
     * set text for input edit text (eval)
     * <p/>
     * set text for matrix (matrix cal)
     * <p/>
     * set text for linear eval
     *
     * @param text - string input
     */
    public abstract void setTextDisplay(String text);
}
