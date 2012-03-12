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

package ch.zhaw.lakerouting.interpolation.algorithms;

import java.net.URI;

/**
 * Created by IntelliJ IDEA.
 * User: mhk
 * Date: 12.03.12
 * Time: 16:27
 */
public class Windfield {
    private Double[][] field;
    private boolean loadArray (FieldLoader field) {
        this.field = field.convertToArray().clone();
        if (this.field != null)
            return true;
        return false;
    }
    public Double[][] getRange (double x, double y) {
        int from_x = 1;
        int from_y = 1;
        int to_x = 2;
        int to_y = 2;
        for (int i = 0; i < field.length; i++) {
            if ( x <= field[i][0] ) {
                from_x = i;
                to_x = i+1;
                break;
            }
        }
        for (int i = 0; i < field[0].length; i++) {
            if( y <= field[0][i] ) {
                from_y = i;
                to_y = i+1;
                break;
            }
        }
        return new Double[][] {{field[from_x][from_y],field[from_x][to_y]},{field[to_x][from_y],field[to_x][to_y]}};
    }

    public boolean loadField (FieldLoader field, URI uri) {
        if (!field.loadRessource(uri))
            return false;
        this.loadArray(field);
        return true;
    }

}
