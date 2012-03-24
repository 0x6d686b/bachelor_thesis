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

package ch.zhaw.lakerouting.interpolation.windfield;

import ch.zhaw.lakerouting.datatypes.Coordinate;
import ch.zhaw.lakerouting.datatypes.WindVector;
import ch.zhaw.lakerouting.interpolation.Field;
import ch.zhaw.lakerouting.interpolation.windfield.loader.WindFieldLoader;
import org.junit.Test;

import java.net.URI;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: mhk
 * Date: 20.03.12
 * Time: 11:26
 * To change this template use File | Settings | File Templates.
 */
public class Windfield implements Field {
    private static final double LOWER_WINDSPEED_BOUNDARY = 0.001;
    private static final int MAX_WINDFIELD_SIZE = 0xff;
    
    private Calendar date;
    private double deltaLng;
    private double deltaLat;
    private Coordinate northWestCorner;
    private Coordinate southEastCorner;
    private int countLngVectors;
    private int countLatVectors;
    private WindVector[][] field;

    @Test
    private boolean loadArray (WindFieldLoader fieldplane) {
        this.field = fieldplane.convertToArray().clone();
        if (this.field != null)
            return true;
        return false;
    }
    @Override
    public Double[][] getRange(double x, double y) {
        // TODO: Double[][] getRange(double x, double y)
        return new Double[0][];
    }

    @Override
    public Double[] getNormalizedValues(double a, double b) {
        // TODO: Double[] getNormalizedValues(double a, double b)
        return new Double[0];
    }

    @Override
    public final <T> boolean loadDiagram(T fieldplane, URI uri) {
        if (fieldplane.getClass() != WindFieldLoader.class) {
            throw new IllegalArgumentException("You need to pass me BoatFieldLoader!");
        }
        if (!((WindFieldLoader)fieldplane).loadRessource(uri))
            return false;
        return this.loadArray((WindFieldLoader) fieldplane);
    }
}
