package ch.zhaw.lakerouting.interpolation.algorithms;

/**
 * Created by IntelliJ IDEA.
 * User: mhk
 * Date: 10.03.12
 * Time: 11:24
 */
public interface InterpolationAlgorithm {
    double interpolate(double x, double y, Double[][] matrix) throws IllegalArgumentException;
}
