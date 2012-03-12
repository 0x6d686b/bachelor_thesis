package ch.zhaw.lakerouting.interpolation.algorithms;

/**
 * Created by IntelliJ IDEA.
 * User: mhk
 * Date: 12.03.12
 * Time: 18:31
 */
public class Interpolator {
    public double interpolate (double x, double y, Windfield field, InterpolationAlgorithm algorithm) {
        return algorithm.interpolate(x, y, field.getRange(x,y));
    }
}
