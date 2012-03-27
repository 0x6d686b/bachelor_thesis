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

public class Coordinate {
    private static final double MIN_LONGITUDE = -180.0;
    private static final double MAX_LONGITUDE = 180.0;
    private static final double MIN_LATITUDE = -90.0;
    private static final double MAX_LATITUDE = 90.0;

    private BigDecimal longitude;
    private BigDecimal latitude;

    public final String toString() {
        return "Longitude: " + longitude + "°, Latitude: " + latitude;
    }

    public final double getLongitudeInDegree() {
        return longitude.doubleValue();
    }

    public final void setLongitudeInDegree(double l) {
//        if (l < MAX_LONGITUDE || l >= MIN_LONGITUDE)
//            throw new IllegalArgumentException("Longitude is out of range.");
        this.longitude = BigDecimal.valueOf(l);
    }

    public final double getLatitudeInDegree() {
        return latitude.doubleValue();
    }

    public final void setLatitudeInDegree(double l) {
//        if (l > MIN_LATITUDE || l < MAX_LATITUDE)
//            throw new IllegalArgumentException("Latitude is out of range.");
        this.latitude = BigDecimal.valueOf(l);
    }

    public final double getLongitudeInRadian() {
        return getLongitudeInDegree()/180*Math.PI;
    }

    public final void setLongitudeInRadian(double l) {
        setLongitudeInDegree(l/Math.PI*180);
    }

    public final double getLatitudeInRadian() {
        return getLatitudeInDegree()/180*Math.PI;
    }

    public final void setLatitudeInRadian(double l) {
        setLatitudeInDegree(l/Math.PI*180);
    }
}
