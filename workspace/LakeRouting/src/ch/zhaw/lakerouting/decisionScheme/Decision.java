package ch.zhaw.lakerouting.decisionScheme;

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

	/* Contains the coordinates of the nodes */
	private List<List<Coordinate>> loc;
	private List<List<WindVector>> wv;
	private List<List<WindVector>> wvAdjusted;
	private int maxi;
	private int maxj;
	private SailingDuration sd;
	private WindfieldContainer windFieldContainer;
	private List<List<Node>> graphList;
	private int spread;

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

	public void createDecisionGraph(Coordinate crd1, Coordinate crd2, int m, int n) {
		/* The new graph with coordinates of the nodes */
		List<List<Coordinate>> coordList = new ArrayList<List<Coordinate>>();
		List<Coordinate> coordRow = new ArrayList<Coordinate>();
		Coordinate crd = new Coordinate();

		double theta1 = crd1.getLongitudeInDegree();
		double phi1 = crd1.getLatitudeInDegree();
		double theta2 = crd2.getLongitudeInDegree();
		double phi2 = crd2.getLatitudeInDegree();

		/* local variables */
		double p, e, c, s;
		p = 0.3; // Does anyone remember what this 'p' is good for? Well I do, do you too?

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

		/* Create the table */
		for (int i = 0; i <= m; i++) {
			for (int j = -n; j <= n; j++) {
				/* Fill the table */
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

		setLoc(coordList);
	}

	public void programmationDynamique(int start, int spread) {
        this.spread = spread;

		graphList = new ArrayList<List<Node>>();
        sd = new SailingDuration();

		initializeGraph(graphList);
        graphList.get(0).get(start).thisAsStartNode();

		getInterpolatedWindfield();

		/*
		 * Clone the ArrayList to make possible, that we can change the object
		 * wvAdjusted, and not the object of the windfieldContainer.
		 */
		// TODO Replace 3 with getWindFieldIndex()
		setWvAdjusted(new ArrayList<List<WindVector>>(windFieldContainer.get(3).getField()));

		/* Call the method progrDyn to calculate the times */
		for (int i = 1; i < getMaxi(); i++) {
			progrDyn(i);
		}
	}

    private void progrDyn(int r) {
        /**
         * Magic numbers no one knows why exactly they must be exactly this value
         */
        // wf = 4, 12, 13, 14
        // TODO Replace 3 with getWindFieldIndex()
        int wf = 3;
        double min;

        double[] travelDistance;

        /** Let's make this easier to read than with pure digits */
        double[][] position = new double[getMaxj()][2];
        final int __ROW__ = 0;
        final int __TRAVELTIME__ = 1;

        Node node;

        /** For-iterator over all nodes in the column r. k - the current node */
        for (int k = 0; k < getMaxj(); k++) {
            /** reset the minimum for each row */
            min = 1000000;

            travelDistance = new double[getMaxj()];

            /**
             * Compares the current node (k) of the column r with all previous
             * nodes (j)
             */
            for (int j = 0; j < getMaxj(); j++) {

                travelDistance[j] = calcTravelDistance(r, j, k, wf);

                /**
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
            /**
             * Sort the minimum-values. So we have the other shortest values if
             * it doesn't match with spread (Not necessary yet).
             */
            // final double[] etabli2 = etabli;
            // Arrays.sort(idx, new Comparator<Integer>() {
            // @Override public int compare(Integer o1, Integer o2) {
            // return Double.compare(etabli2[o1], etabli2[o2]);
            // }
            // });

            /** Computes the spread and updates the matrix graphList */
            /**
             * Now this is just plain dangerous (and therefore bullshit)
             * If we are gone past the spread, the node will not receive
             * an ancestor there but with a travel time meaning the node
             * could(!) under bad circumstances become an ancestor which
             * would create a path with a null pointer when calculating
             * a potential back-path ...
             *
             * You must know that such conceptual mistakes are pure time-
             * bombs waiting to kill you. And I will blame you for not
             * thoroughly thinking this through!
             */
            if (Math.abs(k - (int) position[k][__ROW__]) <= spread) {
                /**
                * If the time is greater than 30, it will choose another
                * windField and compute the distance again.
                */
                if (position[k][__TRAVELTIME__] >= 30d) {
                    int w_calculated = (int) position[k][__TRAVELTIME__] / 30;
                    System.out.print("Position1: " + position[k][__TRAVELTIME__]);
                    int wf_neu = wf + w_calculated;

                    /** Things everyone loooooves ... m( */
                    if (wf_neu > 24)
                        wf_neu = 24;

                    position[k][__TRAVELTIME__] = calcTravelDistance(r, (int) position[k][__ROW__], k, wf_neu);
                    System.out.println(" Position2: " + position[k][__TRAVELTIME__]);
                }

                /**
                 * Setting the graphList with current position and shortest
                 * distance.
                 */
                node = new Node(position[k][__TRAVELTIME__]);
                node.setPrevious(graphList.get(r-1).get((int) position[k][__ROW__]));
                graphList.get(r).set(k,node);
            } else {
                System.out.println("Shit!");
            }
        }
        setWv(windFieldContainer.get(wf).getField());
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
			windFieldContainer
					.bulkLoadWindfield(
							new URI(
									"file",
									"/var/tmp/11072915_905.dat",
									""), loader);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		/* Interpolate all windFields at once */
		windFieldContainer = windFieldContainer.bulkInterpolateOnDecisionNet(
				getLoc(), bil);
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
        setWv(windFieldContainer.get(wf).getField());
		double distance = ortho(loc.get(r - 1).get(j), loc.get(r).get(k));
		WindVector v1 = windFieldContainer.get(wf).getField().get(r - 1).get(j);
		WindVector v2 = windFieldContainer.get(wf).getField().get(r).get(k);

		double sailingDuration = sd.getSailingDuration(loc.get(r - 1).get(j),
				loc.get(r).get(k), v1, v2, distance);

		sailingDuration = sailingDuration
				+ graphList.get(r - 1).get(j).getTimeOfArrival();
		getWvAdjusted().get(r).set(k, v2);

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
	 * Just some default methods to get the value of this variables A common
	 * Java-usage
	 */

	public List<List<Node>> getGraphList() {
		return graphList;
	}

	public List<List<Coordinate>> getLoc() {
		return loc;
	}

	public void setLoc(List<List<Coordinate>> loc) {
		this.loc = loc;
        setMaxi(loc.size());
        setMaxj(loc.get(0).size());
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
