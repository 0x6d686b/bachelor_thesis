package test.ch.zhaw.lakerouting.navigation;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import ch.zhaw.lakerouting.datatypes.Coordinate;
import ch.zhaw.lakerouting.datatypes.WindVector;
import ch.zhaw.lakerouting.navigation.SailingDuration;
/**
 * SailingDuration Tester.
 * 
 * @author Fevzi Yükseldi, Mathias Habluetzel
 *
 */
public class SailingDurationTest {

	private SailingDuration sd;
	private Coordinate crd1;
	private Coordinate crd2;
	private WindVector v1;
	private WindVector v2;
	private WindVector t1;
	private WindVector t2;
	private double distance;
	
	/**
	 * Initializes the fields with test-data 
	 */
	@Before
	public void setUp() {
		sd = new SailingDuration();
		crd1 = new Coordinate();
		crd2 = new Coordinate();
		v1 = new WindVector(-0.09771200000046251, -0.021392000000104914);
		v2 = new WindVector(-0.46651100000067475, -0.10505100000015305);
		t1 = new WindVector(0.0753760000003485, 0.06666400000030767);
		t2 = new WindVector(0.35325300000050847, 0.3119920000004488);
		
		crd1.setLongitudeInDegree(9.522400000000001);
		crd1.setLatitudeInDegree(47.612);
		crd2.setLongitudeInDegree(9.5324);
		crd2.setLatitudeInDegree(47.611);
		distance = 0.4089183013218908;
	}
	
	/**
	 * This method tests, if the two durations are the same.
	 */
	@Test
	public void getSailingDurationTest() {
		double dur = sd.getSailingDuration(crd1, crd2, v1, v2, distance);
		double dur2 = sd.getSailingDuration(crd1, crd2, t1, t2, distance);
//		assertThat(dur, not(dur2));
		assertEquals(dur, dur2, 0);
	}
}
