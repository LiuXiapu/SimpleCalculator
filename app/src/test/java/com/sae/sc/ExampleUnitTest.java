package com.sae.sc;

import com.sae.sc.utils.Resolver;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void testResolver() {
        String expression = "3+5+6";
    }

    @Test
    public void testTokenizeString() {
        String expression = "a++a";

        String[] tokensWithSpace = StringUtils.splitByWholeSeparatorPreserveAllTokens(expression, "+");
        System.out.println(tokensWithSpace.length);

        for (int i = 0; i < tokensWithSpace.length; ++ i) {
            //if (tokensWithSpace[i].equals("")) {
            //    tokensWithSpace[i] = "+";
            //}
            System.out.println(tokensWithSpace[i]);
        }
    }

    @Test
    public void lxpTest() {
        String expression = "sinsina++(ersi)*5sin";
        String token = "E";

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

        for (String sliceAndToken : slicesWithTokens) {
            System.out.println(sliceAndToken);
        }
    }

    @Test
    public void testAddZeroAndMulOp() {
        List<String> elements = new LinkedList<>(Arrays.asList(new String[]{"-", "sin", "(", "+", "E", "2", "E", ")"}));

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

        for (int i = 1; i < elements.size() - 1; ++ i) {

        }

        int size = elements.size();
        if (size >= 2) {
            String lastElement = elements.get(elements.size() - 1);
            if (lastElement == "E") {
                String prevElement = elements.get(elements.size() - 2);
                //如果不是左括号 不是运算符 就添加乘号
            }
        }

        printList(elements);
    }

    @Test
    public void testIsNumber() {
        String numberLike = ".";
        String[] result = StringUtils.splitByWholeSeparatorPreserveAllTokens(numberLike, ".");
        int resultLength = result.length;

        System.out.println(resultLength);
    }

    private void printList(List<? extends Object> objects) {
        for (Object object: objects) {
            System.out.print(object.toString() + "  ");
        }
        System.out.println();
    }

    @Test
    public void testSubList() {
        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        printList(list.subList(1, 2));

    }

    @Test
    public void testFunction() {
        System.out.println(Math.log(Math.E));
    }

    @Test
    public void testBaseConvertion() {
        Map<String, Integer> baseConvertionMap = new HashMap<>(4);
        baseConvertionMap.put("hex", 16);
        baseConvertionMap.put("dec", 10);
        baseConvertionMap.put("oct", 8);
        baseConvertionMap.put("bin", 2);

        String expression = "123";
        String fromBase = "hex";
        String toBase = "dec";


    }
}