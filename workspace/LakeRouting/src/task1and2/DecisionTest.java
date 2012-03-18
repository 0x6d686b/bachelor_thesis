package task1and2;

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
		// punkteCoord.put("Zürich-Peking", new double[] { 8.05, 47.3,
		// 116.3883333, 39.9288889, 12066.387 });
		// punkteCoord.put("Zürich-Sao Paulo", new double[] { 8.05, 47.3,
		// -46.6166667, -23.5333333, 9942.480 });

	}

	@Test
	public void getOrthoTest() {
		
		double distance = 0;
		int i = 0;
//		Graph obj = test.get(de.getMaxi()-1).get(de.getMaxj()/2);
//		System.out.println("sadasdasd");
//		System.out.println("Time: "+obj.getTimeOfArrival()*1.852);
		for (Map.Entry<String, double[]> entry : punkteCoord.entrySet()) {
			
			de.setLoc(de.graphe(entry.getValue()[0], entry.getValue()[1],
					entry.getValue()[2], entry.getValue()[3] ));
			de.setMaxi(de.getLoc().length);
			de.setMaxj(de.getLoc()[0].length);
			de.setCoord(de.getLoc()[0][0].length);
			
//			test = de.programmationDynamique(15);
			
			distance = de.ortho(entry.getValue()[0], entry.getValue()[1],
					entry.getValue()[2], entry.getValue()[3])
					* seamileToKm;
			
			System.out.println("OrthoTest " + i + ": " + entry.getKey()
					+ " distance:" + distance+" ");
			
			if (Math.abs(entry.getValue()[0]-entry.getValue()[2])>180)
				distance=40003.2-distance;

			System.out.println("OrthoTest " + i + ": " + entry.getKey()
					+ " distance:" + distance+" ");

			assertEquals(entry.getValue()[4], distance, tolerance);
			i++;
		}
		//System.out.println("OrthoTest " + i + ": Äquator(0,0) - Thailand(0,90)");

	}
}
