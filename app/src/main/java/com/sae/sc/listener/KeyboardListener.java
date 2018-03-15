package com.sae.sc.listener;

public interface KeyboardListener {
    void insertText(String text);

    void onDefineAndCalc();

    void clickFactorPrime();

    void closeMathView();

    void shareText();

    void onDelete();

    void clickClear();

    void onEqual();

    void insertOperator(String op);

    void onResult(final String result);

    void onError(final String errorResourceId);
}
