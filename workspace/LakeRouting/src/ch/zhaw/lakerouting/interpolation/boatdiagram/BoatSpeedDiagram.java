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

package ch.zhaw.lakerouting.interpolation.boatdiagram;

import ch.zhaw.lakerouting.interpolation.algorithms.InterpolationAlgorithm;
import ch.zhaw.lakerouting.interpolation.boatdiagram.loader.BoatFieldLoader;

import org.apache.log4j.Logger;
import org.junit.Test;

import java.net.URI;

/**
 * Is an abstract data type for the speed the boats reaches under different wind angle of attack.
 *
 * @author Mathias Hablützel
 * @version 1.0-stable
 * @since 1.0
 */
public class BoatSpeedDiagram {
    public static final double HARD_LOWER_BOAT_LIMIT = 0.01;
    private Double[][] field;
    private BoatSpeedDiagramMetadata metadata;

    private Logger logger = Logger.getLogger(this.getClass());
    
    /**
     * Loads the data from {@link BoatFieldLoader}
     *
     * @param fieldplane Object of type {@link BoatFieldLoader}
     * @param uri        {@link URI} of the file to load
     * @return Returns {@code true} if successfull, {@code false} if not
     */
    @Test
    public final <T> boolean loadDiagram(T fieldplane, URI uri) {
        if (!(fieldplane instanceof BoatFieldLoader)) {
        	logger.error("You need to pass me BoatFieldLoader!");
            throw new IllegalArgumentException("You need to pass me BoatFieldLoader!");
        }
        if (!((BoatFieldLoader) fieldplane).loadResource(uri))
            return false;
        return this.loadArray((BoatFieldLoader) fieldplane);
    }

    @Test
    public final double interpolate(double windAttackAngle, double windSpeed, InterpolationAlgorithm algorithm) {
        Double[] val = this.getNormalizedValues(windAttackAngle, windSpeed);
        double value = algorithm.interpolate(val[0], val[1], this.getRange(windAttackAngle, windSpeed));
        return this.limiter(windAttackAngle, windSpeed, value);
    }

    private boolean loadArray(BoatFieldLoader fieldplane) {
        this.field = fieldplane.convertToArray().clone();
        this.metadata = fieldplane.getMetadata();
        if (this.field != null)
            return true;
        return false;
    }

    /**
     * This method prevents to get values out of range when interpolating.
     * Especially it will prevent our boat to get stuck and the calculation
     * to never finish.
     *
     * @param windAttackAngle
     * @param windSpeed
     * @param value
     * @return
     */
    private double limiter(double windAttackAngle, double windSpeed, double value) {
        // prevent our boat to get stuck
        if (windAttackAngle < this.metadata.getMinimalAttackAngle() || windSpeed < this.metadata.getMinimalWindspeed())
            return HARD_LOWER_BOAT_LIMIT;
        if (windSpeed > this.metadata.getMaximalWindspeed())
            return this.metadata.getMaximalWindspeed();
        if (windAttackAngle > this.metadata.getMaximalAttackAngle())
            return HARD_LOWER_BOAT_LIMIT;
        return value;
    }

    /**
     * Gets the next cornering values known. Basically this looks for the next lower and higher known value.
     *
     * @param x
     * @param y
     * @return Returns a 2D array with size 2x2 containing the next known lower and higher values containing x,y.
     */
    @Test
    private final Double[][] getRange(double x, double y) {
        int fromX = 1;
        int fromY = 1;
        int toX = 2;
        int toY = 2;
        for (int i = 0; i < field[0].length; i++) {
            if (x <= field[0][i]) {
                fromX = i - 1;
                toX = i;
                break;
            }
        }
        for (int i = 0; i < field.length; i++) {
            if (y <= field[i][0]) {
                fromY = i - 1;
                toY = i;
                break;
            }
        }
        return new Double[][]{{field[fromY][fromX], field[toY][fromX]}, {field[fromY][toX], field[toY][toX]}};
    }

    /**
     * Calculate the normalized values.
     * <p/>
     * Basically we do nothing else than calculate the percentage from the lower to higher bound of the field.
     * We then know how far away from the lower boundary of the field we are.
     *
     * @param a
     * @param b
     * @return
     */
    @Test
    private final Double[] getNormalizedValues(double a, double b) {
        int x = 1;
        int y = 1;
        for (int i = 0; i < field[0].length; i++) {
            if (a <= field[0][i]) {
                x = i - 1;
                break;
            }
        }
        for (int i = 0; i < field.length; i++) {
            if (b <= field[i][0]) {
                y = i - 1;
                break;
            }
        }
        double deltaX = field[0][x + 1] - field[0][x];
        double deltaY = field[y + 1][0] - field[y][0];

        return new Double[]{((a - field[0][x]) / deltaX), ((b - field[y][0]) / deltaY)};

    }
}
