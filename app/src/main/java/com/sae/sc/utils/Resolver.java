package com.sae.sc.utils;

import android.content.Context;
import android.util.Log;

import com.sae.sc.R;

import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    private int addParenNum = 0;


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

        boolean isCorrect = syntaxElements(elements);
        System.out.println(isCorrect);

        if (isCorrect) {
            try {
                elements = addParen(elements);
                printList(elements);
                return tryCalculate(elements);
            } catch (Exception e) {
                e.printStackTrace();
                Log.w("cal", e.getMessage());
                return "Error: Calculate Error. " + e.getMessage();
            }
        } else {
            return "Error: Syntax Failed. Wrong Format.";
        }
    }

    private String tryCalculate(List<String> elements) throws Exception {
        List<Object> parsedElements = new LinkedList<>();
        for (String element: elements) {
            if (isNumberLike(element)) {
                parsedElements.add(Double.parseDouble(element));
            } else {
                parsedElements.add(element);
            }
        }

        printList(parsedElements);

        double result = calculateInner(parsedElements);

        return Double.toString(result);
    }

    private Double calculateInner(List<Object> elements) throws Exception {
        // 去括号
        List<Object> elementsWithoutParen = new LinkedList<>();
        for (int i = 0; i < elements.size(); ) {
            Object nowElement = elements.get(i);

            // 递归 寻找配对括号
            if (nowElement.equals("(")) {
                int leftCount = 1;
                for (int j = i + 1; j < elements.size(); ++ j) {
                    if (elements.get(j).equals("(")) {
                        ++ leftCount;
                    } else if (elements.get(j).equals(")")) {
                        if (-- leftCount == 0) {
                            List<Object> subList = elements.subList(i + 1, j);
                            double subResult = calculateInner(subList);
                            elementsWithoutParen.add(subResult);

                            i = j + 1;
                            break;
                        }
                    }
                }
            } else {
                elementsWithoutParen.add(nowElement);
                ++ i;
            }
        }

        // 去常数
        List<Object> elementsWithoutConstant = new LinkedList<>();
        for (int i = 0; i < elementsWithoutParen.size(); ++ i) {
            Object nowElement = elementsWithoutParen.get(i);
            if (nowElement instanceof String && isConstant((String) nowElement)) {
                elementsWithoutConstant.add(getConstantValue((String) nowElement));
            } else {
                elementsWithoutConstant.add(nowElement);
            }
        }

        // 去函数
        List<Object> elementsWithoutFunc = new LinkedList<>();
        for (int i = 0; i < elementsWithoutConstant.size(); ++ i) {
            Object nowElement = elementsWithoutConstant.get(i);
            if (nowElement instanceof String && isFunction((String) nowElement)) {
                Object nextElement = elementsWithoutParen.get(i + 1);
                if (nextElement instanceof Double) {
                    elementsWithoutFunc.add(getFunctionResult((String) nowElement, (double) nextElement));
                    ++ i;
                } else {
                    throw new Exception("unrecognized number: " + nextElement);
                }
            } else {
                elementsWithoutFunc.add(nowElement);
            }
        }

        // pow 乘方
        for (int i = 1; i < elementsWithoutFunc.size() - 1; ) {
            Object nowElement = elementsWithoutFunc.get(i);
            if (nowElement instanceof String && ((String) nowElement).equals(context.getString(R.string.power))) {
                Object leftElement = elementsWithoutFunc.get(i - 1);
                Object rightElement = elementsWithoutFunc.get(i + 1);
                if (leftElement instanceof Double && rightElement instanceof Double) {
                    double result = Math.pow((double) leftElement, (double) rightElement);

                    elementsWithoutFunc.remove(i - 1);
                    elementsWithoutFunc.remove(i - 1);
                    elementsWithoutFunc.remove(i - 1);
                    elementsWithoutFunc.add(i - 1, result);
                } else {
                    throw new Exception("unrecognized number: " + leftElement + ", " + rightElement);
                }
            } else {
                ++ i;
            }
        }

        // 乘除
        for (int i = 1; i < elementsWithoutFunc.size() - 1; ) {
            Object nowElement = elementsWithoutFunc.get(i);
            if (nowElement instanceof String &&
                    (((String) nowElement).equals(context.getString(R.string.mul))
                    || ((String) nowElement).equals(context.getString(R.string.div)))) {
                Object leftElement = elementsWithoutFunc.get(i - 1);
                Object rightElement = elementsWithoutFunc.get(i + 1);
                if (leftElement instanceof Double && rightElement instanceof Double) {
                    double result;
                    if (((String) nowElement).equals(context.getString(R.string.mul))) {
                        result = (double) leftElement * (double) rightElement;
                    } else {
                        result = (double) leftElement / (double) rightElement;
                    }

                    elementsWithoutFunc.remove(i - 1);
                    elementsWithoutFunc.remove(i - 1);
                    elementsWithoutFunc.remove(i - 1);
                    elementsWithoutFunc.add(i - 1, result);
                } else {
                    throw new Exception("unrecognized number: " + leftElement + ", " + rightElement);
                }
            } else {
                ++ i;
            }
        }

        // 加减
        for (int i = 1; i < elementsWithoutFunc.size() - 1; ) {
            Object nowElement = elementsWithoutFunc.get(i);
            if (nowElement instanceof String &&
                    (((String) nowElement).equals(context.getString(R.string.plus))
                            || ((String) nowElement).equals(context.getString(R.string.minus)))) {
                Object leftElement = elementsWithoutFunc.get(i - 1);
                Object rightElement = elementsWithoutFunc.get(i + 1);
                if (leftElement instanceof Double && rightElement instanceof Double) {
                    double result;
                    if (((String) nowElement).equals(context.getString(R.string.plus))) {
                        result = (double) leftElement + (double) rightElement;
                    } else {
                        result = (double) leftElement - (double) rightElement;
                    }

                    elementsWithoutFunc.remove(i - 1);
                    elementsWithoutFunc.remove(i - 1);
                    elementsWithoutFunc.remove(i - 1);
                    elementsWithoutFunc.add(i - 1, result);
                } else {
                    throw new Exception("unrecognized number: " + leftElement + ", " + rightElement);
                }
            } else {
                ++ i;
            }
        }

        System.out.println("length = " + elementsWithoutFunc.size());
        return (double) elementsWithoutFunc.get(0);
    }

    private double getFunctionResult(String funcName, double number) throws Exception {
        if (funcName.equals(context.getString(R.string.sin))) {
            return Math.sin(number);
        } else if (funcName.equals(context.getString(R.string.cos))) {
            return Math.cos(number);
        } else if (funcName.equals(context.getString(R.string.tan))) {
            return Math.tan(number);
        } else if (funcName.equals(context.getString(R.string.arcsin))) {
            return Math.asin(number);
        } else if (funcName.equals(context.getString(R.string.arccos))) {
            return Math.acos(number);
        } else if (funcName.equals(context.getString(R.string.arctan))) {
            return Math.atan(number);
        } else if (funcName.equals(context.getString(R.string.fun_ln))) {
            return Math.log(number);
        } else if (funcName.equals(context.getString(R.string.exp))) {
            return Math.exp(number);
        } else if (funcName.equals(context.getString(R.string.log10))) {
            return Math.log10(number);
        } else if (funcName.equals(context.getString(R.string.sqrt_sym))) {
            return Math.sqrt(number);
        } else {
            throw new Exception("unrecognized function: " + funcName);
        }
    }

    private double getConstantValue(String expr) throws Exception{
        if (expr.equals(context.getString(R.string.constant_e))) {
            return Math.E;
        } else if (expr.equals(context.getString(R.string.pi_symbol))) {
            return Math.PI;
        } else {
            throw new Exception("unrecognized constant: " + expr);
        }
    }

    private List<String> addParen(List<String> elements) {
        while (addParenNum -- > 0) {
            elements.add(")");
        }
        return elements;
    }

    // 三种错误 括号不匹配 连续符号 数字错误
    private boolean syntaxElements(List<String> elements) {
        //左括号-右括号
        int leftMinusRight = 0;

        for (int i = 0; i < elements.size() - 1; ++ i) {
            String nowElement = elements.get(i);
            String rightElement = elements.get(i + 1);

            if (nowElement.equals("(")) {
                ++ leftMinusRight;

                if (rightElement.equals(")")) {
                    return false;               // () 空串
                }

            } else if (nowElement.equals(")")) {
                -- leftMinusRight;

                if (leftMinusRight < 0) {
                    return false;               // 右括号大于左括号 ()) ))(
                }
            } else if (isOperator(nowElement)) {
                if (isOperator(rightElement)) {
                    return false;                   // 连续运算符
                }
                if (rightElement.equals(")")) {
                    return false;                   // 符号  )
                }
                if (i > 0) {
                    String leftElement = elements.get(i - 1);
                    if (leftElement.equals("(")) {
                        return false;               // ( 符号
                    }
                }
            } else if (isNumberLike(nowElement) && ! isNumber(nowElement)) {
                return false;                   // 数字格式错误(小数点)
            }
        }

        String lastElement = elements.get(elements.size() - 1);
        if (lastElement.equals("(")) {
            return false;
        } else if (lastElement.equals(")")) {
            if (-- leftMinusRight < 0) {
                return false;
            }
        } else if (isNumberLike(lastElement) && ! isNumber(lastElement)) {
            return false;
        }

        // 检查边界符号
        if (isOperator(elements.get(0)) || isOperator(elements.get(elements.size() - 1))) {
            return false;
        }

        addParenNum = leftMinusRight;
        return true;
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

    private boolean isNumber(String expr) {
        int parts = StringUtils.splitByWholeSeparatorPreserveAllTokens(expr, ".").length;
        if (parts == 1 || (parts == 2 && ! expr.equals("."))) {
            return true;
        }
        return false;
    }



    public String baseConvertion(String expression, String fromBase, String toBase) {
        Map<String, Integer> baseConvertionMap = new HashMap<>(4);
        baseConvertionMap.put("hex", 16);
        baseConvertionMap.put("dec", 10);
        baseConvertionMap.put("oct", 8);
        baseConvertionMap.put("bin", 2);

        try {
            return new BigInteger(expression, baseConvertionMap.get(fromBase.toLowerCase())).toString(baseConvertionMap.get(toBase.toLowerCase())).toUpperCase();
            //return Integer.toString(Integer.parseInt(expression, baseConvertionMap.get(fromBase.toLowerCase())), baseConvertionMap.get(toBase.toLowerCase()));
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Unknown Error";
        }
    }
}
