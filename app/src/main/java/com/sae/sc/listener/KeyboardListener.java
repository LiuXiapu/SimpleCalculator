package com.sae.sc.listener;

public interface KeyboardListener {
    void insertText(String text);

    void onDelete();

    void onClear();

    void onEqual();

    void onError(final String errorResourceId);
}
