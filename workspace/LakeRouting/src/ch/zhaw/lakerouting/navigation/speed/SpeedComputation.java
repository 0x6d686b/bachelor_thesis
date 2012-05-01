package ch.zhaw.lakerouting.navigation.speed;

import java.net.URI;
import java.net.URISyntaxException;

import ch.zhaw.lakerouting.datatypes.Coordinate;
import ch.zhaw.lakerouting.datatypes.Track;
import ch.zhaw.lakerouting.datatypes.WindVector;
import ch.zhaw.lakerouting.interpolation.algorithms.Bilinear;
import ch.zhaw.lakerouting.interpolation.algorithms.InterpolationAlgorithm;
import ch.zhaw.lakerouting.interpolation.boatdiagram.BoatSpeedDiagram;
import ch.zhaw.lakerouting.interpolation.boatdiagram.loader.BoatFieldLoader;
import ch.zhaw.lakerouting.interpolation.boatdiagram.loader.CSVBoatFieldLoader;
import ch.zhaw.lakerouting.navigation.TrackComputation;

public class SpeedComputation {

	private BoatSpeedDiagram field;
	private InterpolationAlgorithm bil;

	public SpeedComputation() {
		URI testfile;
		try {
			testfile = new URI(
					"file",
					"C:/Users/fevzi/Desktop/ZHAW/BA(furu)/git/lakerouting/workspace/LakeRouting/interpolationtest.csv",
					"");
			BoatFieldLoader loader = new CSVBoatFieldLoader();
			field = new BoatSpeedDiagram();
			field.loadDiagram(loader, testfile);
			bil = new Bilinear();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public double speedBoat(Coordinate crd1, Coordinate crd2, WindVector v) {

		double wSpeed = v.getWindspeed();
//		System.out.println("wspeed: " + wSpeed);
		Track track = new TrackComputation().computeScalar(crd1, crd2);
//		System.out.println(track.toString());
		
		double angle = computeAngle(track, v);
//		System.out.println("Angle: " + angle);
		return yacht(wSpeed, angle);
	}

	private double computeAngle(Track tr, WindVector v) {
		double scalar = v.getU() * tr.getTrackLong() + v.getV()
				* tr.getTrackLat();
//		System.out.println("Scalar: " + scalar);
		double checkOfInfinity = tr.getTrackLength() * v.getWindspeed();
		if (checkOfInfinity == 0.0)
			return 180.0;

		return 180 * (1 - Math.acos(scalar / checkOfInfinity) / Math.PI);
	}

	private double yacht(double wSpeed, double angle) {
		// Abfrage machen
//		double test = interpoler.interpolate(angle, wSpeed, field, bil);
		double test = field.interpolate(angle, wSpeed, bil);
//		System.out.println("Interpoler:"+test);
		return test;
	}

}
