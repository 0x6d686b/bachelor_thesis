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
import ch.zhaw.lakerouting.interpolation.windfield.loader.SpaceWindFieldLoader;
import ch.zhaw.lakerouting.interpolation.windfield.loader.WindFieldLoader;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.net.URI;

/**
 * Windfield Tester.
 *
 * @author Mathias Hablützel
 * @since <pre>Apr 1, 2012</pre>
 * @version 1.0
 */
public class WindfieldTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     *
     * Method: loadDiagram(T fieldplane, URI uri)
     *
     */
    @Test
    public void testLoadDiagram() throws Exception {
//TODO: Test goes here... 
    }

    /**
     *
     * Method: interpolate(Coordinate c, InterpolationAlgorithm algorithm)
     *
     */
    @Test
    public void testInterpolate() throws Exception {
        WindFieldLoader loader_wind = new SpaceWindFieldLoader();
        InterpolationAlgorithm bil = new Bilinear();
        Windfield windfield = new Windfield();
        windfield.loadDiagram(loader_wind, new URI("file", "/var/tmp/testfilewindfield.dat", ""));
        Coordinate c = new Coordinate();
        c.setLongitudeInDegree(09.01);
        c.setLatitudeInDegree(45.525);
        WindVector result = windfield.interpolate(c,bil);
        System.out.println(result.toString());
    }


} 
