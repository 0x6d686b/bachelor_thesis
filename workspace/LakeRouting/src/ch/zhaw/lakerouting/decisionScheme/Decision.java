package ch.zhaw.lakerouting.decisionScheme;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import ch.zhaw.lakerouting.datatypes.Node;

//import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.*;
import org.joda.time.DateTime;

import ch.zhaw.lakerouting.datatypes.Coordinate;
import ch.zhaw.lakerouting.datatypes.WindVector;
import ch.zhaw.lakerouting.interpolation.algorithms.Bilinear;
import ch.zhaw.lakerouting.interpolation.algorithms.InterpolationAlgorithm;
import ch.zhaw.lakerouting.interpolation.windfield.Windfield;
import ch.zhaw.lakerouting.interpolation.windfield.WindfieldContainer;
import ch.zhaw.lakerouting.interpolation.windfield.loader.SpaceWindFieldLoader;
import ch.zhaw.lakerouting.navigation.SailingDuration;
import ch.zhaw.properties.PropertyLoad;

/**
 * This class computes the Decision-Matrix between two locations and generates a
 * list, which contains the distances and windvectors for every Node.
 * 
 * @author Fevzi Yuekseldi, Mathias Habluetzel
 * 
 */
public class Decision {

	/** Time-limit constant */
	private static final double TIME_LIMIT_OF_WF = 30d;
	/** Maximum TimeofArrival, default value */
	private static final int MAX_TOA = 1000000;
	/** Contains the coordinates of the nodes */
	private List<List<Node>> graphList;
	/** Contains the current windvectors */
	private Windfield wv;
	/** Width of the graphList */
	private int maxi;
	/** Height of the graphList */
	private int maxj;
	/** Number of connections of a node */
	private int spread;
	private SailingDuration sd;
	private WindfieldContainer windFieldContainer;

	private static Logger logger = Logger.getLogger(Decision.class.getClass());

	public Decision() {
		sd = new SailingDuration();
		graphList = new ArrayList<List<Node>>();
	}

	/**
	 * static-Block, is only called once, when the first instance of this class
	 * is generated. So it loads the file-config only once.
	 */
	static {
		// Read Logging-Properties
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream("config/log4j.properties"));
			PropertyConfigurator.configure(prop);
		} catch (IOException e) {
			// If an Exception is throwed, use StandardOutput
			BasicConfigurator.configure();
			logger.error("Loading log4j.properties failed, use Console-Output as Standard-Output \n\n"
					+ e);
		}
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
	private double ortho(Coordinate crd1, Coordinate crd2) {

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

	/**
	 * The only method, which is public and can be called from the outside. This
	 * is the "entrance" of the program. This method fills the graphList with
	 * the values of the Coordinates. And after that it calls the method
	 * programmationDynamique, from where the distances are calculated.
	 * 
	 * @param crd1
	 *            - Coordinates of the starting-location
	 * @param crd2
	 *            - Coordinates of the destination-location
	 * @param maxi
	 *            - the length of columns
	 * @param maxj
	 *            - the length of rows (maxj*2+1)
	 * @param spread
	 *            - the number of connections of a node, which should be
	 *            considered
	 * @return The list with the nodes and its calculated distances
	 */
	public List<List<Node>> createDecisionGraph(Coordinate crd1,
			Coordinate crd2, int maxi, int maxj, int spread) {

		/*
		 * For a better understanding of the formula, we save the values theta's
		 * and phi's
		 */
		double theta1 = crd1.getLongitudeInDegree();
		double phi1 = crd1.getLatitudeInDegree();
		double theta2 = crd2.getLongitudeInDegree();
		double phi2 = crd2.getLatitudeInDegree();

		/* local variables */
		double p, e, c, s;
		/*
		 * P - This specifies the ratio of the distances between two steps and
		 * two options
		 */
		p = 0.3;

		/* the euclidian distance calculation */
		e = Math.sqrt(Math.pow((theta2 - theta1), 2.0)
				+ Math.pow((phi2 - phi1), 2.0));
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

		/* Setting the maximal values of rows and columns */
		setMaxi(maxi + 1);
		setMaxj(maxj * 2 + 1);

		initializeGraph(graphList);
		Coordinate crd = new Coordinate();

		/* Create the table */
		for (int i = 0; i <= maxi; i++) {
			for (int j = -maxj; j <= maxj; j++) {
				/* Calculate the coordinate values and save it to the list */
				// crd.setLongitudeInDegree(M[0][0] * (e * i / maxi) + M[0][1]
				// * (p * e * j / (maxi)) + theta1);
				// crd.setLatitudeInDegree(M[1][0] * (e * i / maxi) + M[1][1]
				// * (p * e * j / (maxi)) + phi1);
				crd.setLongitudeInDegree(M[0][0] * (e * i / maxi) + M[0][1]
						* (p * e * j / (2 * maxj)) + theta1);
				crd.setLatitudeInDegree(M[1][0] * (e * i / maxi) + M[1][1]
						* (p * e * j / (2 * maxj)) + phi1);
				// logger.info("Coordinate i="+i+" j="+(j+maxj)+" : " +
				// crd.toString());
				graphList.get(i).get(j + maxj).setCrd(crd);
				crd = new Coordinate();
			}
		}

		logger.info("Graph-Nodes with the calculated coordinates are created.");
		dynamicalProgramming(spread);

		return graphList;
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
				node = new Node(MAX_TOA);
				graphRow.add(node);
			}
			g.add(graphRow);
		}
	}

	/**
	 * This method initializes some variables and calls for every column the
	 * method progrdyn(). It's the beginning of the dynamical programming.
	 * 
	 * @param spread
	 *            - the number of connections of a node, which should be
	 *            considered
	 */
	private void dynamicalProgramming(int spread) {
		setSpread(spread);

		/* Setting the Node with index start as the starting-point */
		graphList.get(0).get(getMaxj() / 2).thisAsStartNode();

		/* The default windField number */
		// wf = 3, 4, 12, 13, 14 <-- some samples for experiments
		getInterpolatedWindfield();
		int windfieldNo = getWindFieldIndex();
		setWindFieldToGraphList(windfieldNo);

		/*
		 * The windField, which contains the current windField, which is needed.
		 */
		setWv(windFieldContainer.get(windfieldNo));

		logger.info("Dynamical Programming started.");
		/* Call the method progrDyn to calculate the distances in TOA */
		for (int i = 1; i < getMaxi(); i++) {
			progrDyn(i, windfieldNo);
		}
		logger.info("Dynamical Programming finished.");
		/*
		 * That we have access after the calculation to the default
		 * windField-Metadata
		 */
		setWv(windFieldContainer.get(windfieldNo));
	}

	/**
	 * Gets the windFields from the File listed and returns the interpolated
	 * windVectors for the nodes over all windFields
	 */
	private void getInterpolatedWindfield() {
		/* Load the file. But at first, we need a container */
		SpaceWindFieldLoader loader = new SpaceWindFieldLoader();
		InterpolationAlgorithm bil = new Bilinear();
		windFieldContainer = new WindfieldContainer();

		try {
			/* Load the path of the windField-File from a config file */
			URI identifier = new PropertyLoad().getURIOfProperty(
					"file.path.windFieldContainer", "file");

			/* Load the File and read and save the windFields to the container */
			windFieldContainer.bulkLoadWindfield(identifier, loader);
		} catch (URISyntaxException e) {
			logger.error("Couldn't find the property-name\n\n" + e);
//			e.printStackTrace();
		} catch (IOException e1) {
			logger.error("Failed to load the Property-File\n\n" + e1);
//			e1.printStackTrace();
		}

		/* Interpolate all windFields at once */
		windFieldContainer = windFieldContainer.bulkInterpolateOnDecisionNet(
				graphList, bil);
		logger.info("All windFields interpolated! \nWindFieldContainer Starting-Interval: "+windFieldContainer.getDelta().toString()+"\n");
	}

	/**
	 * Computes the index of the windField by current time.
	 * 
	 * @return the index of the windField
	 */
	private int getWindFieldIndex() {

		DateTime dt = new DateTime();
		/*
		 * To make sure, that the date of the windField is the same as the
		 * current date. TODO check, day of the windfield = current day
		 */
		int dayOfMonthWF = windFieldContainer.get(0).getMetadata().getDate()
				.dayOfMonth().get();
		int dayOfMonth = dt.getDayOfMonth();

		/* Check the hour of the day, and get the index for the windField */
		int hourOfDayWF = windFieldContainer.get(0).getMetadata().getDate()
				.getHourOfDay();
		int hourOfDay = dt.getHourOfDay();
		int minutesOfDay = dt.getMinuteOfHour();

		hourOfDay += (minutesOfDay < 30) ? 0 : 1;

		int difference = hourOfDay - hourOfDayWF;
		if (difference < 0) {
			difference = 24 + difference;
		}
		logger.info("Default WindField: "+windFieldContainer.get(difference).getMetadata().getDate());
		return difference;
	}

	/**
	 * Sets the default windField to the graphList.
	 * 
	 * @param windfieldNo
	 */
	private void setWindFieldToGraphList(int windfieldNo) {
		for (int i = 0; i < windFieldContainer.get(windfieldNo).getField()
				.size(); i++) {
			for (int j = 0; j < windFieldContainer.get(windfieldNo).getField()
					.get(0).size(); j++) {
				graphList
						.get(i)
						.get(j)
						.setWindVector(
								windFieldContainer.get(windfieldNo).getField()
										.get(i).get(j));
			}
		}
	}

	/**
	 * This method calculates step-by-step for every column the distances
	 * between two neighbor columns and saves the shortest distance considering
	 * the spread.
	 * 
	 * @param r
	 *            - the index of a column
	 * @param windfieldNo
	 *            - the default windField number
	 */
	private void progrDyn(int r, int windfieldNo) {

		double maxTimeOfArrival;

		/* the distances of a node to all his neighbor nodes (left column) */
		double[] travelDistance;

		/*
		 * The position and the row-index of the node, which has the shortest
		 * distance to the current node.
		 */
		double[][] position = new double[getMaxj()][2];
		final int __ROW__ = 0;
		final int __TRAVELTIME__ = 1;

		/* For-iterator over all nodes in the column r. k - the current node */
		for (int k = 0; k < getMaxj(); k++) {
			/* reset the minimum for each row */
			maxTimeOfArrival = MAX_TOA;

			travelDistance = new double[getMaxj()];

			/*
			 * Compares the current node (k) of the column r with all previous
			 * nodes (j) in column r-1.
			 */
			for (int j = 0; j < getMaxj(); j++) {

				travelDistance[j] = calcTravelDistance(r, j, k, windfieldNo);

				/*
				 * Saves the minimum distance and makes sure that the node is in
				 * the spread.
				 */
				if (travelDistance[j] < maxTimeOfArrival) {
					/*
					 * Checks, if the position of the shortest distance is legal
					 * based on spread. If not, it sets the default distance.
					 */
					if (Math.abs(k - j) <= spread) {
						maxTimeOfArrival = travelDistance[j];
						position[k][__ROW__] = j;
						position[k][__TRAVELTIME__] = travelDistance[j];
					} else {
						position[k][__ROW__] = j;
						position[k][__TRAVELTIME__] = MAX_TOA;
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
			if (Math.abs(k - (int) position[k][__ROW__]) <= spread) {
				/*
				 * If the time is greater than 30, it will choose another
				 * windField and compute the distance again.
				 */
				if (position[k][__TRAVELTIME__] >= TIME_LIMIT_OF_WF) {
					/* this calculates how much we have to raise the index of WF */
					int windField_raise = (int) (position[k][__TRAVELTIME__] / TIME_LIMIT_OF_WF);
					if (windField_raise > 1)
						logger.warn("Stop, the raise of the windField index is higher than 1. You should try" +
								" again with denser PARAMETERS!");

//					System.out.print("Position1: "
//							+ position[k][__TRAVELTIME__]);

					/* The new windField number */
					int windFieldNo_new = windfieldNo + windField_raise;

					/*
					 * This should never happen. But if it does, then we say
					 * STOP, that's your limit man...
					 */
					if (windFieldNo_new > 24)
						windFieldNo_new = 24;

					/* Recalculate the distance with the new windField */
                        position[k][__TRAVELTIME__] = calcTravelDistance(r,
							(int) position[k][__ROW__], k, windFieldNo_new);

//					System.out.println(" Position2: "
//							+ position[k][__TRAVELTIME__]);
				}

				/*
				 * Setting the graphList with current position and shortest
				 * distance.
				 */
				graphList.get(r).get(k)
						.setTimeOfArrival(position[k][__TRAVELTIME__]);
				graphList
						.get(r)
						.get(k)
						.setPrevious(
								graphList.get(r - 1).get(
										(int) position[k][__ROW__]));
			} else {
				logger.warn("Ooops! The nearest Node is not in the Spread!");
			}
		}

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
		/* Get the ortho-distance between two locations */
		double distance = ortho(graphList.get(r - 1).get(j).getCrd(), graphList
				.get(r).get(k).getCrd());
		WindVector v1 = graphList.get(r - 1).get(j).getWindVector();
		// WindVector v1 = getWv().getField().get(r - 1).get(j);
		WindVector v2 = getWv().getField().get(r).get(k);

		/* Get the sailingduration */
		double sailingDuration = sd.getSailingDuration(graphList.get(r - 1)
				.get(j).getCrd(), graphList.get(r).get(k).getCrd(), v1, v2,
				distance);

		/* Add the new duration to the last TOA */
		sailingDuration = sailingDuration
				+ graphList.get(r - 1).get(j).getTimeOfArrival();
		graphList.get(r).get(k).setWindVector(v2);

		return sailingDuration;
	}

	/**
	 * A method, which isn't used yet. It converts a coordinate to a vector.
	 * 
	 * @param crd
	 * @return A three-dimensional array.
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

	public int getSpread() {
		return spread;
	}

	private void setSpread(int spread) {
		this.spread = spread;
	}
}
