package ch.zhaw.lakerouting.decisionScheme;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class DecisionTest {

	private double[][][] loc;
	private Decision de;
	private static double seamileToKm = 1.852;
	private static double tolerance = 1e-3;
	private Map<String, double[]> punkteCoord;
	private ArrayList<ArrayList<Graph>> test;

	@Before
	public void setUp() {
		de = new Decision();

		punkteCoord = new HashMap<String, double[]>();
		// punkteCoord.put("Äquator-Thailand", new double[] { 0, 0, 90, 0,
		// 10000.800 });
		punkteCoord.put("Äquator-Äquator(160)", new double[] { 0d, 0d, 160d,
				0d, 17779.2d });
		punkteCoord.put("Zürich-Pacific", new double[] { 8.05, 47.3, 226.88,
				-47.22, 26487.754 });
		punkteCoord.put("Zürich-Ankara", new double[] { 8.05, 47.3, 32.54,
				39.57, 26487.754 });
		punkteCoord.put("Zürich-Genf", new double[] { 8.05, 47.3, 6.09, 46.12,
				26487.754 });
		punkteCoord.put("Zürich-Peking", new double[] { 8.05, 47.3,
				116.3883333, 39.9288889, 12066.387 });
		punkteCoord.put("Zürich-Sao Paulo", new double[] { 8.05, 47.3,
				-46.6166667, -23.5333333, 9942.480 });

	}

	@Test
	public void getOrthoTest() {

		double orthoDistanceKm = 0;
		double timeOfArrival = 0;
		int i = 0;

		for (Map.Entry<String, double[]> entry : punkteCoord.entrySet()) {

			de.setLoc(de.graphe(entry.getValue()[0], entry.getValue()[1],
					entry.getValue()[2], entry.getValue()[3]));
			de.setMaxi(de.getLoc().length);
			de.setMaxj(de.getLoc()[0].length);
			de.setCoord(de.getLoc()[0][0].length);

			test = de.programmationDynamique(15);

			orthoDistanceKm = de.ortho(entry.getValue()[0],
					entry.getValue()[1], entry.getValue()[2],
					entry.getValue()[3])
					* seamileToKm;

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

			// assertEquals(timeOfArrival, distance, tolerance);
			i++;
		}

	}
}
