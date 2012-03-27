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

package ch.zhaw.lakerouting.datatypes;

/**
 * Created by IntelliJ IDEA.
 * User: mhk
 * Date: 20.03.12
 * Time: 11:28
 */
public class WindVector {
    private static final int MAXWIND = 180;
    private double u;
    private double v;

    private void validateInput (double input) {
        if (Math.abs(input) > MAXWIND)
            throw new IllegalArgumentException("Input value too high: max " + MAXWIND + "kts.");
    }

    public WindVector (double i, double j) {
        setU(i);
        setV(j);
    }
    
    public final double getU() {
        return u;
    }

    public final void setU(double input) {
        validateInput(input);
        this.u = input;
    }

    public final double getV() {
        return v;
    }

    public final void setV(double input) {
        validateInput(input);
        this.v = input;
    }

    public final double getWindspeed() {
        return Math.sqrt(Math.pow(getU(),2) + Math.pow(getV(),2));
    }

    public final double getAngle() {
        /**
         * Assume: (1,0) is the vector pointing to the North
         * Assume: (u,v) is our wind vector
         * dot-product is defined as Sum(i=0 -> n) {A_i * B_i}
         * We simplify: 1*u + 0*v = u
         */
        return Math.acos( ( getU() ) / getWindspeed() );
    }
}
