package task1and2;

/**
 * This class represents the elements of the Decision-Matrix with the structure:
 * ({{previous node} , {node}} ; time of arrival)
 * 
 * @author Fevzi Y�kseldi, Mathias Habl�tzel
 * 
 */
public class Graph implements Comparable<Graph> {

	private double[] previousNode;
	private double[] node;
	private double timeOfArrival;

	/**
	 * The constructor of the class Graph
	 * 
	 * @param previousNode
	 * @param node
	 * @param timeOfArrival
	 */
	public Graph(double[] previousNode, double[] node, double timeOfArrival) {
		this.previousNode = previousNode;
		this.node = node;
		this.timeOfArrival = timeOfArrival;
	}

	public Graph() {
	}

	public double[] getPreviousNode() {
		return previousNode;
	}

	public void setPreviousNode(double[] previousNode) {
		this.previousNode = previousNode;
	}

	public double[] getNode() {
		return node;
	}

	public void setNode(double[] node) {
		this.node = node;
	}

	public double getTimeOfArrival() {
		return timeOfArrival;
	}

	public void setTimeOfArrival(double timeOfArrival) {
		this.timeOfArrival = timeOfArrival;
	}

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

	// public void compare(double e1, double e2)
	// {
	//
	// }
}
