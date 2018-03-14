package com.sae.sc.i;

public interface ICalculator {
    void onResult(final String result);

    void onError(final String errorResourceId);

    void onDelete();

    //void clickClear();

    void onEqual();
}
