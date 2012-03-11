package ch.zhaw.lakerouting.interpolation.algorithms;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: mhk
 * Date: 10.03.12
 * Time: 11:24
 */
public interface InterpolationAlgorithm {
    public double interpolate(double x, double y, double[][] matrix) throws IllegalArgumentException;
}
