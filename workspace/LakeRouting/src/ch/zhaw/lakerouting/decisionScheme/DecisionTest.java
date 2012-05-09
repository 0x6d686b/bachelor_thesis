package ch.zhaw.lakerouting.decisionScheme;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import ch.zhaw.lakerouting.datatypes.Node;
import org.junit.Before;
import org.junit.Test;

import ch.zhaw.lakerouting.datatypes.Coordinate;

public class DecisionTest {

	private Decision de;
	private static double seamileToKm = 1.852;
	private static double tolerance = 300;
	private Map<String, double[]> punkteCoord;
	private List<List<Node>> test;

	@Before
	public void setUp() {
		de = new Decision();

		punkteCoord = new HashMap<String, double[]>();
		// punkteCoord.put("Äquator-Thailand", new double[] { 0, 0, 90, 0,
		// 10000.800 });
//		punkteCoord.put("Äquator-Äquator(160)",
//				new double[] { 0d, 0d, 160d, 0d });
//		punkteCoord.put("Zürich-Pacific", new double[] { 8.05, 47.3, 226.88,
//				-47.22 });
//		punkteCoord.put("Zürich-Ankara", new double[] { 8.05, 47.3, 32.54,
//				39.57 });
//		punkteCoord
//				.put("Zürich-Genf", new double[] { 8.05, 47.3, 6.09, 46.12 });
//		punkteCoord
//		.put("Zürich-Genf", new double[] { 8, 47, 8, 48 });		
		punkteCoord
		.put("Zürich-Genf", new double[] { 9.40, 47.6, 9.6, 47.58 });	
//		punkteCoord.put("Zürich-Peking", new double[] { 8.05, 47.3,
//				116.3883333, 39.9288889 });
//		punkteCoord.put("Zürich-Sao Paulo", new double[] { 8.05, 47.3,
//				-46.6166667, -23.5333333 });
//		punkteCoord.put("Test-Zürich", new double[] { 359, 47.3,
//				8.05, 47.3 });
//		punkteCoord.put("Test-Zürich1", new double[] { 8.05, 47.3,
//				350, 47.3 });
	}

	@Test
	public void getOrthoTest() {

		double orthoDistanceKm = 0;
		double timeOfArrival = 0;
		int i = 0;
		Coordinate crd1 = new Coordinate();
		Coordinate crd2 = new Coordinate();

		for (Map.Entry<String, double[]> entry : punkteCoord.entrySet()) {

			crd1.setLongitudeInDegree(entry.getValue()[0]);
			crd1.setLatitudeInDegree(entry.getValue()[1]);
			crd2.setLongitudeInDegree(entry.getValue()[2]);
			crd2.setLatitudeInDegree(entry.getValue()[3]);

			int m, n,spread;
			m = 20;
			n = 10;
			spread = 4;
			test = de.createDecisionGraph(crd1, crd2, m, n, 10, spread);

			orthoDistanceKm = de.ortho(crd1, crd2);// * seamileToKm;

			System.out.println("OrthoTest " + i + ": " + entry.getKey()
					+ " distance:" + orthoDistanceKm + " sm");

			if (Math.abs(entry.getValue()[0] - entry.getValue()[2]) > 180) {
				orthoDistanceKm = 40003.2 - orthoDistanceKm;
				System.out.println("OrthoTest " + i + " (grössere Distanz): "
						+ entry.getKey() + " distance:" + orthoDistanceKm
						+ " km");
			}
			timeOfArrival = test.get(de.getMaxi() - 1).get(de.getMaxj() / 2)
					.getTimeOfArrival();
//					* seamileToKm;

			System.out.println("Time of Arrival: " + timeOfArrival + "\n");

//			assertEquals(timeOfArrival, orthoDistanceKm, tolerance);
			i++;
		}

	}
}
