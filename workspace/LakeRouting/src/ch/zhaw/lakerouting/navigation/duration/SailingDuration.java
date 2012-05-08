package ch.zhaw.lakerouting.navigation.duration;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import ch.zhaw.lakerouting.datatypes.Coordinate;
import ch.zhaw.lakerouting.datatypes.WindVector;
import ch.zhaw.lakerouting.navigation.speed.SpeedComputation;
import ch.zhaw.properties.PropertyLoad;

public class SailingDuration {

	private SpeedComputation sp;

	public SailingDuration() {
		try {
			URI identfier = new PropertyLoad()
					.getURIOfProperty("file.path.polaire","file");
			sp = new SpeedComputation(identfier);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public double getSailingDuration(Coordinate crd1, Coordinate crd2,
			WindVector v1, WindVector v2, double distance) {

		double checkOfInfinity = sp.speedBoat(crd1, crd2, v1)
				+ sp.speedBoat(crd1, crd2, v2);
		if (checkOfInfinity == 0.0)
			return 0.0;

		return (2 * distance) / checkOfInfinity;
	}
}
