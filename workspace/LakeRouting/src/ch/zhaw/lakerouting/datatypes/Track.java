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

/**
 * Track is a simple data type calculating the vectors of two Coordinates.
 *
 * <p>Track calculates a vector from Coordinate A -> B and splits it up in two
 * components parallel to the longitudinal respectively latitudinal circle. In
 * mathematical terms: <pre>A + latitudinal_vector + longitudinal_vector = B</pre></p>
 *
 * <p>However note that since the Coordinates do not contain any radial information
 * of a sphere but is limited to angular calculations, Track will return values
 * according to the unit sphere meaning that <pre>x,y,z = 1</pre> in the cartesian
 * representation.</p>
 *
 * <p>The vector is actually a line penetrating a unit sphere on exactly two points
 * which are described by two Coordinates (passed in the constructor).</p>
 */
public class Track {
    /** longitudinal vector */
	private double trackLong;
    /** latitudinal vector */
	private double trackLat;

    /**
     * Constructor for the vector between two Coordinates
     * @param crd1 origin Coordinate
     * @param crd2 destination Coordinate
     */
	public Track(Coordinate crd1, Coordinate crd2) {
		setTrackLong(crd1, crd2);
		setTrackLat(crd1, crd2);
	}

    /**
     * Returns the longitudinal part of the vector
     * @return longitudinal vector length
     */
	public double getTrackLong() {
		return trackLong;
	}

    /**
     * Calculates the longitudinal vector
     * @param crd1 origin Coordinate
     * @param crd2 destination Coordinate
     */
	private void setTrackLong(Coordinate crd1, Coordinate crd2) {
		this.trackLong = -Math.cos(crd2.getLatitudeInRadian())
				* Math.sin(crd1.getLongitudeInRadian() - crd2.getLongitudeInRadian());
	}

    /**
     * Returns the latitudinal part of the vector
     * @return latitudinal vector length
     */
	public double getTrackLat() {
		return trackLat;
	}

    /**
     * Calculates the latitudinal vector
     * @param crd1 origin Coordinate
     * @param crd2 destination Coordinate
     */
	private void setTrackLat(Coordinate crd1, Coordinate crd2) {
		this.trackLat = (Math.cos(crd1.getLatitudeInRadian())* Math.sin(crd2.getLatitudeInRadian()))
				- (Math.cos(crd1.getLongitudeInRadian() - crd2.getLongitudeInRadian()) * Math.cos(crd2.getLatitudeInRadian()) * Math.sin(crd1.getLatitudeInRadian()));
	}

    /**
     * Calculates the vector length
     * @return vector length
     */
	public double getTrackLength(){
		return Math.sqrt(Math.pow(getTrackLong(),2) + Math.pow(getTrackLat(),2));
	}

    /**
     * Returns a human readable String with information about the Track vector in the
     * form of "Track - Length: 0.4872 TrackLatitude: 0.328 TrackLongitude: 0.429"
     * @return human readable String
     */
	public String toString(){
		return "Track - Length: "+getTrackLength()+" TrackLatitude: "+getTrackLat()+ " TrackLongitude: " + getTrackLong();
	}
}
