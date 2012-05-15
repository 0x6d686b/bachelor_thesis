package ch.zhaw.lakerouting.navigation;

import ch.zhaw.lakerouting.datatypes.Coordinate;
import ch.zhaw.lakerouting.datatypes.Track;

/**
 * This class computes the Track and the Heading.
 * 
 * @author Fevzi Yükseldi, Mathias Habluetzel
 * 
 */
public class TrackComputation {

	/**
	 * Computes the heading-angle at location crd1 of a mobile sailing from
	 * there to location crd2.
	 * 
	 * @param crd1
	 *            - Coordinates of the first location
	 * @param crd2
	 *            - Coordinates of the second location
	 * @return the heading-angle
	 */
	public double computeHeading(Coordinate crd1, Coordinate crd2) {

		Track t = computeScalar(crd1, crd2);
		/* Convert the value in degree */
		double nTrack1 = 180 * arcTangente(t) / Math.PI;

		if (nTrack1 > 90) {
			return 450d - nTrack1;
		} else {
			return 90d - nTrack1;
		}
	}

	/**
	 * Computes an arctan of the track. A classical arctan computation delivers
	 * the heading.
	 * 
	 * @param t
	 *            - track
	 * @return angle in radian
	 */
	private double arcTangente(Track t) {
		double trackLong = t.getTrackLong();
		double trackLat = t.getTrackLat();
		double result = 0.0;

		/* Carefully designed in order to catch all possible configurations */
		if ((trackLong > 0) && (trackLat > 0)) {
			result = Math.atan(trackLat / trackLong);
		} else if ((trackLong < 0) && (trackLat > 0)) {
			result = Math.PI - Math.atan(trackLat / Math.abs(trackLong));
		} else if ((trackLong < 0) && (trackLat < 0)) {
			result = Math.PI + Math.atan(trackLat / trackLong);
		} else if ((trackLong > 0) && (trackLat < 0)) {
			result = 2 * Math.PI - Math.atan(Math.abs(trackLat) / trackLong);
		} else if ((trackLong == 0) && (trackLat > 0)) {
			result = Math.PI / 2;
		} else if ((trackLong == 0) && (trackLat < 0)) {
			result = 3 * Math.PI / 2;
		} else if ((trackLong < 0) && (trackLat == 0)) {
			result = Math.PI;
		} else if ((trackLong > 0) && (trackLat == 0)) {
			result = 0.0;
		}
		return result;
	}

	/**
	 * Computes the crossprduct of two locations.
	 * Currently not used.
	 * 
	 * @param crd1
	 *            - Coordinates of the first location
	 * @param crd2
	 *            - Coordinates of the second location
	 * @return a three spatial dimensional vector
	 */
	public double[] computeCross(Coordinate crd1, Coordinate crd2) {
		double[] cross = new double[3];

		double theta1 = crd1.getLongitudeInDegree();
		double phi1 = crd1.getLatitudeInDegree();
		double theta2 = crd2.getLongitudeInDegree();
		double phi2 = crd2.getLatitudeInDegree();

		cross[0] = square(Math.cos(phi1)) * Math.cos(phi2) * Math.sin(theta1)
				* Math.sin(theta1 - theta2) + Math.cos(theta2) * Math.cos(phi2)
				* square(Math.sin(phi1)) - Math.cos(theta1) * Math.cos(phi1)
				* Math.sin(phi1) * Math.sin(phi2);

		cross[1] = (-Math.cos(theta1))
				* square(Math.cos(phi1))
				* Math.cos(phi2)
				* Math.sin(theta1 - theta2)
				+ Math.sin(phi1)
				* (Math.cos(phi2) * Math.sin(theta2) * Math.sin(phi1) - Math
						.cos(phi1) * Math.sin(theta1) * Math.sin(phi2));

		cross[2] = Math.cos(phi1)
				* (-Math.cos(theta1 - theta2) * Math.cos(phi2) * Math.sin(phi1) + Math
						.cos(phi1) * Math.sin(phi2));

		return cross;
	}

	/**
	 * Calculates the square of a value.
	 * 
	 * @param x
	 *            - the value which should be squared.
	 * @return the squared value
	 */
	private double square(double x) {
		return x * x;
	}

	/**
	 * Computes the Long/Lat of a Track.
	 * 
	 * @param crd1
	 *            - Coordinates of the first location
	 * @param crd2
	 *            - Coordinates of the second location
	 * @return the track
	 */
	public Track computeScalar(Coordinate crd1, Coordinate crd2) {
		Track t = new Track(crd1, crd2);
		return t;
	}

}
