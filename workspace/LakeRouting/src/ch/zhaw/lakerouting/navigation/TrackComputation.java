package ch.zhaw.lakerouting.navigation;

import ch.zhaw.lakerouting.datatypes.Coordinate;
import ch.zhaw.lakerouting.datatypes.Track;

public class TrackComputation {

	public double track12(Coordinate crd1, Coordinate crd2) {

		Track t = computeScalar(crd1, crd2);
		double nTrack1=180*arcTangente(t)/Math.PI;
		
		if(nTrack1>90){
			return 450d - nTrack1;
		}else
		{
			return 90d - nTrack1;
		}
	}

	private double arcTangente(Track t) {
		double trackLong = t.getTrackLong();
		double trackLat = t.getTrackLat();
		double result = 0.0;

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

	public double[] computeCross(Coordinate crd1, Coordinate crd2) {
		double[] cross = new double[3];

		double theta1 = crd1.getLongitudeInDegree();
		double phi1 = crd1.getLatitudeInDegree();
		double theta2 = crd2.getLongitudeInDegree();
		double phi2 = crd2.getLatitudeInDegree();

		cross[0] = quadrat(Math.cos(phi1)) * Math.cos(phi2) * Math.sin(theta1)
				* Math.sin(theta1 - theta2) + Math.cos(theta2) * Math.cos(phi2)
				* quadrat(Math.sin(phi1)) - Math.cos(theta1) * Math.cos(phi1)
				* Math.sin(phi1) * Math.sin(phi2);

		cross[1] = (-Math.cos(theta1))
				* quadrat(Math.cos(phi1))
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

	private double quadrat(double x) {
		return x * x;
	}

	public Track computeScalar(Coordinate crd1, Coordinate crd2) {
		Track t = new Track(crd1, crd2);
		return t;
	}
}
