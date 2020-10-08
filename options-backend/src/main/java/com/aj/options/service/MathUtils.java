package com.aj.options.service;

import java.math.BigDecimal;

public class MathUtils {
    public static int compareDouble(double d1, double d2) {
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);

        return b1.compareTo(b2);
    }
}
