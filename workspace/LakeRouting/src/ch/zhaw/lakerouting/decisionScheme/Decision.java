package ch.zhaw.lakerouting.decisionScheme;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import ch.zhaw.lakerouting.datatypes.Coordinate;
import ch.zhaw.lakerouting.datatypes.Graph;
import ch.zhaw.lakerouting.datatypes.WindVector;
import ch.zhaw.lakerouting.interpolation.algorithms.Bilinear;
import ch.zhaw.lakerouting.interpolation.algorithms.InterpolationAlgorithm;
import ch.zhaw.lakerouting.interpolation.windfield.WindfieldContainer;
import ch.zhaw.lakerouting.interpolation.windfield.loader.SpaceWindFieldLoader;
import ch.zhaw.lakerouting.navigation.duration.SailingDuration;

/**
 * This class computes the Decision-Matrix between two locations
 * 
 * @author Fevzi Yuekseldi, Mathias Habluetzel
 * 
 */
public class Decision {

	// contains the coordinates of the nodes as [theta, phi]
	private List<List<Coordinate>> loc;
	private List<List<WindVector>> wv;
	private List<List<WindVector>> wvAdjusted;
	private int maxi;
	private int maxj;
	private int coord;
	private SailingDuration sd = new SailingDuration();
	private WindfieldContainer windFieldContainer;
	private List<List<Graph>> graphList;
	private int spread;

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
	 * Enables the construction of a grid of nodes with coordinates, but without
	 * the decision tree
	 * 
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
	public void graphe(Coordinate crd1, Coordinate crd2, int m, int n) {
		/* Convert from degree to radian */
		List<List<Coordinate>> coordList = new ArrayList<List<Coordinate>>();
		List<Coordinate> coordRow = new ArrayList<Coordinate>();
		Coordinate crd = new Coordinate();

		double theta1 = crd1.getLongitudeInDegree();
		double phi1 = crd1.getLatitudeInDegree();
		double theta2 = crd2.getLongitudeInDegree();
		double phi2 = crd2.getLatitudeInDegree();

		// local variables
		double p, e, c, s;
		p = 0.3;
		

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

		// create the table
		for (int i = 0; i <= m; i++) {
			for (int j = -n; j <= n; j++) {
				// fill the table
				crd.setLongitudeInDegree(M[0][0] * (e * i / m) + M[0][1]
						* (p * e * j / (2 * n)) + theta1);
				crd.setLatitudeInDegree(M[1][0] * (e * i / m) + M[1][1]
						* (p * e * j / (2 * n)) + phi1);
				coordRow.add(crd);
				crd = new Coordinate();
			}
			coordList.add(coordRow);
			coordRow = new ArrayList<Coordinate>();
		}

		setParameters(coordList);
	}

	private void setParameters(List<List<Coordinate>> coordList) {
		setLoc(coordList);
		setMaxi(coordList.size());
		setMaxj(coordList.get(0).size());
		setCoord(2);
	}

	/**
	 * This method handles the dynamical programming. It fills the graph at
	 * first with default-values, after that it calculates the shortest
	 * distance.
	 * 
	 * @param start
	 *            - the starting node
	 * @return A 2-dimensional arrayList with the decision tree
	 */
	public void programmationDynamique(int start, int spread) {
		
		this.spread = spread;
		// 5-dimensional Matrix, defined with the generics
		graphList = new ArrayList<List<Graph>>();
		
		// Fill the Graph with default-values
		initializeGraph();

		getInterpolatedWindfield();
		
		// Clone the ArrayList to make possible, that we can change the object
		// wvAdjusted, and not the object of the windfieldContainer
		// TODO Replace 4 with getWindFieldIndex()
		wvAdjusted = new ArrayList<List<WindVector>>(windFieldContainer.get(4).getField());

		// fill at point (0,start) the node with values 1 and 0
		setStartPoint(start);

		// call the method progrDyn to calculate the times
		for (int i = 1; i < getMaxi(); i++) {
			progrDyn(i);
		}
	}

	/**
	 * Fills the graph at the beginning with default values 0 for Nodes 1000000
	 * for TimeOfArrival
	 */
	private void initializeGraph() {
		Graph graph;
		ArrayList<Graph> graphRow;
		double[] init = { 0, 0 };
		for (int i = 0; i < getMaxi(); i++) {
			graphRow = new ArrayList<Graph>();
			for (int j = 0; j < getMaxj(); j++) {
				graph = new Graph();
				graph.setPreviousNode(init);
				graph.setNode(init);
				graph.setTimeOfArrival(1000000);
				graphRow.add(graph);
			}
			graphList.add(graphRow);
		}
	}

	/**
	 * Sets the start-node with the parameter
	 * 
	 * @param start
	 *            - row-number
	 */
	private void setStartPoint(int start) {
		double[] init2 = { 1, 1 };
		graphList.get(0).get(start).setPreviousNode(init2);
		graphList.get(0).get(start).setNode(init2);
		graphList.get(0).get(start).setTimeOfArrival(0);
	}

	/**
	 * Gets the windFields from the File listed and returns the interpolated
	 * windVectors for the nodes over all Fields
	 */
	private void getInterpolatedWindfield() {
		SpaceWindFieldLoader loader = new SpaceWindFieldLoader();
		InterpolationAlgorithm bil = new Bilinear();
		windFieldContainer = new WindfieldContainer();
		try {
			windFieldContainer
					.bulkLoadWindfield(
							new URI(
									"file",
									"C:/Users/fevzi/Desktop/ZHAW/BA(furu)/git/lakerouting/workspace/LakeRouting/11072915_905.dat",
									""), loader);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		windFieldContainer = windFieldContainer.bulkInterpolateOnDecisionNet(
				loc, bil);
		System.out.println("WF " + windFieldContainer.getDelta().toString());

		// setWv(foo.get(0).interpolateOnDecisionNet(loc, bil));
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
	private void progrDyn(int r) {
//		 int wf = getWindFieldIndex();
//		 System.out.println("WF "+wf);
		// wf = 4, 12, 13, 14
		// TODO Replace 4 with getWindFieldIndex()
		int wf = 4;

		// table of arrival times
		double[] etabli;
		// Integer[] idx;
		double min;
		double[][] position = new double[getMaxj()][2];
		double[] node;

		// for iterator for all nodes in the r-column
		for (int k = 0; k < getMaxj(); k++) {
			min = 1000000;

			etabli = new double[getMaxj()];
			// idx = new Integer[getMaxj()];
			// compares the node in the r-column with all the previous nodes
			// loc[r-1][j] the previous node, loc[r][k] the current node
			for (int j = 0; j < getMaxj(); j++) {
				// NEW
				
					etabli[j] = getEtabli(r, j, k, wf, graphList);
					// idx[j]=j;
					// finds the position of a minimum value and saves it into
					// position
					if (etabli[j] < min) {
						if (Math.abs(k - j) <= spread) {
							min = etabli[j];
							position[k][0] = j;
							position[k][1] = etabli[j];
						}else{
							position[k][0] = j;
							position[k][1] = 1000000;
						}
					}
			}
			/*
			 * Sort the minimum-values So we have the other shortest values if
			 * it doesnt match with spread
			 */
			// final double[] etabli2 = etabli;
			// Arrays.sort(idx, new Comparator<Integer>() {
			// @Override public int compare(Integer o1, Integer o2) {
			// return Double.compare(etabli2[o1], etabli2[o2]);
			// }
			// });
			
			// computes the spread and updates the matrix graphList
			if (Math.abs(k - (int) position[k][0]) <= spread) {
				if (position[k][1] >= 30d) {
					int w_calculated = (int) position[k][1] / 30;
					System.out.print("Position1: " + position[k][1]);
					int wf_neu = wf + w_calculated;
					if(wf_neu>24) wf_neu = 24;
					position[k][1] = getEtabli(r, (int) position[k][0], k, wf_neu, graphList);
					System.out.println(" Position2: " + position[k][1]);
				}
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
		setWv(windFieldContainer.get(wf).getField());
	}

	private int getWindFieldIndex() {

		DateTime dt = new DateTime();
		int dayOfMonthWF = windFieldContainer.get(0).getMetadata().getDate()
				.dayOfMonth().get();
		int dayOfMonth = dt.getDayOfMonth();
		
		// TODO To check, if the day of the windfield is the same like the
		// current day

		int hourOfDayWF = windFieldContainer.get(0).getMetadata().getDate()
				.getHourOfDay();
		int hourOfDay = dt.getHourOfDay();
		int minutesOfDay = dt.getMinuteOfHour();

		hourOfDay += (minutesOfDay < 30) ? 0 : 1;

		int difference = hourOfDay - hourOfDayWF;
		if (difference < 0) {
			difference = 24 + difference;
		}
		return difference;
	}

	private double getEtabli(int r, int j, int k, int wf,
			List<List<Graph>> graphList) {
		
		setWv(windFieldContainer.get(wf).getField());
		double distance = ortho(loc.get(r - 1).get(j), loc.get(r).get(k));
		WindVector v1 = windFieldContainer.get(wf).getField().get(r - 1).get(j);
		WindVector v2 = windFieldContainer.get(wf).getField().get(r).get(k);
		double sailingDuration = sd.getSailingDuration(loc.get(r - 1).get(j), loc.get(r).get(k),
				v1, v2, distance);
		sailingDuration = sailingDuration + graphList
				.get(r - 1).get(j).getTimeOfArrival();
		wvAdjusted.get(r).set(k, v2);
		return sailingDuration;
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
		sphere[0] = Math.cos(theta) * Math.cos(phi);
		sphere[1] = Math.sin(theta) * Math.cos(phi);
		sphere[2] = Math.sin(phi);

		return sphere;
	}

	public List<List<Graph>> getGraphList() {
		return graphList;
	}

	// Just some default methods to get the value of this variables
	// A common Java-usage
	public List<List<Coordinate>> getLoc() {
		return loc;
	}

	public void setLoc(List<List<Coordinate>> loc) {
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

	public List<List<WindVector>> getWv() {
		return wv;
	}

	public void setWv(List<List<WindVector>> wv) {
		this.wv = wv;
	}

	public List<List<WindVector>> getWvAdjusted() {
		return wvAdjusted;
	}

	public void setWvAdjusted(List<List<WindVector>> wvAdjusted) {
		this.wvAdjusted = wvAdjusted;
	}
}
