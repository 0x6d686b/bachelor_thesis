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
 * Node is used for the decision graph, it contains the data structure for the
 * decision logic.
 *
 * <p>Basically every node knows four values:
 * <ul>
 * <li>the previous or ancestor node,</li>
 * <li>its coordinate,</li>
 * <li>the wind vector at its position,</li>
 * <li>and the time it requires to travel to this node.</li>
 * </ul>
 * From every point we can travel backwards to the starting point and will
 * not encounter any null pointer. This makes the node data structure very
 * solid and robust against implementation faults. Also it thus guarantees
 * to always terminate at the starting point.
 * </p>
 *
 * <h3 style="color:red;">Warning!</h3>
 * <p>However note that the constructor <b>does not</b> take care of preventing
 * a null pointer. You <b>must</b> set a ancestor or previous by yourself or
 * if you are at the starting point use {@link #thisAsStartNode()} !</p>
 *
 * @author Mathias Hablützel
 * @version 1.0-stable
 * @since 1.0
 */

public class Node implements Comparable<Node> {
    /** Time it takes to travel to the node in minutes */
    private double timeOfArrival;
    /** The ancestor or previous node */
    private Node previous = null;
    /** The coordinate of the node */
    private Coordinate crd;
    /** The wind vector at the node's position */
    private WindVector windVector;

    /**
     * Standard empty constructor
     *
     * <p>Sets
     * <ul>
     * <li>{@link #timeOfArrival}</li>
     * <li>{@link #windVector}</li>
     * <li>{@link #crd}</li>
     * </ul>
     * to zero values.</p>
     */
    public Node() {
    	timeOfArrival = 0;
        windVector = new WindVector(0, 0);
        crd = new Coordinate();
    }

    /**
     * Constructor with known {@link #timeOfArrival}
     * @param timeOfArrival time needed to reach the node
     */
    public Node(double timeOfArrival) {
        this.timeOfArrival = timeOfArrival;
        windVector = new WindVector(0, 0);
        crd = new Coordinate();
    }

    /**
     * Constructor with known {@link #timeOfArrival} and {@link #windVector}
     * @param timeOfArrival time needed to reach the node
     * @param wv wind vector of the node
     */
    public Node(double timeOfArrival, WindVector wv) {
        this.timeOfArrival = timeOfArrival;
        windVector = wv;
        crd = new Coordinate();
    }

    /**
     * Constructor with known {@link #timeOfArrival}, {@link #windVector} and {@link #crd}
     * @param timeOfArrival time needed to reach the node
     * @param wv wind vector of the node
     * @param crd coordinate of the node
     */
    public Node(double timeOfArrival, WindVector wv, Coordinate crd) {
        this.timeOfArrival = timeOfArrival;
        windVector = wv;
        this.crd = crd;
    }

    /**
     * <p>Comparator overloading to be able to compare two Nodes according to its
     * time-to-travel to the according Node.</p>
     *
     * @param o the Object to be compared
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to,
     * or greater than the specified object.
     */
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
     * Sets the Node as a starting Node.
     *
     * We just let the first Node point to itself
     * so when the Node points to itself we cannot under
     * any circumstances have a null pointer exception
     * and it's very easy to check if we are at the first node
     *
     * @see #isStartNode()
     */
    public void thisAsStartNode() {
        setPrevious(this);
        setTimeOfArrival(0);
    }

    /**
     * Returns a boolean if the Node is the starting node.
     *
     * It will check if the ancestor has been set to point to itself meaning
     * the starting point has been reached.
     *
     * @return a boolean, true if it is the starting node, otherwise false
     */
    public boolean isStartNode() {
        if (this == this.previous())
            return true;
        return false;
    }

    /**
     * Returns the time needed to reach the Node.
     * @return returns the travel time in minutes
     */
    public double getTimeOfArrival() {
        return timeOfArrival;
    }

    /**
     * Sets the time needed to reach the Node.
     * @param timeOfArrival travel time in minutes
     */
    public void setTimeOfArrival(double timeOfArrival) {
        this.timeOfArrival = timeOfArrival;
    }

    /**
     * Returns the ancestor Node.
     *
     * <p>
     *     Note: The constructor does <b>not</b> enforce you to set an ancestor, however this should
     *     not happen unless you take care of potential null pointers.
     * </p>
     * @return the previous Node
     */
    public Node previous() {
        return previous;
    }

    /**
     * Sets the ancestor Node.
     * @param previous the ancestor Node
     */
    public void setPrevious(Node previous) {
        this.previous = previous;
    }

    /**
     * Returns the Coordinate of the Node
     * @return Coordinate of the Node
     */
	public Coordinate getCrd() {
		return crd;
	}

    /**
     * Sets the Coordinate of the Node
     * @param crd Coordinate of the Node
     */
	public void setCrd(Coordinate crd) {
		this.crd = crd;
	}

    /**
     * Returns a human readable String of the Node containing {@link #timeOfArrival} and the Coordinate in
     * the form of "Node: TOA: 34.122, Longitude: 23.112°, Latitude 133.288"
     * @return a String with human readable information about the Node
     */
	public final String toString() {
        return "Node: TOA: "+timeOfArrival+", Longitude: " + crd.getLongitudeInDegree() + "°, Latitude: " + crd.getLatitudeInDegree();
    }

    /**
     * Returns the WindVector of the Node.
     * @return a WindVector
     */
	public WindVector getWindVector() {
		return windVector;
	}

    /**
     * Sets the WindVector of the Node.
     * @param windVector the WindVector
     */
	public void setWindVector(WindVector windVector) {
		this.windVector = windVector;
	}
}
