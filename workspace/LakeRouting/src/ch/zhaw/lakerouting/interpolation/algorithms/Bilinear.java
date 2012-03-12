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

import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: mhk
 * Date: 10.03.12
 * Time: 11:21
 */
public class Bilinear implements InterpolationAlgorithm {
    @Override @Test
    public double interpolate(double x, double y, Double[][] matrix) {
        if (!(x >= 0 && x <= 1 && y >= 0 && y <= 1))
            throw new IllegalArgumentException("Params not in required range, got x: " + x + " and y: " + y);

        if (!(matrix.length == 2 && matrix[0].length == 2 && matrix[1].length == 2))
            throw new IllegalArgumentException("Passed matrix array has not the required size.");

        return x * (matrix[1][0] - matrix[0][0])
                + x * y * (matrix[0][0] - matrix[0][1] + matrix[1][1] - matrix[1][0])
                + y * (matrix[0][1] - matrix[0][0]) + matrix[0][0];
    }
}
