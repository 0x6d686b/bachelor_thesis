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
 * @author Fevzi Yükseldi, Mathias Habluetzel
 * 
 */
public class SpeedComputation {

	private BoatSpeedDiagram field;
	private InterpolationAlgorithm bil;

	/**
	 * Loads the polaire-diagram.
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
	 * Computes the speed of the sailing boat which depends firstly on the
	 * intensity of the wind, secondly on the angle of incidence of the
	 * wind-vector onto the sails.
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
	 * Computes the wind-angle on the track.
	 * 
	 * @param tr
	 *            - Track
	 * @param v
	 *            - Windvector
	 * @return the wind-angle
	 */
	private double computeAngle(Track tr, WindVector v) {
		double scalar = v.getU() * tr.getTrackLong() + v.getV()
				* tr.getTrackLat();

		double checkOfInfinity = tr.getTrackLength() * v.getWindspeed();

		/* If the angle is zero, then it shouldn't divide */
		try {
			return 180 * (1 - Math.acos(scalar / checkOfInfinity) / Math.PI);
		} catch (ArithmeticException e) {
			return 180.0;
		}

	}

}
