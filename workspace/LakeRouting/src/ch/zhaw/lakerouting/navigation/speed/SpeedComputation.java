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

package ch.zhaw.lakerouting.navigation.speed;

import java.net.URI;

import ch.zhaw.lakerouting.datatypes.Coordinate;
import ch.zhaw.lakerouting.datatypes.Track;
import ch.zhaw.lakerouting.datatypes.WindVector;
import ch.zhaw.lakerouting.interpolation.algorithms.Bilinear;
import ch.zhaw.lakerouting.interpolation.algorithms.InterpolationAlgorithm;
import ch.zhaw.lakerouting.interpolation.boatdiagram.BoatSpeedDiagram;
import ch.zhaw.lakerouting.interpolation.boatdiagram.loader.BoatFieldLoader;
import ch.zhaw.lakerouting.interpolation.boatdiagram.loader.CSVBoatFieldLoader;
import ch.zhaw.lakerouting.navigation.TrackComputation;

/**
 * Computes the speed of a sailing boat on a given track.
 * 
 * @author Fevzi Yükseldi
 * @version 1.0-stable
 * @since 1.0
 */
public class SpeedComputation {

	private BoatSpeedDiagram field;
	private InterpolationAlgorithm bil;

	/**
	 * Constructor which loads the polar diagram field of the
     * boat
     *
     * <p>Now think of SpeedComputation as per boat or ship type. You therefore
     * can use different SpeedComputation for different boats and you would be able
     * to calculate possibly different best paths for different boat types.</p>
     *
     * <p>Also you can use this for calculating the position of other concurrent
     * ships in a regatta if they are using the optimal track. Using this you could
     * be able to come up with a strategy how and when you'll be able to steal their
     * wind.</p>
	 * 
	 * @param identifier
	 *            - The URI of the file
	 */
	public SpeedComputation(URI identifier) {

		BoatFieldLoader loader = new CSVBoatFieldLoader();
		field = new BoatSpeedDiagram();
		field.loadDiagram(loader, identifier);
		bil = new Bilinear();

	}

	/**
	 * Computes the speed of the sailing boat
     *
     * <p>The travelling speed of a boat which depends firstly on the
     * intensity of the wind, secondly on the angle of incidence of the
     * wind-vector onto the sails.</p>
	 * 
	 * @param crd1
	 *            - Coordinates of the first location
	 * @param crd2
	 *            - Coordinates of the second location
	 * @param v
	 *            - Windvector
	 * @return the speed of the boat
	 */
	public double speedBoat(Coordinate crd1, Coordinate crd2, WindVector v) {

		double wSpeed = v.getWindspeed();
		Track track = new TrackComputation().computeScalar(crd1, crd2);

		double angle = computeAngle(track, v);

		/*
		 * This line is the hole module yacht in mathematica It interpolates the
		 * speed from the polaire-diagram
		 */
		return field.interpolate(angle, wSpeed, bil);
	}

	/**
	 * Computes the attack angle of the wind on the track
	 * 
	 * @param tr
	 *            - Track
	 * @param v
	 *            - Windvector
	 * @return the wind-angle
	 */
	private double computeAngle(Track tr, WindVector v) {
		double scalar = v.getU() * tr.getTrackLong() + v.getV() * tr.getTrackLat();

		/* If the angle is zero, then it shouldn't divide */
		try {
			return 180 * (1 - Math.acos(scalar / (tr.getTrackLength() * v.getWindspeed())) / Math.PI);
		} catch (ArithmeticException e) {
			return 180.0;
		}

	}

}
