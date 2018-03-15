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
            return;
        }

        Object result;
        try {
            result = ExpressionEvaluator.evaluate(expression);
            callback.onEvaluated(expression, result.toString(), Activity.RESULT_OK);
        } catch (Exception e) {
            callback.onCalculateError(e);
        }
    }


    public interface EvaluateCallback {
        void onEvaluated(String expr, String result, int errorResourceId);

        void onCalculateError(Exception e);
    }
}
