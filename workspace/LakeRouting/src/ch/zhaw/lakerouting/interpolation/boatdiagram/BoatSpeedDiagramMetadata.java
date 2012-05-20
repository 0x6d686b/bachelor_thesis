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

package ch.zhaw.lakerouting.interpolation.boatdiagram;

public final class BoatSpeedDiagramMetadata {
    private double minimalAttackAngle;
    private double maximalAttackAngle;
    private double minimalWindspeed;
    private double maximalWindspeed;

    public double getMinimalAttackAngle() {
        return minimalAttackAngle;
    }

    public void setMinimalAttackAngle(double input) {
        this.minimalAttackAngle = input;
    }

    public double getMaximalAttackAngle() {
        return maximalAttackAngle;
    }

    public void setMaximalAttackAngle(double input) {
        this.maximalAttackAngle = input;
    }

    public double getMinimalWindspeed() {
        return minimalWindspeed;
    }

    public void setMinimalWindspeed(double maximalWindspeed) {
        this.minimalWindspeed = maximalWindspeed;
    }

    public double getMaximalWindspeed() {
        return maximalWindspeed;
    }

    public void setMaximalWindspeed(double input) {
        this.maximalWindspeed = input;
    }
}