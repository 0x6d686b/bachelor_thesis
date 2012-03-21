package ch.zhaw.lakerouting.decisionScheme;

import static org.junit.Assert.*;
import ch.zhaw.lakerouting.interpolation.algorithms.Coordinate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class DecisionTest {

	private Decision de;
	private static double seamileToKm = 1.852;
	private static double tolerance = 300;
	private Map<String, double[]> punkteCoord;
	private ArrayList<ArrayList<Graph>> test;

	@Before
	public void setUp() {
		de = new Decision();

		punkteCoord = new HashMap<String, double[]>();
		// punkteCoord.put("Äquator-Thailand", new double[] { 0, 0, 90, 0,
		// 10000.800 });
		punkteCoord.put("Äquator-Äquator(160)",
				new double[] { 0d, 0d, 160d, 0d });
		punkteCoord.put("Zürich-Pacific", new double[] { 8.05, 47.3, 226.88,
				-47.22 });
		punkteCoord.put("Zürich-Ankara", new double[] { 8.05, 47.3, 32.54,
				39.57 });
		punkteCoord
				.put("Zürich-Genf", new double[] { 8.05, 47.3, 6.09, 46.12 });
		punkteCoord.put("Zürich-Peking", new double[] { 8.05, 47.3,
				116.3883333, 39.9288889 });
		punkteCoord.put("Zürich-Sao Paulo", new double[] { 8.05, 47.3,
				-46.6166667, -23.5333333 });
	}

	@Test
	public void getOrthoTest() {

		double orthoDistanceKm = 0;
		double timeOfArrival = 0;
		int i = 0;
		Coordinate crd1, crd2;

		for (Map.Entry<String, double[]> entry : punkteCoord.entrySet()) {

			crd1 = new Coordinate(entry.getValue()[0], entry.getValue()[1]);
			crd2 = new Coordinate(entry.getValue()[2], entry.getValue()[3]);

			de.setLoc(de.graphe(crd1, crd2));
			de.setMaxi(de.getLoc().length);
			de.setMaxj(de.getLoc()[0].length);
			de.setCoord(de.getLoc()[0][0].length);

			test = de.programmationDynamique(10);

			orthoDistanceKm = de.ortho(crd1, crd2) * seamileToKm;

			System.out.println("OrthoTest " + i + ": " + entry.getKey()
					+ " distance:" + orthoDistanceKm + " km");

			if (Math.abs(entry.getValue()[0] - entry.getValue()[2]) > 180) {
				orthoDistanceKm = 40003.2 - orthoDistanceKm;
				System.out.println("OrthoTest " + i + " (grössere Distanz): "
						+ entry.getKey() + " distance:" + orthoDistanceKm
						+ " km");
			}
			timeOfArrival = test.get(de.getMaxi() - 1).get(de.getMaxj() / 2)
					.getTimeOfArrival()
					* seamileToKm;

			System.out.println("Time of Arrival: " + timeOfArrival + " km\n");

			assertEquals(timeOfArrival, orthoDistanceKm, tolerance);
			i++;
		}

	}
}
