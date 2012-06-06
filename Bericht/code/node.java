public class Node implements Comparable<Node> {
    /** Time it takes to travel to the node in minutes */
    private double timeOfArrival;
    /** The ancestor or previous node */
    private Node previous = null;
    /** The coordinate of the node */
    private Coordinate crd;
    /** The wind vector at the node's position */
    private WindVector windVector;

    .
    .
    .
    // Some specific Constructors
    .
    .
    .
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

    // Some getter & setter of the variables
    .
    .
    .
}
