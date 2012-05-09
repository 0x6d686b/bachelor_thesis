package ch.zhaw.lakerouting.decisionScheme;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import ch.zhaw.lakerouting.datatypes.Node;
import org.joda.time.DateTime;

import ch.zhaw.lakerouting.datatypes.Coordinate;
import ch.zhaw.lakerouting.datatypes.WindVector;
import ch.zhaw.lakerouting.interpolation.algorithms.Bilinear;
import ch.zhaw.lakerouting.interpolation.algorithms.InterpolationAlgorithm;
import ch.zhaw.lakerouting.interpolation.windfield.Windfield;
import ch.zhaw.lakerouting.interpolation.windfield.WindfieldContainer;
import ch.zhaw.lakerouting.interpolation.windfield.loader.SpaceWindFieldLoader;
import ch.zhaw.lakerouting.navigation.duration.SailingDuration;
import ch.zhaw.properties.PropertyLoad;

/**
 * This class computes the Decision-Matrix between two locations
 * 
 * @author Fevzi Yuekseldi, Mathias Habluetzel
 * 
 */
public class Decision {

	/* Contains the coordinates of the nodes */
	private List<List<Node>> graphList;
	private Windfield wv;
	private Windfield wvAdjusted;
	private int maxi;
	private int maxj;
	private SailingDuration sd;
	private WindfieldContainer windFieldContainer;
	
	private int spread;

	public Decision(){
		sd = new SailingDuration();
		graphList = new ArrayList<List<Node>>();
	}

	/**
	 * Calculates the orthodromy between two locations L1(x1,y1) and L2(x2,y2)
	 * 
	 * @param crd1
	 *            - Coordinates of the first location
	 * @param crd2
	 *            - Coordinates of the second location
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
		return distance;
	}

	public List<List<Node>> createDecisionGraph(Coordinate crd1, Coordinate crd2, int maxi,
			int maxj, int start, int spread) {
		
		double theta1 = crd1.getLongitudeInDegree();
		double phi1 = crd1.getLatitudeInDegree();
		double theta2 = crd2.getLongitudeInDegree();
		double phi2 = crd2.getLatitudeInDegree();

		/* local variables */
		double p, e, c, s;
		// P - This specifies the ratio of the distances between two steps and two
		// options
		p = 0.3;

		/* the euclidian distance */
		e = Math.sqrt(Math.pow((theta2 - theta1), 2.0)
				+ Math.pow((phi2 - phi1), 2.0));
		// System.out.println("Euclidian Distance: "+e+" Grad, "+e*60*1.852+" km");
		/* Cosinus value */
		c = (theta2 - theta1) / e;
		/* Sinus value */
		s = (phi2 - phi1) / e;

		/* Rotation Matrix */
		double[][] M = new double[2][2];
		M[0][0] = c;
		M[0][1] = -s;
		M[1][0] = s;
		M[1][1] = c;

		Coordinate crd = new Coordinate();
		setMaxi(maxi+1);
		setMaxj(maxj*2+1);
		initializeGraph(graphList);
		
		/* Create the table */
		for (int i = 0; i <= maxi; i++) {
			for (int j = -maxj; j <= maxj; j++) {
				/* Fill the table */
				crd.setLongitudeInDegree(M[0][0] * (e * i / maxi) + M[0][1]
						* (p * e * j / (2 * maxj)) + theta1);
				crd.setLatitudeInDegree(M[1][0] * (e * i / maxi) + M[1][1]
						* (p * e * j / (2 * maxj)) + phi1);
				graphList.get(i).get(j+maxj).setCrd(crd);
				crd = new Coordinate();
			}
		}
		
		programmationDynamique(start, spread);
		
		return graphList;
	}

	private void programmationDynamique(int start, int spread) {
		setSpread(spread);

		graphList.get(0).get(start).thisAsStartNode();

		getInterpolatedWindfield();
		/*
		 * The adjusted windfield with the interpolated windvectors.
		 */
		// wf = 4, 12, 13, 14
		// TODO Replace 3 with getWindFieldIndex()
		int windfieldNo = 3;
		setWvAdjusted(windFieldContainer.get(windfieldNo));

		/* Call the method progrDyn to calculate the times */
		for (int i = 1; i < getMaxi(); i++) {
			progrDyn(i, windfieldNo);
		}
	}

	private void progrDyn(int r, int windfieldNo) {
		
		double min;

		double[] travelDistance;

		/* Let's make this easier to read than with pure digits */
		double[][] position = new double[getMaxj()][2];
		final int __ROW__ = 0;
		final int __TRAVELTIME__ = 1;

		Node node;

		/* For-iterator over all nodes in the column r. k - the current node */
		for (int k = 0; k < getMaxj(); k++) {
			/* reset the minimum for each row */
			min = 1000000;

			travelDistance = new double[getMaxj()];

			/*
			 * Compares the current node (k) of the column r with all previous
			 * nodes (j)
			 */
			for (int j = 0; j < getMaxj(); j++) {

				travelDistance[j] = calcTravelDistance(r, j, k, windfieldNo);

				/*
				 * Saves the minimum distance and makes sure that the node is in
				 * the spread.
				 */
				if (travelDistance[j] < min) {
					if (Math.abs(k - j) <= spread) {
						min = travelDistance[j];
						position[k][__ROW__] = j;
						position[k][__TRAVELTIME__] = travelDistance[j];
					} else {
						position[k][__ROW__] = j;
						position[k][__TRAVELTIME__] = 1000000;
					}
				}
			}
			/*
			 * Sort the minimum-values. So we have the other shortest values if
			 * it doesn't match with spread (Not necessary yet).
			 */
			// final double[] etabli2 = etabli;
			// Arrays.sort(idx, new Comparator<Integer>() {
			// @Override public int compare(Integer o1, Integer o2) {
			// return Double.compare(etabli2[o1], etabli2[o2]);
			// }
			// });

			/* Computes the spread and updates the matrix graphList */
			/*
			 * Now this is just plain dangerous. If we are gone past the spread,
			 * the node will not receive an ancestor there but with a travel
			 * time meaning the node could(!) under bad circumstances become an
			 * ancestor which would create a path with a null pointer when
			 * calculating a potential back-path ...
			 */
			if (Math.abs(k - (int) position[k][__ROW__]) <= spread) {
				/*
				 * If the time is greater than 30, it will choose another
				 * windField and compute the distance again.
				 */
				if (position[k][__TRAVELTIME__] >= 30d) {
					int windField_raise = (int) position[k][__TRAVELTIME__] / 30;
					// System.out.print("Position1: "
					// + position[k][__TRAVELTIME__]);

					int windFieldNo_new = windfieldNo + windField_raise;

					/*
					 * This should never happen. But if it does, then we say
					 * STOP, that's your limit man...
					 */
					if (windFieldNo_new > 24)
						windFieldNo_new = 24;

					if (windField_raise > 1)
						System.out
								.println("STOOOP MAN!! Choose denser PARAMETERS!!");

					position[k][__TRAVELTIME__] = calcTravelDistance(r,
							(int) position[k][__ROW__], k, windFieldNo_new);
					// System.out.println(" Position2: "
					// + position[k][__TRAVELTIME__]);
				}

				/*
				 * Setting the graphList with current position and shortest
				 * distance.
				 */
				node = new Node(position[k][__TRAVELTIME__]);
				node.setPrevious(graphList.get(r - 1).get(
						(int) position[k][__ROW__]));
				node.setCrd(graphList.get(r).get(k).getCrd());
				graphList.get(r).set(k, node);
			} else {
				System.out.println("Shit!");
			}
		}
		setWv(windFieldContainer.get(windfieldNo));
	}

	/**
	 * Fills the graph at the beginning with default values, 0 for Nodes and
	 * 1000000 for TimeOfArrival
	 */
	private void initializeGraph(List<List<Node>> g) {
		Node node;
		ArrayList<Node> graphRow;
		for (int i = 0; i < getMaxi(); i++) {
			graphRow = new ArrayList<Node>();
			for (int j = 0; j < getMaxj(); j++) {
				node = new Node(1000000);
				graphRow.add(node);
			}
			g.add(graphRow);
		}
	}

	/**
	 * Gets the windFields from the File listed and returns the interpolated
	 * windVectors for the nodes over all windFields
	 */
	private void getInterpolatedWindfield() {
		/* Load the file */
		SpaceWindFieldLoader loader = new SpaceWindFieldLoader();
		InterpolationAlgorithm bil = new Bilinear();
		windFieldContainer = new WindfieldContainer();

		try {
			URI identifier = new PropertyLoad().getURIOfProperty(
					"file.path.windFieldContainer", "file");

			windFieldContainer.bulkLoadWindfield(identifier, loader);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		/* Interpolate all windFields at once */
		windFieldContainer = windFieldContainer.bulkInterpolateOnDecisionNet(
				graphList, bil);
		System.out.println("WF " + windFieldContainer.getDelta().toString());
	}

	/**
	 * Computes the index of the windField by the current time.
	 * 
	 * @return the index of the windField
	 */
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

	/**
	 * Computes the distance between two locations considering the windvectors.
	 * 
	 * @param r
	 *            - column index
	 * @param j
	 *            - index of the node in the column before
	 * @param k
	 *            - index of the current-Node
	 * @param wf
	 *            - index of the windField
	 * @return the distance
	 */
	private double calcTravelDistance(int r, int j, int k, int wf) {
		setWv(windFieldContainer.get(wf));
		double distance = ortho(graphList.get(r - 1).get(j).getCrd(), graphList.get(r).get(k).getCrd());
		WindVector v1 = getWv().getField().get(r - 1).get(j);
		WindVector v2 = getWv().getField().get(r).get(k);

		double sailingDuration = sd.getSailingDuration(graphList.get(r - 1).get(j).getCrd(),
				graphList.get(r).get(k).getCrd(), v1, v2, distance);

		sailingDuration = sailingDuration
				+ graphList.get(r - 1).get(j).getTimeOfArrival();
		getWvAdjusted().getField().get(r).set(k, v2);

		return sailingDuration;
	}

	public double[] transformCoordToVector(Coordinate crd) {
		double theta = crd.getLongitudeInRadian();
		double phi = crd.getLatitudeInRadian();

		double[] sphere = new double[3];
		sphere[0] = Math.cos(theta) * Math.cos(phi);
		sphere[1] = Math.sin(theta) * Math.cos(phi);
		sphere[2] = Math.sin(phi);

		return sphere;
	}

	/*
	 * Just some default methods to get the value of this variables. A common
	 * Java-usage
	 */

	public List<List<Node>> getGraphList() {
		return graphList;
	}
	
	public int getMaxi() {
		return maxi;
	}

	private void setMaxi(int maxi) {
		this.maxi = maxi;
	}

	public int getMaxj() {
		return maxj;
	}

	private void setMaxj(int maxj) {
		this.maxj = maxj;
	}

	public Windfield getWv() {
		return wv;
	}

	private void setWv(Windfield wv) {
		this.wv = wv;
	}

	public Windfield getWvAdjusted() {
		return wvAdjusted;
	}

	private void setWvAdjusted(Windfield wvAdjusted) {
		this.wvAdjusted = wvAdjusted;
	}

	public int getSpread() {
		return spread;
	}

	private void setSpread(int spread) {
		this.spread = spread;
	}
}
