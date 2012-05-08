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
import ch.zhaw.lakerouting.datatypes.WindVector;
import ch.zhaw.lakerouting.interpolation.windfield.Windfield;

import java.util.AbstractList;
import java.util.List;

public class Rendering {
    private static int OFFSET = 2;

    public String renderWindfield(Windfield f) {
        String s = new String();
        s += SVGPrimitives.Initialization();
        /**
         * A word of warning:
         * This only works if the delta_lat and delta_lng are of the same
         * value meaning we have a squarish windfield grid. If not, this
         * has to be reworked and you have to differ between step_x and
         * step_y since they will be different when you want to scale
         * proportionally.
         */
        double step = Math.min(96 / f.getMetadata().getCountLatVectors() , 96 / f.getMetadata().getCountLngVectors());
        List<List<WindVector>> wv = f.getField();
        for(int i = 0; i < wv.size(); ++i) {
            for (int j = 0; j < wv.get(0).size(); ++j) {
                double u = wv.get(i).get(j).getU();
                double v = wv.get(i).get(j).getV();

                s += SVGPrimitives.Windarrow(step * j + OFFSET, step * i + OFFSET, u, v, "Black");
            }
        }

        s += SVGPrimitives.Infobox( (4 * OFFSET + wv.get(0).size()),
                                    f.getMetadata().getDate(),
                                    f.getMetadata().getDate(),
                                    f.getMetadata().getNorthWestCorner(),
                                    f.getMetadata().getSouthEastCorner(),
                                    "-NONE-",
                                    f.getMetadata().getDate(),
                                    wv.size() + "x" + wv.get(0).size());

        s += SVGPrimitives.ClosingCanvas();

        return s;
    }

}
