package com.sae.sc.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.preference.PreferenceManager;

import com.sae.sc.R;

public final class FontManager {

    private static Typeface typeface = null;

    public static Typeface loadTypefaceFromAsset(Context context) {
        if (typeface != null) {
            return typeface;
        }

        String path = "fonts/";
        String font = context.getString(R.string.font_light);

        if (font.equals(context.getString(R.string.font_black))) {
            path = path + "Roboto-Black.ttf";
        } else if (font.equals(context.getString(R.string.font_light))) {
            path = path + "Roboto-Light.ttf";
        } else if (font.equals(context.getString(R.string.font_bold))) {
            path = path + "Roboto-Bold.ttf";
        } else if (font.equals(context.getString(R.string.font_italic))) {
            path = path + "Roboto-Italic.ttf";
        } else if (font.equals(context.getString(R.string.font_thin))) {
            path = path + "Roboto-Thin.ttf";
        }
        AssetManager assetManager = context.getAssets();
        typeface = Typeface.createFromAsset(assetManager, path);
        return typeface;
    }
}
