package com.sae.sc.listener;

public interface KeyboardListener {
    void onInsert(String text);

    void onDelete();

    void onClear();

    void onEqual();

    void onError(final String errorResourceId);
}
