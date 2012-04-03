package ch.zhaw.lakerouting.decisionScheme;

import java.util.ArrayList;

import ch.zhaw.lakerouting.datatypes.Coordinate;
import ch.zhaw.lakerouting.datatypes.Graph;
import ch.zhaw.lakerouting.datatypes.WindVector;
import ch.zhaw.lakerouting.navigation.duration.SailingDuration;


/**
 * This class computes the Decision-Matrix between two locations
 * 
 * @author Fevzi Yuekseldi, Mathias Habluetzel
 * 
 */
public class Decision {

	// contains the coordinates of the nodes as [theta, phi]
	private double[][][] loc;
	private int maxi;
	private int maxj;
	private int coord;
	private SailingDuration sd = new SailingDuration();

	/**
	 * Calculates the orthodromy between two locations L1(x1,y1) and L2(x2,y2)
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return The distance
	 */
	public double ortho(Coordinate crd1, Coordinate crd2) {

		/* Convert from degree to radian */
		double theta1 = crd1.getLongitudeInRadian();
		double phi1 = crd1.getLatitudeInRadian();
		double theta2 = crd2.getLongitudeInRadian();
		double phi2 = crd2.getLatitudeInRadian();

		/*
		 * Calculate the distance, since a nautical mile is defined by 1' at the
		 * aequator, we can use this formula for any near-spherical object.
		 * Later we would just use a given convertion factor to get meters or
		 * similar.
		 */
		double distance = 360
				* 60 /* we need minutes */
				* Math.acos(Math.cos(theta1 - theta2) * Math.cos(phi1)
						* Math.cos(phi2) + Math.sin(phi1) * Math.sin(phi2))
				/ (2 * Math.PI);
		// System.out.println("DISTANCE: "+distance);
		// System.out.println("DISTANCE: "+distance*1.852);
		return distance;
	}

	/**
	 * Enables the construction of a grid of nodes without the decision tree
	 * 
	 * @param theta1
	 *            - longitudes of the 1st location
	 * @param phi1
	 *            - latitudes of the 1st location
	 * @param theta2
	 *            - longitudes of the 2nd location
	 * @param phi2
	 *            - latitudes of the 2nd location
	 * @return A three dimensional array with the nodes
	 */
	public double[][][] graphe(Coordinate crd1, Coordinate crd2) {
		/* Convert from degree to radian */
		double theta1 = crd1.getLongitudeInDegree();
		double phi1 = crd1.getLatitudeInDegree();
		double theta2 = crd2.getLongitudeInDegree();
		double phi2 = crd2.getLatitudeInDegree();
		
		// local variables
		double p, e, c, s;
		int m, n;
		p = 0.3;
		m = 20;
		n = 10;

		// the euclidian distance
		e = Math.sqrt(Math.pow((theta2 - theta1), 2.0)
				+ Math.pow((phi2 - phi1), 2.0));
		// System.out.println("Euclidian Distance: "+e+" Grad, "+e*60*1.852+" km");
		// cosinus value
		c = (theta2 - theta1) / e;
		// sinus value
		s = (phi2 - phi1) / e;

		// rotation Matrix
		double[][] M = new double[2][2];
		M[0][0] = c;
		M[0][1] = -s;
		M[1][0] = s;
		M[1][1] = c;

		// the three dimensional tableMatrix
		double[][][] tableMatrix = new double[m + 1][2 * n + 1][2];
		// create the table
		for (int i = 0; i <= m; i++) {
			for (int j = -n; j <= n; j++) {
				// fill the table
				tableMatrix[i][j + n][0] = M[0][0] * (e * i / m) + M[0][1]
						* (p * e * j / (2 * n)) + theta1;
				tableMatrix[i][j + n][1] = M[1][0] * (e * i / m) + M[1][1]
						* (p * e * j / (2 * n)) + phi1;
				// output on the Console for verification
				// System.out.println("[i;j] "+i+";"+j+": "+tableMatrix[i][j+n][0]
				// +" , "+ tableMatrix[i][j+n][1]);
			}
		}
		return tableMatrix;
	}

	/**
	 * In this method starts the dynamical programming it defines the default
	 * 5-dimensional matrix and the start-point
	 * 
	 * @param start
	 *            - the starting node
	 * @return A 5-dimensional Array
	 */
	public ArrayList<ArrayList<Graph>> programmationDynamique(int start) {
		// 5-dimensional Matrix, defined with the generics
		ArrayList<ArrayList<Graph>> graphList = new ArrayList<ArrayList<Graph>>();
		Graph graph;

		// fill the graph with default values 0 and 1000000
		double[] init = { 0, 0 };
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
		double[] init2 = { 1, 1 };
		graphList.get(0).get(start).setPreviousNode(init2);
		graphList.get(0).get(start).setNode(init2);
		graphList.get(0).get(start).setTimeOfArrival(0);

		// call the method progrDyn to calculate the times
		for (int i = 1; i < getMaxi(); i++) {
			graphList = progrDyn(i, graphList);
		}
		// for (ArrayList<Graph> d : graphList) {
		// for(int i=0;i<d.size();i++)
		// {
		// System.out.println("PN: "+d.get(i).getPreviousNode()[0]+" "+d.get(i).getPreviousNode()[1]+" N: "+d.get(i).getNode()[0]+" "+d.get(i).getNode()[1]+" TA: "+d.get(i).getTimeOfArrival());
		// }
		// }
		return graphList;
	}

	/**
	 * Computes the arrival-times for the actual node
	 * 
	 * @param r
	 *            - the column-index of the graph
	 * @param graphList
	 *            - the 5-dimensional matrix
	 * @return The calculated 5-dimensional Array
	 */
	private ArrayList<ArrayList<Graph>> progrDyn(int r,
			ArrayList<ArrayList<Graph>> graphList) {
		int spread = 4;

		// table of arrival times
		double[] etabli;
		double min;
		double[][] position = new double[getMaxj()][2];
		double[] node;
		Coordinate crd1 = new Coordinate();
		Coordinate crd2 = new Coordinate();
		
		
		//Test, nachher durch interpolation ersetzen!!!!
		WindVector wv1 = new WindVector(10, 15);
		WindVector wv2 = new WindVector(10, 15);
		
		// for iterator for all nodes in the r-column
		for (int k = 0; k < getMaxj(); k++) {
			min = 1000000;

			etabli = new double[getMaxj()];
			// compares the node in the r-column with all the previous nodes
			for (int j = 0; j < getMaxj(); j++) {
				// loc[r-1][j] the previous node, loc[r][k] the current node
				crd1.setLongitudeInDegree(loc[r - 1][j][0]);
				crd1.setLatitudeInDegree(loc[r - 1][j][1]);
				crd2.setLongitudeInDegree(loc[r][k][0]);
				crd2.setLatitudeInDegree(loc[r][k][1]);
				etabli[j] = sd.getSailingDuration(crd1, crd2, wv1, wv2)
						+ graphList.get(r - 1).get(j).getTimeOfArrival();

				// finds the position of a minimum value and saves it into
				// position
				if (etabli[j] < min) {
					min = etabli[j];
					position[k][0] = j;
					position[k][1] = etabli[j];
				}
			}

			// assume k = 20
			// assume etabli[2] is minimum, therefore position[20][0] = 2
			// (20 - 2 > spread) -->> This node will NOT have a previous node!!

			// computes the spread and updates the matrix graphList
			if (Math.abs(k - (int) position[k][0]) <= spread) {
				node = new double[2];
				node[0] = r - 1;
				node[1] = position[k][0];
				graphList.get(r).get(k).setPreviousNode(node);
				node = new double[2];
				node[0] = r;
				node[1] = k;
				graphList.get(r).get(k).setNode(node);
				graphList.get(r).get(k).setTimeOfArrival(position[k][1]);
			}
		}
		return graphList;
	}

	/**
	 * Computes the coordinates of the sphere to a vector in 3d, which we yet
	 * don't use now
	 * 
	 * @param theta
	 * @param phi
	 */
	public double[] transformCoordToVector(Coordinate crd) {
		double theta = crd.getLongitudeInRadian();
		double phi = crd.getLatitudeInRadian();
		
		double[] sphere = new double[3];
		sphere[0] = Math.cos(theta)
				* Math.cos(phi);
		sphere[1] = Math.sin(theta)
				* Math.cos(phi);
		sphere[2] = Math.sin(phi);

		return sphere;
	}

	// Just some default methods to get the value of this variables
	// A common Java-usage
	public double[][][] getLoc() {
		return loc.clone();
	}

	public void setLoc(double[][][] loc) {
		this.loc = loc.clone();
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
}
