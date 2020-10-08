package com.aj.options;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.AkimaSplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.NevilleInterpolator;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Supplier;

public class InterpolationTest {
    public static void main(String[] args) {
        double tolerance = 1e-15;
        double x[] = {0.0, 0.1, 0.2, 0.4, 0.5, 0.6};
        double y[] = {1.0, 1.2, 1.5, 2.2, 2.8, 3.2};

        testValueWithFun(x, y, 0.31, () -> new SplineInterpolator());
        testValueWithFun(x, y, 0.31, () -> new AkimaSplineInterpolator());
        testValueWithFun(x, y, 0.31, () -> new NevilleInterpolator());  // PolynomialFunctionLagrangeForm

        double[]  m1 = getInterpolationModuleValueArray(x, y, () -> new SplineInterpolator());
        p("SplineInterpolator module values:        " + Arrays.toString(m1));
        double[]  m2 = getInterpolationModuleValueArray(x, y, () -> new AkimaSplineInterpolator());
        p("AkimaSplineInterpolator module values:   " + Arrays.toString(m2));
        double[]  m3 = getInterpolationModuleValueArray(x, y, () -> new NevilleInterpolator());
        p("NevilleInterpolatormodule values:        " + Arrays.toString(m3));
    }

    public static void testValueWithFun(double[] x, double[] y, double x1, Supplier<UnivariateInterpolator> supplier) {
        UnivariateFunction     func = supplier.get().interpolate(x, y);

        double val = func.value(x1);
        p(func.getClass()   + " interploate at " + x1 + " = " + val);
    }

    public static double[] getInterpolationModuleValueArray(double[] x, double[] y,
                                                            Supplier<UnivariateInterpolator> supplier) {
        if (x.length != y.length || y.length < 3) {
            throw new RuntimeException("x[] and y[] must have the same size and greater than 2");
        }

        // moduleValues[] saves all the module values. The first and last values are copied from y[].
        double[] moduleValues = new double[y.length];
        moduleValues[0] = y[0];
        moduleValues[y.length - 1] = y[y.length - 1];

        // array x1 keeps all the strikes except the one that we need to calculate the module value
        double[] x1 = new double[x.length -1];
        // array y1 keeps all the option prices from array y except at the strike price that we need to calculate the module value
        double[] y1 = new double[y.length -1];

        for (int i = 1; i < moduleValues.length - 1; i++) {
            // copy all x[] values to x1[] except the x[i] which we want to calculate module value
            x1[i - 1] = x[i - 1];
            System.arraycopy(x, i + 1, x1, i, x1.length - i);

            // copy all y[] values to y1[] except the y[i] which we want to calculate module value
            y1[i - 1] = y[i - 1];
            System.arraycopy(y, i + 1, y1, i, y1.length - i);

            UnivariateFunction  func = supplier.get().interpolate(x1, y1);
            moduleValues[i] = func.value(x[i]);
        }

        return moduleValues;
    }

    static void p(String s) {
        System.out.println(s);
    }
}
