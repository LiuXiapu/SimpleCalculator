package com.sae.sc.utils;

import android.content.Context;

import com.sae.sc.R;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Sae on 2018/3/16.
 */

public class Resolver {
    protected Context context;

    private List<String> mKeywords;
    private List<String> mOperators;
    private List<String> mConstants;
    private List<String> mParen;

    private Resolver(Context context) {
        this.context = context;

        initKeywords();
    }

    public static Resolver newInstance(Context context) {
        return new Resolver(context);
    }

    private void initKeywords() {
        mKeywords = Arrays.asList(
                context.getString(R.string.arcsin),
                context.getString(R.string.arccos),
                context.getString(R.string.arctan),
                context.getString(R.string.sin),
                context.getString(R.string.cos),
                context.getString(R.string.tan),

                context.getString(R.string.fun_ln),
                context.getString(R.string.exp),
                context.getString(R.string.log10),
                context.getString(R.string.sqrt_sym)
        );

        mOperators = Arrays.asList(
                context.getString(R.string.mul),
                context.getString(R.string.div),
                context.getString(R.string.plus),
                context.getString(R.string.minus),

                context.getString(R.string.power)
        );

        mParen = Arrays.asList(
                context.getString(R.string.leftParen),
                context.getString(R.string.rightParen)
        );

        mConstants = Arrays.asList(
                context.getString(R.string.constant_e),
                context.getString(R.string.pi_symbol)
        );
    }


    /**
     *
     * @param expression math expression
     * @return "Error" + errorReason when error happened
     *          Number.toString() when calculate rightly
     */
    public String resolveFromString(final String expression) {
        System.out.println(expression);

        List<String> elements = getElementsFromExpression(expression);
        printList(elements);

        //atan分解为a tan
        elements = fixAError(elements);
        printList(elements);

        elements = addZeroAndMulOp(elements);
        printList(elements);

        return "Error";
    }

    private List<String> addZeroAndMulOp(List<String> elements) {
        // 添加0
        if (elements.get(0).equals("+") | elements.get(0).equals("-")) {
            elements.add(0, "0");
        }

        for (int i = 1; i < elements.size(); ++ i) {
            String nowElement = elements.get(i);
            if (nowElement.equals("+") | nowElement.equals("-")) {
                String prevElement = elements.get(i - 1);
                if (prevElement.equals(("("))) {
                    elements.add(i ++, "0");
                }
            }
        }

        // )(  连续括号
        for (int i = 1; i < elements.size(); ++ i) {
            String nowElement = elements.get(i);
            if (nowElement.equals("(")) {
                String prevElement = elements.get(i - 1);
                if (prevElement.equals((")"))) {
                    elements.add(i ++, "*");
                }
            }
        }

        // pi E
        // >= 3 xxxExxxx
        for (int i = 1; i < elements.size() - 1; ++ i) {
            String nowElement = elements.get(i);
            if (isConstant(nowElement)) {

                String leftElement = elements.get(i - 1);
                if (leftElement.equals("(") || isOperator(leftElement)) {
                } else {
                    elements.add(i++, "*");
                }

                String rightElement = elements.get(i + 1);
                if (rightElement.equals(")") || isOperator(rightElement)) {
                } else {
                    elements.add(++ i, "*");
                }
            }
        }

        // xxxxE  Exxxx
        if (elements.size() >= 3) {
            if (isConstant(elements.get(elements.size() - 1))) {
                String leftElement = elements.get(elements.size() - 2);
                if (leftElement.equals("(") || isOperator(leftElement)) {
                } else {
                    elements.add(elements.size() - 1, "*");
                }
            }
            if (isConstant(elements.get(0))) {
                String rightElement = elements.get(1);
                if (rightElement.equals(")") || isOperator(rightElement)) {
                } else {
                    elements.add(1, "*");
                }
            }
        }

        // == 2 xE Ex
        if (elements.size() == 2) {
            if (isConstant(elements.get(0)) || isConstant(elements.get(1))) {
                elements.add(1, "*");
            }
        }

        // 3func    )func
        for (int i = 1; i < elements.size(); ++ i) {
            String nowElement = elements.get(i);
            if (isFunction(nowElement)) {
                String leftElement = elements.get(i - 1);
                if (leftElement.equals(")") || isNumberLike(leftElement)) {
                    elements.add(i ++, "*");
                }
            }
        }

        // 3(   )3
        for (int i = 1; i < elements.size(); ++ i) {
            String nowElement = elements.get(i);
            if (nowElement.equals("(")) {
                String leftElement = elements.get(i - 1);
                if (isNumberLike(leftElement)) {
                    elements.add(i ++, "*");
                }
            } else if (isNumberLike(nowElement)) {
                String leftElement = elements.get(i - 1);
                if (leftElement.equals(")")) {
                    elements.add(i ++, "*");
                }
            }
        }

        return elements;
    }

    private List<String> fixAError(List<String> elements) {
        List<String> fixedElements = new LinkedList<>();

        for (int i = 0; i < elements.size(); ++ i) {
            String nowElement = elements.get(i);
            if (! nowElement.equals("a")) {
                fixedElements.add(nowElement);
            } else {
                fixedElements.add(nowElement + elements.get(++ i));
            }
        }

        return fixedElements;
    }

    private List<String> getElementsFromExpression(final String expression) {
        List<String> elements = new LinkedList<>();
        List<String> tmpStack;

        elements.add(expression);

        for (String constantToken: mConstants) {
            tmpStack = new LinkedList<>();
            for (int i = 0; i < elements.size(); ++ i) {
                String subExpression = elements.get(i);
                List<String> slicesWithTokens = getSlicesWithTokens(subExpression, constantToken);
                tmpStack.addAll(slicesWithTokens);
            }
            elements = tmpStack;

        }

        for (String keywordToken: mKeywords) {
            tmpStack = new LinkedList<>();
            for (int i = 0; i < elements.size(); ++ i) {
                String subExpression = elements.get(i);
                List<String> slicesWithTokens = getSlicesWithTokens(subExpression, keywordToken);
                tmpStack.addAll(slicesWithTokens);
            }
            elements = tmpStack;
        }

        for (String operatorToken: mOperators) {
            tmpStack = new LinkedList<>();
            for (int i = 0; i < elements.size(); ++ i) {
                String subExpression = elements.get(i);
                List<String> slicesWithTokens = getSlicesWithTokens(subExpression, operatorToken);
                tmpStack.addAll(slicesWithTokens);
            }
            elements = tmpStack;
        }

        for (String parenToken: mParen) {
            tmpStack = new LinkedList<>();
            for (int i = 0; i < elements.size(); ++ i) {
                String subExpression = elements.get(i);
                List<String> slicesWithTokens = getSlicesWithTokens(subExpression, parenToken);
                tmpStack.addAll(slicesWithTokens);
            }
            elements = tmpStack;
        }

        return elements;
    }

    private List<String> getSlicesWithTokens(final String expression, final String token) {
        String[] slicesWithSpace = StringUtils.splitByWholeSeparatorPreserveAllTokens(expression, token);
        String[] slicesWithSpaceAndTokens = new String[2 * slicesWithSpace.length - 1];
        for (int i = 0; i < slicesWithSpace.length - 1; ++ i) {
            slicesWithSpaceAndTokens[2 * i] = slicesWithSpace[i];
            slicesWithSpaceAndTokens[2 * i + 1] = token;
        }
        slicesWithSpaceAndTokens[2 * slicesWithSpace.length - 2] = slicesWithSpace[slicesWithSpace.length - 1];

        List<String> slicesWithTokens = new ArrayList<>();
        for (String slicesWithSpaceAndToken : slicesWithSpaceAndTokens) {
            if (! slicesWithSpaceAndToken.equals("")) {
                slicesWithTokens.add(slicesWithSpaceAndToken);
            }
        }

        return slicesWithTokens;
    }

    private void printList(List<? extends Object> objects) {
        for (Object object: objects) {
            System.out.print(object.toString() + "  ");
        }
        System.out.println();
    }

    private boolean isConstant(String expr) {
        return mConstants.contains(expr);
    }

    private boolean isFunction(String expr) {
        return mKeywords.contains(expr);
    }

    private boolean isOperator(String expr) {
        return mOperators.contains(expr);
    }

    private boolean isParen(String expr) {
        return mParen.contains(expr);
    }

    private boolean isNumberLike(String expr) {
        return (!isConstant(expr)) && (!isFunction(expr)) && (!isOperator(expr)) && (!isParen(expr));
    }
}
