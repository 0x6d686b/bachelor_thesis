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
import ch.zhaw.lakerouting.datatypes.Node;
import ch.zhaw.lakerouting.datatypes.WindVector;
import ch.zhaw.lakerouting.interpolation.algorithms.InterpolationAlgorithm;

import java.util.ArrayList;
import java.util.List;

public class Windfield {
    private static final double LOWER_WINDSPEED_BOUNDARY = 0.001;
    private static final int MAX_WINDFIELD_SIZE = 0xff;
    
    private WindfieldMetadata metadata;
    private List<List<WindVector>> field;

    public static Windfield getInstance() {
        return new Windfield();
    }

    public WindVector interpolate(Coordinate c, InterpolationAlgorithm algorithm) {
        WindVector vector = new WindVector(0,0);
        WindVector[][] r = this.getRange(c);
        Double[][] uVector = {{r[0][0].getU() , r[1][0].getU()},
                              {r[0][1].getU() , r[1][1].getU()}};
        Double[][] vVector = {{r[0][0].getV() , r[1][0].getV()},
                              {r[0][1].getV() , r[1][1].getV()}};
        Double[] val = this.getNormalizedCoordinate(c);
        vector.setU( algorithm.interpolate(val[0], val[1], uVector) );
        vector.setV( algorithm.interpolate(val[0], val[1], vVector) );
        return vector;
    }

    public List<List<WindVector>> interpolateOnDecisionNet(List<List<Node>> coordinates, InterpolationAlgorithm algorithm) {
    	List<List<WindVector>> vectorField = new ArrayList<List<WindVector>>();
    	List<WindVector> vectorRow;
        for (int i = 0; i < coordinates.size(); i++) {
        	vectorRow = new ArrayList<WindVector>();
            for (int j = 0; j < coordinates.get(0).size(); j++) {
                vectorRow.add(interpolate(coordinates.get(i).get(j).getCrd(), algorithm));
            }
            vectorField.add(vectorRow);
        }
        return vectorField;
    }

    public final Windfield setField (WindfieldMetadata m, List<List<WindVector>> f) {
    	List<List<WindVector>> vectorField = new ArrayList<List<WindVector>>();
    	List<WindVector> vectorRow = new ArrayList<WindVector>();
        for (int i = 0; i < f.size(); i++) {
            for (int j = 0; j < f.get(0).size(); j++) {
                vectorRow.add(f.get(i).get(j));
            }
            vectorField.add(vectorRow);
            vectorRow = new ArrayList<WindVector>();
        }
        this.field = vectorField;
        this.metadata = m;
        return this;
    }

    public WindfieldMetadata getMetadata () {
        return this.metadata;
    }

    public WindVector get(int x, int y) {
        return field.get(x).get(y);
    }

    public List<List<WindVector>> getField() {
        return field;
    }


    /**
     * Returns the directly neighbouring WindVectors
     * @param coordinate
     * @return
     */
    private WindVector[][] getRange(Coordinate coordinate) {
        /**
         * Don't ask about this voodoo ...
         */
        int lnglow = new Double(Math.floor((coordinate.getLongitudeInDegree() -
                metadata.getNorthWestCorner().getLongitudeInDegree())
                / metadata.getDeltaLng())).intValue();
        int lnghigh = new Double(Math.ceil((coordinate.getLongitudeInDegree() -
                metadata.getNorthWestCorner().getLongitudeInDegree())
                / metadata.getDeltaLng())).intValue();
        int latlow = new Double(Math.floor((coordinate.getLatitudeInDegree() -
                metadata.getNorthWestCorner().getLatitudeInDegree())
                / metadata.getDeltaLat())).intValue();
        int lathigh = new Double(Math.ceil((coordinate.getLatitudeInDegree() -
                metadata.getNorthWestCorner().getLatitudeInDegree())
               / metadata.getDeltaLat())).intValue();
        return new WindVector[][] {{field.get(latlow).get(lnglow),field.get(latlow).get(lnghigh)},
                                   {field.get(lathigh).get(lnglow),field.get(lathigh).get(lnghigh)}};
    }

    /**
     * Returns the normalized value of the Coordinate c,
     * basically meaning you get a double value between 0 and
     * 1 where 0 is the preceding grid point and the 1 is the
     * next grid point.
     * @param c
     * @return A Double[] containing the longitude ratio and the latitude ratio
     */
    private Double[] getNormalizedCoordinate(Coordinate c) {
        /**
         * First Latitude then Longitude... BECAUSE IT'S SO!!
         */
        double lng = ((c.getLongitudeInDegree() - metadata.getNorthWestCorner().getLongitudeInDegree()) /
                metadata.getDeltaLng()) % 1;
        double lat = ((c.getLatitudeInDegree() - metadata.getNorthWestCorner().getLatitudeInDegree()) /
                metadata.getDeltaLat()) % 1;
        return new Double[] {lat,lng};
    }
}
