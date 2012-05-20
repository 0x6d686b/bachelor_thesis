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

/**
 * Windfield contains a 2D array of WindVectors.
 *
 * This Class eases the handling of WindVectors in an array with a collection of
 * handy methods to help working on a decision net.
 * @author Mathias Hablützel
 * @since 1.0
 * @version 1.0-stable
 */
public class Windfield {
    /**
     * Global lower wind speed.
     *
     * This global lower boundary for the wind speed prevents to block
     * calculations with never terminating paths (when the boat gets stuck in
     * a calm) and also division by zero.
     */
    public static final double LOWER_WINDSPEED_BOUNDARY = 0.001;
    /**
     * Internal wind field size limitation, also prevents a potential DoS attack
     * when an attacker tries to state an unreasonably big field and thus producing
     * immense workload on the CPU.
     */
    private static final int MAX_WINDFIELD_SIZE = 0xff;

    /** The metadata of the wind field */
    private WindfieldMetadata metadata;
    /** List containing all WindVector */
    private List<List<WindVector>> field;

    /**
     * Instance constructor.
     * @return a new Windfield Object
     */
    public static Windfield getInstance() {
        return new Windfield();
    }

    /**
     * Interpolates the WindVector on a given Coordinate.
     *
     * <p>The wind field is a grid of known WindVector but in between there is no data
     * given thus the interpolation gives an approximation of the wind at the given
     * Coordinate using the indicated InterpolationAlgorithm which for most cases can
     * be left to the use of {@link ch.zhaw.lakerouting.interpolation.algorithms.Bilinear}</p>
     *
     * <h4>Note</h4>
     * <p>However, the interpolation will not verify if the Coordinate is actually within
     * the boundaries of the Windfield. If you request an interpolation outside the known
     * wind field you'll simply encounter an ArrayIndexOutOfBoundsException.</p>
     * @param c Coordinate of the requested WindVector
     * @param algorithm Interpolation algorithm used to calculate the WindVector
     * @return The interpolated WindVector at Coordinate c
     * @throws ArrayIndexOutOfBoundsException
     */
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

    /**
     * Interpolates the wind on a list of coordinates.
     *
     * @param coordinates A two-dimensional List of Coordinates.
     * @param algorithm The used InterpolationAlgorithm.
     * @return A two-dimensional List of WindVectors
     * @throws ArrayIndexOutOfBoundsException
     */
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

    /**
     * Imports a two-dimensional List of WindVectors and sets the WindfieldMetadata.
     * @param m a WindfieldMetadata object.
     * @param f a two-dimensional WindVector List.
     * @return Returns itself.
     */
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

    /**
     * Returns the WindfieldMetadata
     * @return WindfieldMetadata
     */
    public WindfieldMetadata getMetadata () {
        return this.metadata;
    }

    /**
     * Returns the WindVector at the position (x,y).
     * @param x the x-th row
     * @param y the y-th column
     * @return the corresponding WindVector
     * @throws ArrayIndexOutOfBoundsException
     */
    public WindVector get(int x, int y) {
        return field.get(x).get(y);
    }

    /**
     * Returns the whole two-dimensional List of WindVectors.
     * @return a two-dimensional List of WindVectors
     */
    public List<List<WindVector>> getField() {
        return field;
    }

    /**
     * Returns the directly neighbouring WindVectors
     * @param coordinate the Coordinate seeking its neighbours
     * @return a two-dimensional Array containing the direct neighbour of the Coordinate.
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
     * Returns the normalized value of the Coordinate c.
     *
     * Basically meaning you get a double value between 0 and
     * 1 where 0 is the preceding grid point and the 1 is the
     * next grid point.
     * @param c The Coordinate of a grid point
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
