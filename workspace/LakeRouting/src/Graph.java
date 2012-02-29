
public class Graph {
	
	private double[] previousNode;
	private double[] node;
	private double timeOfArrival;

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
}
