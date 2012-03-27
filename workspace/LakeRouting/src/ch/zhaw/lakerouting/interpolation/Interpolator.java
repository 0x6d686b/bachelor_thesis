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

package ch.zhaw.lakerouting.interpolation;

import ch.zhaw.lakerouting.datatypes.Coordinate;
import ch.zhaw.lakerouting.datatypes.WindVector;
import ch.zhaw.lakerouting.interpolation.algorithms.InterpolationAlgorithm;
import org.junit.Test;

/**
 * The main class for the interpolation process, handles all calls
 *
 * When given the windfield and the interpolation algorithm it'll
 * calculate the interpolated value at {@code x,y}
 * @author Mathias Hablützel
 * @since 1.0
 * @version 1.0
 */
public class Interpolator {
    /**
     *
     * @param windAttackAngle in Degree, 0 is from front, 180 is from behind
     * @param windSpeed in knots
     * @param field {@link ch.zhaw.lakerouting.interpolation.boatdiagram.BoatSpeedDiagram} Field containing the boatdiagram
     * @param algorithm
     * @return
     */
    @Test
    public final double interpolate (double windAttackAngle, double windSpeed, Field field, InterpolationAlgorithm algorithm) {
        Double[] val = field.getNormalizedValues(windAttackAngle,windSpeed);
        return algorithm.interpolate(val[0], val[1], field.getRange(windAttackAngle,windSpeed));
    }
    
    @Test
    public final WindVector interpolate (Coordinate c, Field field, InterpolationAlgorithm algorithm) {
        WindVector vector = new WindVector(0,0);
        WindVector[][] r = field.getRange(c);
        Double[][] uVector = {{r[0][0].getU() , r[1][0].getU()},
                              {r[0][1].getU() , r[1][1].getU()}};
        Double[][] vVector = {{r[0][0].getV() , r[1][0].getV()},
                              {r[0][1].getV() , r[1][1].getV()}};
        Double[] val = field.getNormalizedCoordinate(c);
        vector.setU( algorithm.interpolate(val[0], val[1], uVector) );
        vector.setV( algorithm.interpolate(val[0], val[1], vVector) );

        return vector;
    }
}
