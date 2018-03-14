package com.sae.sc.view;


import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Html;
import android.util.AttributeSet;

import com.sae.sc.utils.FontManager;

public class CalcButton extends android.support.v7.widget.AppCompatButton {

    public CalcButton(Context context) {
        super(context);
        setup(context);

    }

    public CalcButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);

    }

    public CalcButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setup(context);

    }

    private void setup(Context context) {
        setTypeface(FontManager.loadTypefaceFromAsset(context));

        String text = getText().toString();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
        } else {
            setText(Html.fromHtml(text));
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled) {
            animate().alpha(1f).start();
        } else {
            animate().alpha(0.2f).start();
        }
        super.setEnabled(enabled);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selected) {
            animate().alpha(1f).start();
        } else {
            animate().alpha(0.3f).start();
        }
    }
}
