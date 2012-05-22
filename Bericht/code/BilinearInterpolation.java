public class Bilinear implements InterpolationAlgorithm {
    /**
     * Interpolates at the position (x,y) on the matrix.
     * @param x Must be between (0,1)
     * @param y Must be between (0,1)
     * @param matrix Is a 2D array of size 2x2 with the corner value needed to interpolate
     * @return returns a double contain the interpolated value.
     */
    @Override @Test
    public final double interpolate(double x, double y, Double[][] matrix) {
        if (x > 1 || x < 0 || y > 1 || y < 0)
            throw new IllegalArgumentException("Params not in required range, got x: " + x + " and y: " + y);

        if (!(matrix.length == 2 && matrix[0].length == 2 && matrix[1].length == 2))
            throw new IllegalArgumentException("Passed matrix array has not the required size.");

        return x * (matrix[1][0] - matrix[0][0])
                + x * y * (matrix[0][0] - matrix[0][1] + matrix[1][1] - matrix[1][0])
                + y * (matrix[0][1] - matrix[0][0]) + matrix[0][0];
    }
}
