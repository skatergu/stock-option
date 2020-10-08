package com.aj.options;

import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;

import java.util.ArrayList;
import java.util.Arrays;

public class TestFitter {
    public static void main(String[] args) {
        double tolerance = 1e-15;
        double x[] = {0.0, 0.1, 0.2, 0.4, 0.5, 0.6};
        double y[] = {1.0, 1.2, 1.5, 2.2, 2.8, 3.2};

        ArrayList<WeightedObservedPoint> obs = getWeightedObservedPoints(x, y);
        testFitterValue(obs, 0.31);
        double[] y1 = getFitterValues(obs, 1);
    }

    private static ArrayList<WeightedObservedPoint> getWeightedObservedPoints(double[] x, double[] y) {
        ArrayList<WeightedObservedPoint> pts = new ArrayList<>(x.length);
        for (int i = 0; i < x.length; i++) {
            pts.add(new WeightedObservedPoint(1, x[i], y[i]));
        }

        return pts;
    }

    /*
     * test fitters of different degrees
     */
    public static void testFitterValue(ArrayList<WeightedObservedPoint> obs, double x1) {
        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(1);
        double[] coeffs = fitter.fit(obs);
        p("Liner: coeffs = " + Arrays.toString(coeffs));
        p("       x=" + x1 + " => y=" + calcCurveValue(coeffs, x1));

        fitter = PolynomialCurveFitter.create(2);
         coeffs = fitter.fit(obs);
        p("Parobola: coeffs = " + Arrays.toString(coeffs));
        p("          x=" + x1 + " => y=" + calcCurveValue(coeffs, x1));
    }

    /**
     *    Get the fitter curve values
     * @param obs     input data points
     * @param degree  fitter curve degree
     * @return        fitter curve values for input x[]
     */
    public static double[] getFitterValues(ArrayList<WeightedObservedPoint> obs, int degree) {
        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(degree);
        double[] coeffs = fitter.fit(obs);

        double[] result = new double[obs.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = calcCurveValue(coeffs, obs.get(i).getX());
        }

        return result;
    }

    /*
     * calculate curve value based on the fitter equation denoted by the coefficients in coeffs array
     */
    private static double calcCurveValue(double[] coeffs, double x) {
        double result = 0.0;
        for (int i = coeffs.length - 1; i >= 0; --i) {
            result = result * x + coeffs[i];
        }
        return result;
    }
    static void p(String s) {
        System.out.println(s);
    }
}
