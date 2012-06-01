package ch.zhaw.lakerouting.navigation;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;

import ch.zhaw.lakerouting.datatypes.Coordinate;
import ch.zhaw.lakerouting.datatypes.WindVector;
import ch.zhaw.lakerouting.navigation.speed.SpeedComputation;
import ch.zhaw.properties.PropertyLoad;

/**
 * Computes the duration of a distance based on windvectors and speedBoat.
 * 
 * @author Fevzi Yükseldi, Mathias Habluetzel
 * 
 */
public class SailingDuration {

	private SpeedComputation sp;

	private Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * Ascertains the file and its path and generates an instance of
	 * SpeedComputation.
	 */
	public SailingDuration() {
		try {
			/* Loads the path of the file from the config-File */
			URI identfier = new PropertyLoad().getURIOfProperty(
					"file.path.polaire", "file");
			sp = new SpeedComputation(identfier);
		} catch (URISyntaxException e) {
			logger.error("URI Exception occured.\n"+e);
			e.printStackTrace();
		} catch (IOException e1) {
			logger.error("IOException occured.\n"+e1);
			e1.printStackTrace();
		}
	}

	/**
	 * Computes the distance between two locations considering the windvectors.
	 * 
	 * @param crd1
	 *            - Coordinates of the first location
	 * @param crd2
	 *            - Coordinates of the second location
	 * @param v1
	 *            - windVector at coordinate crd1
	 * @param v2
	 *            - windVector at coordinate crd2
	 * @param distance
	 *            - the ortho-distance between two locations
	 * @return the effective duration between two locations
	 */
	public double getSailingDuration(Coordinate crd1, Coordinate crd2,
			WindVector v1, WindVector v2, double distance) {

		/*
		 * The speed of the Boat once with windvector v1 and once with
		 * windvector v2.
		 */
		double speed = sp.speedBoat(crd1, crd2, v1)
				+ sp.speedBoat(crd1, crd2, v2);
		
		/* If the speed is zero, then it shouldn't divide */
		try {
			return (2 * distance) / speed;
		} catch (ArithmeticException e) {
			logger.error("Tried to divide by zero. So it returned zero.");
			return 0.0;
		}
	}
}
