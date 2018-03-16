package com.sae.sc.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class Evaluator {
    public static final String TAG = "Evaluator";
    public static final int INPUT_EMPTY = 2;

    private Resolver resolver;

    private Evaluator(Context context) {
        resolver = Resolver.newInstance(context);
    }

    @NonNull
    public static Evaluator newInstance(Context context) {
        return new Evaluator(context);
    }

    public void evaluate(String expression, EvaluateCallback callback) {
        if (expression.isEmpty()) {
            callback.onEvaluated(expression, "", INPUT_EMPTY);
            //callback.onCalculateError(null);
            return;
        }

        String result = resolver.resolveFromString(expression);

        if (result.startsWith("Error")) {
            callback.onCalculateError(result);
        } else {
            callback.onEvaluated(expression, result, Activity.RESULT_OK);
        }

    }


    public interface EvaluateCallback {
        void onEvaluated(String expr, String result, int errorResourceId);

        void onCalculateError(String errorString);
    }
}
