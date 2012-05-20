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

package ch.zhaw.lakerouting.datatypes;

import java.math.BigDecimal;

/**
 * Data type for storing a coordinate and retrieving it
 * in different units.
 *
 * <p>All formats will be in longitude and latitude, refer to the
 * GPS format.</p>
 *
 * <p>For convenience and for certain computation we provide
 * the unit in degree and radian. Watch out not to mix them
 * unless you are <b>really sure</b> what you are doing!</p>
 *
 * @author Mathias Hablützel
 * @version 1.0-stable
 * @since 1.0
 */

public class Coordinate {

    /**
     * The purpose of restricting the coordinate range is
     * to prevent that an out of range value can created
     * in first place.
     */

    /** Restricting range of longitude */
    private static final double MIN_LONGITUDE = -180.0;
    /** Restricting range of longitude */
    private static final double MAX_LONGITUDE = 180.0;
    /** Restricting range of latitude */
    private static final double MIN_LATITUDE = -90.0;
    /** Restricting range of latitude */
    private static final double MAX_LATITUDE = 90.0;

    /**
     * We use BigDecimal here to provide a maximum of accuracy
     * this implicates that we transform this into double values
     * later.
     */

    private BigDecimal longitude;

    /**
     * @see #longitude
     */
    private BigDecimal latitude;

    /**
     * Returns the coordinate in a human readable way.
     * @return a String containing the coordinates
     */
    public final String toString() {
        return "Longitude: " + longitude + "°, Latitude: " + latitude;
    }

    /**
     * Returns the longitude in degree
     * @return longitude
     * @see #getLongitudeInRadian()
     */
    public final double getLongitudeInDegree() {
        return longitude.doubleValue();
    }

    /**
     * Sets the longitude in degree
     * @param l longitude
     * @see #setLongitudeInRadian(double) for setting the longitude in radian
     * @throws IllegalArgumentException
     */
    public final void setLongitudeInDegree(double l) {
        if (l > MAX_LONGITUDE || l <= MIN_LONGITUDE)
            throw new IllegalArgumentException("Longitude is out of range.");
        this.longitude = BigDecimal.valueOf(l);
    }

    /**
     * Returns the latitude in degree
     * @return latitude
     * @see #getLatitudeInRadian()
     */
    public final double getLatitudeInDegree() {
        return latitude.doubleValue();
    }

    /**
     * Sets the latitude in degree
     * @param l latitude
     * @see #setLatitudeInRadian(double)
     * @throws IllegalArgumentException
     */
    public final void setLatitudeInDegree(double l) {
        if (l > MAX_LATITUDE || l < MIN_LATITUDE)
            throw new IllegalArgumentException("Latitude is out of range.");
        this.latitude = BigDecimal.valueOf(l);
    }

    /**
     * Returns the longitude in radian
     * @return longitude
     * @see #getLongitudeInDegree()
     */
    public final double getLongitudeInRadian() {
        return getLongitudeInDegree()/180*Math.PI;
    }

    /**
     * Sets the longitude in radian
     * @param l longitude
     * @see #getLongitudeInDegree()
     * @throws IllegalArgumentException
     */
    public final void setLongitudeInRadian(double l) {
        setLongitudeInDegree(l/Math.PI*180);
    }

    /**
     * Returns the latitude in radian
     * @return latitude
     * @see #getLatitudeInDegree()
     */
    public final double getLatitudeInRadian() {
        return getLatitudeInDegree()/180*Math.PI;
    }

    /**
     * Sets the latitude in radian
     * @param l latitude
     * @see #setLatitudeInDegree(double)
     * @throws IllegalArgumentException
     */
    public final void setLatitudeInRadian(double l) {
        setLatitudeInDegree(l/Math.PI*180);
    }
}
