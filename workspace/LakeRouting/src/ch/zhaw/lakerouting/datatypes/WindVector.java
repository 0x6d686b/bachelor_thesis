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

import org.apache.log4j.Logger;

import ch.zhaw.lakerouting.decisionScheme.Decision;

/**
 * Primitive wind vector with helpful calculations
 *
 * <p>A wind vector is nothing else than a composite of two normal vectors with
 * getters for the most used calculations like wind speed and angle.</p>
 *
 * <h3 style="color:red;">Warning!</h3>
 * <p>However, please note that we obviously interchanged the naming of the
 * wind components. In meteorology the u-component is the wind blowing <b>to</b>
 * the east (unlike us, we use north), and the v-component is the wind blowing
 * <b>to</b> the north (unlike us, we use east). This has mathematical
 * background.</p>
 *
 * <p>Also note that we unlike in meteorology do <b>not</b> calculate the wind
 * direction by the term of where the is coming <b>from</b> but where the vectors
 * are pointing at!</p>
 *
 * @author Mathias Hablützel
 * @since 1.0
 * @version 1.0-stable
 */
public class WindVector {
    /** Just a limiter for preventing completely unreasonable values */
    private static final int MAXWIND = 180;
    /** u is the unit vector with value (1,0) pointing to the north */
    private double u;
    /** v is the unit vector with value (0,1) pointing to the east */
    private double v;

    private Logger logger = Logger.getLogger(this.getClass());
    
    /**
     * Basic input validator which could be extended at any later moment.
     * @param input input of unit value
     * @throws IllegalArgumentException
     */
    private void validateInput (double input) {
        if (Math.abs(input) > MAXWIND){
        	logger.error("Input value too high: max " + MAXWIND + "kts.");
            throw new IllegalArgumentException("Input value too high: max " + MAXWIND + "kts.");
        }
    }

    /**
     * Constructor for a WindVector
     * @param i north pointing part of wind (can be negative)
     * @param j east pointing part of wind (can be negative)
     * @throws IllegalArgumentException
     */
    public WindVector (double i, double j) {
        setU(i);
        setV(j);
    }

    /**
     * Gets the (1,0) part of the WindVector, the north-value-part
     * @return north pointing portion of wind speed
     */
    public final double getU() {
        return u;
    }

    /**
     * Sets the (1,0) part of the WindVector, the north-value-part
     * @param input north pointing portion of wind speed
     */
    public final void setU(double input) {
        validateInput(input);
        this.u = input;
    }

    /**
     * Gets the (0,1) part of the WindVector, the east-value-part
     * @return east pointing portion of wind speed
     */
    public final double getV() {
        return v;
    }

    /**
     * Sets the (0,1) part of the WindVector, the east-value-part
     * @param input east pointing portion of wind speed
     */
    public final void setV(double input) {
        validateInput(input);
        this.v = input;
    }

    /**
     * Calculates the actual wind speed of the WindVector
     * @return wind speed
     */
    public final double getWindspeed() {
        return Math.sqrt(Math.pow(getU(),2) + Math.pow(getV(),2));
    }

    /**
     * Calculates the wind attack angle where 0° is north, 90° east.
     *
     * <p>For our needs we calculate the wind angle as the angle of the wind
     * vector pointing at, starting from 0° at North, 90° for East, 180° for
     * South and 270° West respective 0 for north, π/2 for east, π for south
     * and 3π/2 for west.</p>
     *
     * <h4 style="color:red;">Warning</h4>
     * <p>Also note that we unlike in meteorology do <b>not</b> calculate the wind
     * direction by the term of where the is coming <b>from</b> but where the vectors
     * are pointing at!</p>
     *
     * @return radian value of angle
     */
    public final double getAngle() {
        /**
         * Assume: (1,0) is the vector pointing to the North
         * Assume: (u,v) is our wind vector
         * dot-product is defined as Sum(i=0 -> n) {A_i * B_i}
         * We simplify: 1*u + 0*v = u
         */
        return Math.acos( ( getU() ) / getWindspeed() );
    }

    /**
     * Human readable output of WindVector of type "u: 0.3144, v: 1.829, angle: 2.3331, velocity 4,5kts"
     * @return human readable string
     */
    public final String toString() {
        String s = "";

        s += "u: " + getU() + ", ";
        s += "v: " + getV() + ", ";
        s += "angle: " + getAngle() + ", ";
        s += "velocity: " + getWindspeed() + " kts";
        
        return s;
    }

    /**
     * Output possibility for Mathematica used while developing. Do not not rely
     * on finding this method in later releases.
     *
     * @return string containing the vector for Mathematica
     */
    public final String mathematicaVectorOutput() {
        String s = "";
        return s += "{" + getU() + "," + getV() + "}";
    }
}
