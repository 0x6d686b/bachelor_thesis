package ch.zhaw.lakerouting.interpolation.algorithms;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: mhk
 * Date: 11.03.12
 * Time: 17:53
 */
public class BilinearTest {
    @Test
    public void testInterpolate() throws Exception {
        Bilinear bilinear_object = new Bilinear();
        double[][] arr = {{1,3},{2,4}};
        double[][] arr2 = {{21,171},{4,68}};
        assertEquals(2.1, bilinear_object.interpolate(0.6, 0.25, arr), 0.001);
        assertEquals(103.527, bilinear_object.interpolate(0.41, 0.78, arr2), 0.001);
    }
}
