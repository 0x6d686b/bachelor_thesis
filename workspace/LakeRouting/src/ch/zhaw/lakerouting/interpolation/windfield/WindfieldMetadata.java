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
import org.joda.time.DateTime;

import java.util.Calendar;

public class WindfieldMetadata {
    private DateTime date;
    private double deltaLng;
    private double deltaLat;
    private Coordinate northWestCorner;
    private Coordinate southEastCorner;
    private int countLngVectors;
    private int countLatVectors;
    
    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime input) {
        this.date = input;
    }

    public double getDeltaLng() {
        return deltaLng;
    }

    public void setDeltaLng(double deltaLat) {
        this.deltaLng = deltaLat;
    }

    public double getDeltaLat() {
        return deltaLat;
    }

    public void setDeltaLat(double input) {
        this.deltaLat = input;
    }

    public Coordinate getNorthWestCorner() {
        return northWestCorner;
    }

    public void setNorthWestCorner(Coordinate input) {
        this.northWestCorner = input;
    }

    public Coordinate getSouthEastCorner() {
        return southEastCorner;
    }

    public void setSouthEastCorner(Coordinate countLngVectors) {
        this.southEastCorner = countLngVectors;
    }

    public int getCountLngVectors() {
        return countLngVectors;
    }

    public void setCountLngVectors(int input) {
        this.countLngVectors = input;
    }

    public int getCountLatVectors() {
        return countLatVectors;
    }

    public void setCountLatVectors(int input) {
        this.countLatVectors = input;
    }


}
