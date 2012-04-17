/*
 * Copyright (c) 2012, M. Hablützel, F. Yükseldi, J. Ambühl
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the ZHAW, MeteoSchweiz nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL M. Hablützel, F. Yükseldi, J. Ambühl BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ch.zhaw.lakerouting.navigation;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ch.zhaw.lakerouting.datatypes.Coordinate;

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
		punkteCoord.put("{8, 47} {7, 46}", new double[] { 8, 47, 7,46,
				214.905 });
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
	public void track12Test() {

		Coordinate crd1 = new Coordinate();
		Coordinate crd2 = new Coordinate();
		
		for (Map.Entry<String, double[]> entry : punkteCoord.entrySet()) {


			crd1.setLongitudeInDegree(entry.getValue()[0]);
			crd1.setLatitudeInDegree(entry.getValue()[1]);
			crd2.setLongitudeInDegree(entry.getValue()[2]);
			crd2.setLatitudeInDegree(entry.getValue()[3]);
			
			double rslt = trackComp.track12(crd1, crd2);

			System.out.println(entry.getKey() + " Track in Degrees: " + rslt);

			assertEquals(rslt, entry.getValue()[4], tolerance);
		}
	}
}
