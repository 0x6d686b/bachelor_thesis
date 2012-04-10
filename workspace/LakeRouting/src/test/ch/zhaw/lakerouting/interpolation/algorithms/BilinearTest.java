/*
 * Copyright (c) 2012, M. Hablützel, F. Yükseldi, J. Ambühl
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the ZHAW, MeteoSchweiz nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL M. Hablützel, F. Yükseldi, J. Ambühl BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package test.ch.zhaw.lakerouting.interpolation.algorithms;

import ch.zhaw.lakerouting.interpolation.algorithms.Bilinear;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests the {@link Bilinear} if it works correct
 * @author Mathias Hablützel
 * @version 1.0
 * @since 1.0
 *
 */
public class BilinearTest {
    @Test
    public final void testInterpolate() throws Exception {
        Bilinear bilinearObject = new Bilinear();
        Double[][] arr = new Double[][] {{1d, 3d}, {2d, 4d}};
        Double[][] arr2 = new Double[][] {{21d,171d},{4d,68d}};
        Double[][] arr3 = new Double[][] {{2.8,3.95},{2.84,4.2}};
        assertEquals(2.1, bilinearObject.interpolate(0.6, 0.25, arr), 0.001);
        assertEquals(103.527, bilinearObject.interpolate(0.41, 0.78, arr2), 0.001);
        assertEquals(2.87871, bilinearObject.interpolate(0.21,0.046, arr3), 0.1);

        // Epsilon testing
        assertEquals(0, bilinearObject.interpolate(0.00001,0.00001,arr) - arr[0][0], 0.0001);
        assertEquals(0, bilinearObject.interpolate(0.99999,0.00001,arr) - arr[1][0], 0.0001);
        assertEquals(0, bilinearObject.interpolate(0.00001,0.99999,arr) - arr[0][1], 0.0001);
        assertEquals(0, bilinearObject.interpolate(0.99999,0.99999,arr) - arr[1][1], 0.0001);

    }
}
