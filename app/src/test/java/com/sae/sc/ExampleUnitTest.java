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
        String expression = "";
        System.out.println(Resolver.resolveFromString(expression));
    }
}