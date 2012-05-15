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

public class Node implements Comparable<Node> {
    private double timeOfArrival;
    private Node previous = null;
    private Coordinate crd;
    private WindVector windVector;
    
    public Node() {
    	timeOfArrival = 0;
        windVector = new WindVector(0, 0);
    }
    
    public Node(double timeOfArrival) {
        this.timeOfArrival = timeOfArrival;
        windVector = new WindVector(0, 0);
    }
    
    public Node(double timeOfArrival, WindVector wv) {
        this.timeOfArrival = timeOfArrival;
        windVector = wv;
    }
    


    @Override
    public int compareTo(Node o) {
        if (getTimeOfArrival() < o.getTimeOfArrival()) {
            return -1;
        } else if (getTimeOfArrival() == o.getTimeOfArrival()) {
            return 0;
        }
        return 1;
    }

    /**
     * We just let the first node point to itself
     * so when the node points to itself we cannot under
     * any circumstances have a null pointer exception
     * and it's very easy to check if we are at the first node
     *
     * @see #isStartNode()
     */
    public void thisAsStartNode() {
        setPrevious(this);
        setTimeOfArrival(0);
    }

    public boolean isStartNode() {
        if (this == this.previous())
            return true;
        return false;
    }

    public double getTimeOfArrival() {
        return timeOfArrival;
    }

    public void setTimeOfArrival(double timeOfArrival) {
        this.timeOfArrival = timeOfArrival;
    }

    public Node previous() {
        return previous;
    }

    public void setPrevious(Node previous) {
        this.previous = previous;
    }

	public Coordinate getCrd() {
		return crd;
	}

	public void setCrd(Coordinate crd) {
		this.crd = crd;
	}
	
	public final String toString() {
        return "Node: TOA: "+timeOfArrival+", Longitude: " + crd.getLongitudeInDegree() + "°, Latitude: " + crd.getLatitudeInDegree();
    }
	public WindVector getWindVector() {
		return windVector;
	}
	public void setWindVector(WindVector windVector) {
		this.windVector = windVector;
	}
}
