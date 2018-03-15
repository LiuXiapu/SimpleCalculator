package com.sae.sc.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/*
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.eval.TeXUtilities;
import org.matheclipse.core.interfaces.IExpr;
 */


public class Evaluator {
    public static final String TAG = "Evaluator";

    /*
    public static final int RESULT_OK = 1;
    public static final int RESULT_ERROR = -1;
    public static final int INPUT_EMPTY = 2;
    public static final int RESULT_ERROR_WITH_INDEX = 3;

    private final ExprEvaluator mExprEvaluator;
    private final TeXUtilities mTexEngine;

    private Evaluator() {
        mExprEvaluator = new ExprEvaluator();
        mTexEngine = new TeXUtilities(mExprEvaluator.getEvalEngine(), true);
    }

    @NonNull
    public static Evaluator newInstance() {
        return new Evaluator();
    }

    @Nullable
    public static Exception getError(String expr) {
        try {
            EvalEngine.get().parse(expr);
        } catch (Exception e) {
            return e;
        }
        return null;
    }

    public void evaluate(String expression, EvaluateCallback callback) {
        if (expression.isEmpty()) {
            callback.onEvaluated(expression, "", INPUT_EMPTY);
            return;
        }

        try {
            IExpr iExpr = mExprEvaluator.evaluate("N(" + expression + ")");
            callback.onEvaluated(expression, iExpr.toString(), RESULT_OK);
        } catch (Exception e) {
            callback.onCalculateError(e);
        }
    }
    */

    public interface EvaluateCallback {
        void onEvaluated(String expr, String result, int errorResourceId);

        void onCalculateError(Exception e);
    }

    public enum InputType {
        RESULT_OK, RESULT_ERROR, INPUT_EMPTY, RESULT_ERROR_WITH_INDEX
    }

}
