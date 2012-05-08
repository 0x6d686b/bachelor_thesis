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

package test.ch.zhaw.lakerouting.interpolation.windfield;

import ch.zhaw.lakerouting.datatypes.Coordinate;
import ch.zhaw.lakerouting.datatypes.WindVector;
import ch.zhaw.lakerouting.interpolation.algorithms.Bilinear;
import ch.zhaw.lakerouting.interpolation.algorithms.InterpolationAlgorithm;
import ch.zhaw.lakerouting.interpolation.windfield.Windfield;
import ch.zhaw.lakerouting.interpolation.windfield.WindfieldContainer;
import ch.zhaw.lakerouting.interpolation.windfield.loader.SpaceWindFieldLoader;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * WindfieldContainer Tester.
 *
 * @author Mathias Hablützel
 * @since <pre>Apr 1, 2012</pre>
 * @version 1.0
 */
public class WindfieldContainerTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     *
     * Method: bulkLoadWindfield(URI identifier)
     *
     */
    @Test
    public void testBulkLoadWindfield() throws Exception {
        SpaceWindFieldLoader loader = new SpaceWindFieldLoader();
        Coordinate c = new Coordinate();
        InterpolationAlgorithm bil = new Bilinear();

        WindfieldContainer foo = new WindfieldContainer();
        foo.bulkLoadWindfield(new URI("file", "/var/tmp/11072915_905.dat", ""), loader);

        System.out.print("Single point interpolation: ");
        c.setLongitudeInDegree(09.34);
        c.setLatitudeInDegree(47.48);
        WindVector result = foo.get(0).interpolate(c, bil);
        System.out.println(result.toString());

        System.out.println("\n\nInterpolation on decision net: ");
        List<List<Coordinate>> net = new ArrayList<List<Coordinate>>();
        for (int i = 0; i < 4; ++i) {
        	List<Coordinate> row = new ArrayList<Coordinate>();
            for (int j = 0; j < 5; ++j) {
                Coordinate k = new Coordinate();
                k.setLongitudeInDegree(c.getLongitudeInDegree() + j*0.01);
                k.setLatitudeInDegree(c.getLatitudeInDegree() + i*0.01);
                row.add(k);
            }
            net.add(row);
        }
        double i = 0;
        List<List<WindVector>> vectorField =  foo.get(22).interpolateOnDecisionNet(net,bil);
        for (List<WindVector> vectorRow : vectorField) {
            System.out.print("{{" +i + ",0},");
            for(WindVector vector : vectorRow) {
//                System.out.print(vector.mathematicaVectorOutput() + ",");
            }
            System.out.print("},");
            i += 0.1;
        }
        foo = foo.bulkInterpolateOnDecisionNet(net,bil);
        Windfield a = foo.get(0);
        Windfield b = foo.get(4);
        System.out.println();
    }


} 
