package com.sae.sc.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.wltea.expression.ExpressionEvaluator;


public class Evaluator {
    public static final String TAG = "Evaluator";

    public static final int INPUT_EMPTY = 2;

    private Evaluator() {
    }

    @NonNull
    public static Evaluator newInstance() {
        return new Evaluator();
    }

    public void evaluate(String expression, EvaluateCallback callback) {
        if (expression.isEmpty()) {
            callback.onEvaluated(expression, "", INPUT_EMPTY);
            //callback.onCalculateError(null);
            return;
        }

        String result = Resolver.resolveFromString(expression);

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
