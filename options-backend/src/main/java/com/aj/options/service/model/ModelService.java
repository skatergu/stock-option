package com.aj.options.service.model;

import com.aj.options.entity.Option;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.AkimaSplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.NevilleInterpolator;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Service
public class ModelService {
    public void calcModelData(List<Option> options, ModelType model) {
        switch (model) {
            case SplineInterpolator -> getInterpolationModelValue(options, () -> new SplineInterpolator());
            case AkimaSplineInterpolator -> getInterpolationModelValue(options, () -> new AkimaSplineInterpolator());
            case FitOneD -> fitData(options, 1);
            case FitTwoD -> fitData(options, 2);
            default -> System.err.println("Model " + model + " is not supported");
        }
    }

    private void getInterpolationModelValue(List<Option> options, Supplier<UnivariateInterpolator> supplier) {
        if (options.size() < 3)
            throw new RuntimeException("Options don't have enough data");

        double[] strikes = options.stream().mapToDouble(Option::getStrike).toArray();
        double[] px = options.stream().mapToDouble(Option::getPrice).toArray();
        double[] modelValues = calcModelValue(strikes, px, supplier);

        for (int i=0; i<modelValues.length; i++) {
            Option option = options.get(i);
            option.setModelValue(modelValues[i]);
        }
    }

    private void fitData(List<Option> options, int dimension) {
        double[] strikes = options.stream().mapToDouble(Option::getStrike).toArray();
        double[] px = options.stream().mapToDouble(Option::getPrice).toArray();

        ArrayList<WeightedObservedPoint> obs = getWeightedObservedPoints(strikes, px);
        double[] modelValues = getFitterValues(obs, dimension);

        for (int i=0; i<modelValues.length; i++) {
            Option option = options.get(i);
            option.setModelValue(modelValues[i]);
        }
    }

    /**
     *    Get the fitter curve values
     * @param obs     input data points
     * @param degree  fitter curve degree
     * @return        fitter curve values for input x[]
     */
    public double[] getFitterValues(ArrayList<WeightedObservedPoint> obs, int degree) {
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
    private double calcCurveValue(double[] coeffs, double x) {
        double result = 0.0;
        for (int i = coeffs.length - 1; i >= 0; --i) {
            result = result * x + coeffs[i];
        }
        return result;
    }

    private ArrayList<WeightedObservedPoint> getWeightedObservedPoints(double[] x, double[] y) {
        ArrayList<WeightedObservedPoint> pts = new ArrayList<>(x.length);
        for (int i = 0; i < x.length; i++) {
            pts.add(new WeightedObservedPoint(1, x[i], y[i]));
        }

        return pts;
    }


    private double[] calcModelValue(double[] x, double[] y, Supplier<UnivariateInterpolator> supplier) {
        // moduleValues[] saves all the module values. The first and last values are copied from y[].
        double[] moduleValues = new double[y.length];
        moduleValues[0] = y[0];
        moduleValues[y.length - 1] = y[y.length - 1];

        // array x1 keeps all the strikes except the one that we need to calculate the module value
        double[] x1 = new double[x.length -1];
        // array y1 keeps all the option prices from array y except at the strike price that we need to calculate the module value
        double[] y1 = new double[y.length -1];

        for (int i = 1; i < moduleValues.length - 1; i++) {
            try {
                // copy all x[] values to x1[] except the x[i] which we want to calculate module value
                x1[i - 1] = x[i - 1];
                System.arraycopy(x, i + 1, x1, i, x1.length - i);

                // copy all y[] values to y1[] except the y[i] which we want to calculate module value
                y1[i - 1] = y[i - 1];
                System.arraycopy(y, i + 1, y1, i, y1.length - i);

                UnivariateFunction func = supplier.get().interpolate(x1, y1);
                moduleValues[i] = func.value(x[i]);
            } catch (Exception e) {
                System.err.println(e);
            }
        }

        return moduleValues;
    }
}
