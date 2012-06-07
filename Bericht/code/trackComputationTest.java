public class TrackComputationTest {

	private Map<String, double[]> punkteCoord;
	private static double tolerance = 1e-1;
	private TrackComputation trackComp;

	@Before
	public void setUp() {
		trackComp = new TrackComputation();

		punkteCoord = new HashMap<String, double[]>();
		punkteCoord.put("{8, 47} {8.01, 48}", new double[] { 8, 47, 8.01, 48,
				0.383 });
		punkteCoord.put("{8, 47} {7, 46}",
				new double[] { 8, 47, 7, 46, 214.905 });
		punkteCoord.put("{8, 47} {7.99, 48}", new double[] { 8, 47, 7.99, 48,
				359.616 });
		punkteCoord
				.put("{8, 47} {8, 46}", new double[] { 8, 47, 8, 46, 180.0 });
		punkteCoord.put("{8, 47} {179.9, 47}", new double[] { 8, 47, 179.9, 47,
				5.529 });
		punkteCoord.put("{0.1, 47} {179.9, 47}", new double[] { 0.1, 47, 179.9,
				47, 0.136 });
	}

	@Test
	public void computeHeadingTest() {

		Coordinate crd1 = new Coordinate();
		Coordinate crd2 = new Coordinate();

		for (Map.Entry<String, double[]> entry : punkteCoord.entrySet()) {

			crd1.setLongitudeInDegree(entry.getValue()[0]);
			crd1.setLatitudeInDegree(entry.getValue()[1]);
			crd2.setLongitudeInDegree(entry.getValue()[2]);
			crd2.setLatitudeInDegree(entry.getValue()[3]);

			double rslt = trackComp.computeHeading(crd1, crd2);

			System.out.println(entry.getKey() + " Track in Degrees: " + rslt);

			assertEquals(rslt, entry.getValue()[4], tolerance);
		}
	}
}