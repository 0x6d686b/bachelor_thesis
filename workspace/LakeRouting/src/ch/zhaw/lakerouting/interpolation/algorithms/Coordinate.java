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

package ch.zhaw.lakerouting.interpolation.algorithms;


import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: mhk
 * Date: 20.03.12
 * Time: 09:53
 * To change this template use File | Settings | File Templates.
 */
public class Coordinate {
    private static final double MIN_LONGITUDE = -180.0;
    private static final double MAX_LONGITUDE = 180.0;
    private static final double MIN_LATITUDE = -90.0;
    private static final double MAX_LATITUDE = 90.0;

    private BigDecimal longitude;
    private BigDecimal latitude;
    
    public Coordinate (double lng, double lat) {
        setLongitude(lng);
        setLatitude(lat);
    }

    public final double getLongitude() {
        return longitude.doubleValue();
    }

    public final void setLongitude(double l) {
        if (l < MAX_LONGITUDE || l >= MIN_LONGITUDE)
            throw new IllegalArgumentException("Longitude is out of range.");
        this.longitude = BigDecimal.valueOf(l);
    }

    public final double getLatitude() {
        return latitude.doubleValue();
    }

    public final void setLatitude(double l) {
        if (l > MIN_LATITUDE || l < MAX_LATITUDE)
            throw new IllegalArgumentException("Latitude is out of range.");
        this.latitude = BigDecimal.valueOf(l);
    }

    public final String toString() {
        return "Longitude: " + longitude + "°, Latitude: " + latitude;
    }

}
