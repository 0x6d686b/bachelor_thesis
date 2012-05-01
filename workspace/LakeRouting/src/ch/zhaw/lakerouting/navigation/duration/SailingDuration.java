package ch.zhaw.lakerouting.navigation.duration;

import ch.zhaw.lakerouting.datatypes.Coordinate;
import ch.zhaw.lakerouting.datatypes.WindVector;
import ch.zhaw.lakerouting.navigation.speed.SpeedComputation;

public class SailingDuration {

	private SpeedComputation sp;

	public SailingDuration() {
		sp = new SpeedComputation();
	}

	public double getSailingDuration(Coordinate crd1, Coordinate crd2,
			WindVector v1, WindVector v2, double distance) {

		double checkOfInfinity = sp.speedBoat(crd1, crd2, v1) + sp.speedBoat(
				crd1, crd2, v2);
		if(checkOfInfinity==0.0)
			return 0.0;
		
		return (2 * distance) / checkOfInfinity;
	}
}
