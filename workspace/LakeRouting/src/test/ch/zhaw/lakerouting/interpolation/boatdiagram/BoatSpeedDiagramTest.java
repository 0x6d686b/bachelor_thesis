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

package test.ch.zhaw.lakerouting.interpolation.boatdiagram;

import ch.zhaw.lakerouting.interpolation.algorithms.Bilinear;
import ch.zhaw.lakerouting.interpolation.algorithms.InterpolationAlgorithm;
import ch.zhaw.lakerouting.interpolation.boatdiagram.BoatSpeedDiagram;
import ch.zhaw.lakerouting.interpolation.boatdiagram.loader.BoatFieldLoader;
import ch.zhaw.lakerouting.interpolation.boatdiagram.loader.CSVBoatFieldLoader;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;

/**
 * BoatSpeedDiagram Tester.
 *
 * @author <Authors name>
 * @since <pre>03/31/2012</pre>
 * @version 1.0
 */
public class BoatSpeedDiagramTest {

    private static URI testfile;

    @BeforeClass
    public static void oneTimeSetup() {
        try {
            testfile = new URI("/var/tmp/interpolationtest.csv");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {
            FileWriter fstream = new FileWriter(testfile.getPath());
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(",35,40,45,60,100,120,140,165,175,180\n");
            out.write("1,0.3,0.6,0.8,1,1.2,1.2,1.1,0.8,0.65,0.6\n");
            out.write("5,1.9,2,2.1,2.35,2.7,2.8,2.84,2.95,2.9,2.85\n");
            out.write("10,3.3,3.47,3.6,3.8,3.9,3.95,4.2,4.39,4.4,4.35\n");
            out.write("15,4.01,4.2,4.4,5,5.1,5.2,5.35,5.5,5.44,5.42\n");
            out.write("20,4.2,4.5,4.8,5.6,5.8,6,6.3,6.4,6.32,6.3\n");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Close the output stream
    }

    @AfterClass
    public static void after() {
        new File(testfile.getPath()).delete();
    }

//    public BoatSpeedDiagramTest(String name) {
//        super(name);
//    }
//
//    public void setUp() throws Exception {
//        super.setUp();
//    }
//
//    public void tearDown() throws Exception {
//        super.tearDown();
//    }
//
//    public static Test suite() {
//        return new TestSuite(BoatSpeedDiagramTest.class);
//    }

    @Test
    public void testInterpolation() throws Exception{
        testfile = new URI("file", "/var/tmp/interpolationtest.csv", "");
        BoatFieldLoader loader = new CSVBoatFieldLoader();
        BoatSpeedDiagram field = new BoatSpeedDiagram();
        field.loadDiagram(loader, testfile);
        InterpolationAlgorithm bil = new Bilinear();
        assertEquals(3.98924, field.interpolate(47.6, 12.47, bil), 0.1);
        assertEquals(2.87871, field.interpolate(124.2, 5.23, bil), 0.1);
    }
}
