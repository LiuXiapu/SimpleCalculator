package com.sae.sc;

import com.sae.sc.utils.Resolver;

import org.junit.Test;

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
        System.out.println(Resolver.resolveFromString(expression));
    }

    @Test
    public void testTokenizeString() {
        String expression = "1a2bc";
        for (String a: expression.split("a|b")) {
            System.out.println(a);
        }
    }
}