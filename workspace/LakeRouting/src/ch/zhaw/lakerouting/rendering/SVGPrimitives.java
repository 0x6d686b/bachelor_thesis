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

package ch.zhaw.lakerouting.rendering;

import ch.zhaw.lakerouting.datatypes.Coordinate;
import org.joda.time.DateTime;

public class SVGPrimitives {

    public static String Initialization() {
        String s = new String();

        s += Header();
        s += Canvas();
        s += Defs();

        return s;
    }

    public static  String Header() {
        String s = new String();
        s += "<?xml version=\"1.0\" standalone=\"no\"?>\n" +
                "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \n" +
                "  \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">";
        return s;
    }

    public static String Canvas() {
        String s = new String();
        s += "<svg width=\"297mm\" height=\"210mm\" \n" +
                "     viewBox=\"0 0 100 100\" version=\"1.1\"\n" +
                "     xmlns=\"http://www.w3.org/2000/svg\">";
        return s;
    }

    public static String ClosingCanvas() {
        return "</svg>";
    }

    public static String Defs() {
        String s = new String();

        /**
         * Arrow tip, is used for the windfield vectors
         */
        s += "<defs>\n" +
                "    <marker id=\"Arrowtip\"\n" +
                "      viewBox=\"0 0 10 10\" refX=\"0\" refY=\"5\" \n" +
                "      markerUnits=\"strokeWidth\"\n" +
                "      markerWidth=\"10\" markerHeight=\"10\"\n" +
                "      orient=\"auto\">\n" +
                "      <path d=\"M 0 0 L 10 5 L 0 10 z\" />\n" +
                "    </marker>\n" +
                "  </defs>";

        return s;
    }

    public static String Infobox(DateTime starttime, DateTime arrivaltime, Coordinate start, Coordinate end, String boattype, DateTime fieldFiletime, String netresolution) {
        String s = new String();
        s += "<svg>" +
                "<g>\n" +
                "  <rect x=\"5\" y=\"5\" width=\"500\" height=\"100\"\n" +
                "    fill=\"white\" stroke=\"black\" stroke-width=\"2\" />\n" +
                "  <text x=\"20\" y=\"20\">Starttime: <tspan font-weight=\"bold\">"+ starttime.toString("HH:mmz") +"</tspan></text>\n" +
                "  <text x=\"20\" y=\"40\">Endtime: <tspan font-weight=\"bold\">"+ arrivaltime.toString("HH:mmz") +"</tspan> </text>\n" +
                "  <text x=\"20\" y=\"78\">Start coordinate: <tspan font-weight=\"bold\">"+ start.toString() +"</tspan></text>\n" +
                "  <text x=\"20\" y=\"98\">End coordinate: <tspan font-weight=\"bold\">"+ end.toString() +"</tspan> </text>\n" +
                "  <text x=\"250\" y=\"20\">Boat type: "+ boattype +"</text>\n" +
                "  <text x=\"250\" y=\"40\">Windfield file time: "+ fieldFiletime.toString("HH:mmz") +"</text>\n" +
                "  <text x=\"250\" y=\"60\">Decision net resolution: "+ netresolution +"</text>\n" +
                "</g>\n" +
                "</svg>";

        return s;
    }

    public static String Windarrow(double x, double y, double u, double v, String color) {
        return "<path d=\"M " + x + " " + y + " L " + (x+u) + " " + (y+v) + "\" stroke-width=\"0.1\" stroke=\"" + color + "\" marker-end=\"url(#Arrowtip)\" />";
    }
}
