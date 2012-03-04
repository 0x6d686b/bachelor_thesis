import java.util.ArrayList;

/**
 * This class computes the Decision-Matrix between two locations
 * @author Fevzi Yükseldi, Mathias Hablützel
 *
 */
public class Decision {
	
	// contains the coordinates of the nodes as [theta, phi]
	private double[][][] loc;
	private int maxi;
	private int maxj;
	private int coord;
	
	/**
	 * Calculates the orthodromy between two locations L1(x1,y1) and L2(x2,y2)
	 * @param x1 
	 * @param y1 
	 * @param x2 
	 * @param y2 
	 * @return The distance
	 */
	public double ortho(double x1,double y1,double x2,double y2){
		double theta1=Math.PI*x1/180;
		double phi1=Math.PI*y1/180;
		double theta2=Math.PI*x2/180;
		double phi2=Math.PI*y2/180;
		
		double distance = 360*60*Math.acos(Math.cos(theta1-theta2)*Math.cos(phi1)*Math.cos(phi2)+Math.sin(phi1)*Math.sin(phi2))/(2*Math.PI);
//		System.out.println("DISTANCE: "+distance);
		return distance;
	}
	
	/**
	 * Enables the construction of a grid of nodes without the decision tree
	 * @param theta1 - longitudes of the 1st location
	 * @param phi1   - latitudes of the 1st location
	 * @param theta2 - longitudes of the 2nd location
	 * @param phi2   - latitudes of the 2nd location
	 * @return A three dimensional array with the nodes
	 */
	public double[][][] graphe(double theta1, double phi1, double theta2, double phi2){
		// local variables
		double p,e,c,s;
		int m,n;
		p=0.3; 
		m = 20;
		n=10;
		
		// the euclidian distance
		e = Math.sqrt(Math.pow((theta2-theta1), 2.0) + Math.pow((phi2-phi1), 2.0));
		// cosinus value
		c = (theta2-theta1)/e;
		// sinus value
		s = (phi2-phi1)/e;
		
		//rotation Matrix
		double[][] M = new double[2][2];
		M[0][0] = c;
		M[0][1] = -s;
		M[1][0] = s;
		M[1][1] = c;
		
		// the three dimensional tableMatrix
		double[][][] tableMatrix=new double[m+1][2*n+1][2];
		// create the table
		for(int i= 0;i<=m;i++)
		{
			for(int j=-n;j<=n;j++)
			{
				// fill the table
				tableMatrix[i][j+n][0] = M[0][0]*(e*i/m) + M[0][1]*(p*e*j/m) + theta1;
				tableMatrix[i][j+n][1] = M[1][0]*(e*i/m) + M[1][1]*(p*e*j/m) + phi1;
				// output on the Console for verification
//				System.out.println("[i;j] "+i+";"+j+": "+tableMatrix[i][j+n][0] +" , "+ tableMatrix[i][j+n][1]);
			}
		}
		return tableMatrix;
	}
	
	/**
	 * In this method starts the dynamical programming
	 * it defines the default 5-dimensional matrix and the start-point
	 * @param start - the starting node
	 * @return A 5-dimensional Array
	 */
	public ArrayList<ArrayList<Graph>> programmationDynamique(int start){
		//5-dimensional Matrix, defined with the generics
		ArrayList <ArrayList<Graph>> graphList = new ArrayList<ArrayList<Graph>>();
		Graph graph;
		
		// fill the graph with default values 0 and 1000000
		double[] init = new double[2];
		init[0]=0;
		init[1]=0;
		for (int i = 0; i < getMaxi(); i++) {
			graphList.add(new ArrayList<Graph>());
			for (int j = 0; j < getMaxj(); j++) {
				graph = new Graph();
				graph.setPreviousNode(init);
				graph.setNode(init);
				graph.setTimeOfArrival(1000000);
				graphList.get(i).add(graph);
			}
		}
		
		// fill at point (0,start) the node with values 1 and 0
		double[] init2 = new double[2];
		init2[0]=1;
		init2[1]=1;
		graphList.get(0).get(start).setPreviousNode(init2);
		graphList.get(0).get(start).setNode(init2);
		graphList.get(0).get(start).setTimeOfArrival(0);
		
		// call the method progrDyn to calculate the times
		for (int i = 1; i < getMaxi(); i++) {
			graphList = progrDyn(i, graphList);
		}
//		for (ArrayList<Graph> d : graphList) {
//			for(int i=0;i<d.size();i++)
//			{
//				System.out.println("PN: "+d.get(i).getPreviousNode()[0]+" "+d.get(i).getPreviousNode()[1]+" N: "+d.get(i).getNode()[0]+" "+d.get(i).getNode()[1]+" TA: "+d.get(i).getTimeOfArrival());
//			}
//		}
		return graphList;
	}	
	
	/**
	 * Computes the arrival-times for the actual node
	 * @param r - the column-index of the graph
	 * @param graphList - the 5-dimensional matrix 
	 * @return The calculated 5-dimensional Array
	 */
	private ArrayList<ArrayList<Graph>> progrDyn(int r, ArrayList<ArrayList<Graph>> graphList){
		int spread = 4;
		
		// table of arrival times
		double[] etabli;
		double min;
		double[][] position = new double[getMaxj()][2];
		double[] node;
	    // for iterator for all nodes in the r-column
		for (int k = 0; k < getMaxj(); k++) {
			min = 1000000;
			
			etabli = new double[getMaxj()];
			// compares the node in the r-column with all the previous nodes
			for (int j = 0; j < getMaxj(); j++) {
				// loc[r-1][j] the previous node, loc[r][k] the current node
				etabli[j] = graphList.get(r-1).get(j).getTimeOfArrival() + ortho(loc[r-1][j][0], loc[r-1][j][1], loc[r][k][0], loc[r][k][1]);
				
				// finds the position of a minimum value and saves it into position
				if(etabli[j]<min)
				{
					min = etabli[j];
					position[k][0] = j;
					position[k][1] = etabli[j];
				}
			}
			
			// computes the spread and updates the matrix graphList
			if(Math.abs(k-(int)position[k][0])<=spread)
			{
				node = new double[2];
				node[0]=r-1;
				node[1]=position[k][0];
				graphList.get(r).get(k).setPreviousNode(node);
				node = new double[2];
				node[0]=r;
				node[1]=k;
				graphList.get(r).get(k).setNode(node);
				graphList.get(r).get(k).setTimeOfArrival(position[k][1]);
			}
		}
		return graphList;
	}
	
	/**
	 * Computes the coordinates of the sphere, which we yet don't use now 
	 * @param theta
	 * @param phi
	 */
	public void setX(double theta, double phi){
		double[] sphere = new double[3];
		sphere[0] = Math.cos(Math.PI*theta/180)*Math.cos(Math.PI*phi/180);
		sphere[1] = Math.sin(Math.PI*theta/180)*Math.cos(Math.PI*phi/180);
		sphere[2] = Math.sin(Math.PI*phi/180);
	}
	
	// Just some default methods to get the value of this variables
	// A common Java-usage
	public double[][][] getLoc() {
		return loc;
	}

	public void setLoc(double[][][] loc) {
		this.loc = loc;
	}
	
	public int getMaxi() {
		return maxi;
	}

	public void setMaxi(int maxi) {
		this.maxi = maxi;
	}

	public int getMaxj() {
		return maxj;
	}

	public void setMaxj(int maxj) {
		this.maxj = maxj;
	}

	public int getCoord() {
		return coord;
	}

	public void setCoord(int coord) {
		this.coord = coord;
	}
	
	/**
	 * The main method, which calls the methods
	 * @param args
	 */
	public static void main(String[] args) {
		Decision de = new Decision();
		de.setLoc(de.graphe(8.05, 47.3, 226.88, -47.22));
		de.setMaxi(de.getLoc().length);
		de.setMaxj(de.getLoc()[0].length);
		de.setCoord(de.getLoc()[0][0].length);
//		System.out.println("Maxi: "+de.getMaxi()+" Maxj: "+de.getMaxj()+" Coord:"+de.getCoord());
		
		ArrayList<ArrayList<Graph>> test = de.programmationDynamique(15);
		
	}
}