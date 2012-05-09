package ch.zhaw.lakerouting.datatypes;

/**
 * This class represents the elements of the Decision-Matrix with the structure:
 * ({{previous node} , {node}} ; time of arrival)
 * 
 * @author Fevzi Y�kseldi, Mathias Habl�tzel
 * 
 */
public class Graph implements Comparable<Graph> {

	private int[] previousNode;
	private int[] node;
	private double timeOfArrival;

	/**
	 * The constructor of the class Graph
	 * 
	 * @param previousNode
	 * @param node
	 * @param timeOfArrival
	 */
	public Graph(int[] previousNode, int[] node, double timeOfArrival) {
		this.previousNode = previousNode;
		this.node = node;
		this.timeOfArrival = timeOfArrival;
	}

	public Graph() {
	}

	public int[] getPreviousNode() {
		return previousNode;
	}

	public void setPreviousNode(int[] previousNode) {
		this.previousNode = previousNode;
	}

	public int[] getNode() {
		return node;
	}

	public void setNode(int[] node) {
		this.node = node;
	}

	public double getTimeOfArrival() {
		return timeOfArrival;
	}

	public void setTimeOfArrival(double timeOfArrival) {
		this.timeOfArrival = timeOfArrival;
	}

	/**
	 * This method allows us to use the methods of the Collections-class,
	 * like getMin() or getMax etc.
	 * It compares the TimeOfArrival value of two Graphs
	 */
	@Override
	public int compareTo(Graph o) {

		if (getTimeOfArrival() < o.getTimeOfArrival()) {
			return -1;
		} else if (getTimeOfArrival() == o.getTimeOfArrival()) {
			return 0;
		}
		// TODO Auto-generated method stub
		return 1;
	}
}
