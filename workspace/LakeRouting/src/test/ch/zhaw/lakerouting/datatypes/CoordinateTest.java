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

package test.ch.zhaw.lakerouting.datatypes;

import ch.zhaw.lakerouting.datatypes.Coordinate;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

/**
 * Coordinate Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Mrz 20, 2012</pre>
 */
public class CoordinateTest {
    private Coordinate foo;

    @Before
    public void before() throws Exception {
        foo = new Coordinate();
        foo.setLongitudeInDegree(25.3456);
        foo.setLatitudeInDegree(34.3334);
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getLongitude()
     */
    @Test
    public void testGetLongitude() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: setLongitude(double l)
     */
    @Test
    public void testSetLongitude() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getLatitude()
     */
    @Test
    public void testGetLatitude() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: setLatitude(double l)
     */
    @Test
    public void testSetLatitude() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: toString()
     */
    @Test
    public void testToString() throws Exception {
//TODO: Test goes here... 
    }


} 
