package com.sae.sc.listener;

/**
 * Created by Sae on 2018/3/26.
 */

public interface BaseKeyboardListener {
    void onInsert(String text);

    void onDelete();

    void onClear();

}
