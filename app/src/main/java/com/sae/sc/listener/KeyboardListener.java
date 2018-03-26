package com.sae.sc.listener;

public interface KeyboardListener extends BaseKeyboardListener {
    void onEqual();

    void onEqual(boolean isPressEqual);

    void onError(final String errorResourceId);
}
