package ch.zhaw.lakerouting.interpolation.algorithms;

import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: mhk
 * Date: 10.03.12
 * Time: 11:21
 */
public class Bilinear implements InterpolationAlgorithm {
    @Override @Test
    public double interpolate(double x, double y, double[][] matrix) {
        if (!(x >= 0 && x <= 1 && y >= 0 && y <= 1))
            throw new IllegalArgumentException("Params not in required range, got x: " + x + " and y: " + y);

        if (!(matrix.length == 2 && matrix[0].length == 2 && matrix[1].length == 2))
            throw new IllegalArgumentException("Passed matrix array has not the required size.");

        return x * (matrix[1][0] - matrix[0][0])
                + x * y * (matrix[0][0] - matrix[0][1] + matrix[1][1] - matrix[1][0])
                + y * (matrix[0][1] - matrix[0][0]) + matrix[0][0];
    }
}
