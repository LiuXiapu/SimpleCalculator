package com.sae.sc.view.base;

import android.content.Context;
import android.util.AttributeSet;

import static com.sae.sc.utils.FontManager.loadTypefaceFromAsset;

public class BaseEditText extends AutoCompleteFunctionEditText {
    public BaseEditText(Context context) {
        super(context);
        setup(context);
    }

    public BaseEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);

    }

    public BaseEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context);
    }

    private void setup(Context context) {
        if (!isInEditMode()) {
            setTypeface(loadTypefaceFromAsset(context));
        }
    }
}
