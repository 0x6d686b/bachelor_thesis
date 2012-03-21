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
 * Created by IntelliJ IDEA. User: mhk Date: 20.03.12 Time: 09:53 To change this
 * template use File | Settings | File Templates.
 */
public class Coordinate {
	// theta
	private BigDecimal longitude;
	// phi
	private BigDecimal latitude;
	private Boolean isDegree = true;

	public Coordinate(double lng, double lat) {
		setLongitude(lng);
		setLatitude(lat);
	}

	public double getLongitude() {
		return longitude.doubleValue();
	}

	public void setLongitude(double l) throws IllegalArgumentException {
		//
//		if (l >= 180.0 || l < -180.0)
//			throw new IllegalArgumentException("Longitude is out of range.");
		this.longitude = BigDecimal.valueOf(l);
	}

	public double getLatitude() {
		return latitude.doubleValue();
	}

	public void setLatitude(double l) throws IllegalArgumentException {
//		if (l < -90.0 || l > 90.0)
//			throw new IllegalArgumentException("Latitude is out of range.");
		this.latitude = BigDecimal.valueOf(l);
	}

	public Boolean isDegree() {
		return isDegree;
	}

	public void setIsDegree(Boolean isDegree) {
		this.isDegree = isDegree;
	}

	public String toString() {
		return "Longitude: " + longitude + "°, Latitude: " + latitude;
	}

	/**
	 * This method converts the degrees into radians. It makes an operation how
	 * degree*PI/180
	 * 
	 * @returns a double array [2]
	 */
	public double[] getRadian() {
		double[] radian = new double[2];
		radian[0] = getLongitude() * Math.PI / 180;
		radian[1] = getLatitude() * Math.PI / 180;
		return radian;
	}

	/**
	 * Converts the actual Coordinate from degrees to radians.
	 * 
	 * Important: the negative degrees are represented as negative radians too
	 */
	public void convertToRadian() {
		if(isDegree()){
			setLatitude(getLatitude() * Math.PI / 180);
			setLongitude(getLongitude() * Math.PI / 180);
			setIsDegree(false);
		}
	}
	/**
	 * Converts the actual Coordinate from radians to degrees.
	 */
	public void convertToDegree() {
		if(!isDegree()){
			setLatitude(getLatitude() * 180 /Math.PI);
			setLongitude(getLongitude() * 180 /Math.PI);
			setIsDegree(true);
		}
	}
}
