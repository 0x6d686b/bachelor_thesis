package ch.zhaw.lakerouting.decisionScheme;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import ch.zhaw.lakerouting.datatypes.Coordinate;
import ch.zhaw.lakerouting.datatypes.Graph;

/**
 * This class demonstrates the Decision-Matrix in a graphical way.
 * 
 * @author Fevzi Yuekseldi, Mathias Habluetzel
 * 
 */
public class GridFrame extends JFrame implements ActionListener {

	/** default variable, nothing special */
	private static final long serialVersionUID = 1L;

	protected JTextField txtFieldStart;
	protected JTextField txtFieldLength;
	protected JTextField txtFieldWidth;
	protected JTextField txtFieldSpread;
	protected JLabel txtFieldStartLabel;
	protected JLabel txtFieldLengthLabel;
	protected JLabel txtFieldWidthLabel;
	protected JLabel txtFieldSpreadLabel;

	private List<List<Graph>> graphList;
	private List<List<Coordinate>> loc;
	private double longMin;
	private double latMin;
	private double longMax;
	private double latMax;
	private int start = 10;
	private int m = 20;
	private int n = 10;
	private int spread = 10;

	private int[][][] positionLongLats;

	private Decision de;

	public GridFrame() {
		initializeVariables(start,m,n,spread);
		
		Container container = getContentPane();
		container.setLayout(new FlowLayout());
		
		txtFieldStart = new JTextField(10);
		txtFieldStart.addActionListener(this);
		txtFieldLength = new JTextField(10);
		txtFieldLength.addActionListener(this);
		txtFieldWidth = new JTextField(10);
		txtFieldWidth.addActionListener(this);
		txtFieldSpread = new JTextField(10);
		txtFieldSpread.addActionListener(this);
		txtFieldStartLabel = new JLabel("Start: ");
		txtFieldLengthLabel = new JLabel("Length: ");
		txtFieldWidthLabel = new JLabel("Width: ");
		txtFieldSpreadLabel = new JLabel("Spread: ");
		
		
		container.add(txtFieldStartLabel);
		container.add(txtFieldStart);
		container.add(txtFieldLengthLabel);
		container.add(txtFieldLength);
		container.add(txtFieldWidthLabel);
		container.add(txtFieldWidth);
		container.add(txtFieldSpreadLabel);
		container.add(txtFieldSpread);
	}

	public static void main(String str[]) {
		GridFrame fenster = new GridFrame();
		fenster.setTitle("Optimalste Route zeichnen");
		fenster.setSize(940, 740);
		fenster.setVisible(true);
		fenster.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.BLACK);

		/*
		 * These are needed to normalize the distance between the nodes
		 * Therefore it is now possible to draw all graphs
		 */
		double stepWidth = 840 / (longMax - longMin);
		double stepHeight = 630 / (latMax - latMin);
		double step = (stepWidth < stepHeight) ? stepWidth : stepHeight;
		drawPointAndWindVector(g, de, positionLongLats, step);

		/*
		 * Get the minimum timeOfArrival Node of every column x and save it to
		 * position
		 */
		int x;
		double[][] positionOfMinArrival = new double[de.getMaxi()][2];
		for (int i = 1; i <= de.getMaxi(); i++) {
			x = i - 1;
			Graph obj = (Graph) Collections.min(graphList.get(x));
			positionOfMinArrival[x][0] = obj.getPreviousNode()[1];
			positionOfMinArrival[x][1] = graphList.get(x).indexOf(obj);
		}

		/*
		 * Now we have the last minimum timeOfArrival Node of the last column.
		 * We have now to go backwards started at the last minimum Node and save
		 * the path
		 */
		int w;
		for (int i = 1; i <= de.getMaxi(); i++) {
			w = de.getMaxi() - i;
			if (w >= 1) {
				positionOfMinArrival[w - 1][1] = positionOfMinArrival[w][0];
				positionOfMinArrival[w - 1][0] = graphList.get(w)
						.get((int) positionOfMinArrival[w][0])
						.getPreviousNode()[1];
			}
		}

		drawShortestPath(g, de, positionLongLats, positionOfMinArrival);

		drawAllShortestPathOfNodes(g, de, positionLongLats, positionOfMinArrival);
		
	}

	/**
	 * Initialize Variables, which we need to draw the graph.
	 * This method is called in the constructor to not recalculate the 
	 * graph, when swing calls the repaint method.
	 */
	private void initializeVariables(int start, int m, int n, int spread) {
		de = new Decision();

		/* Define start and destination node */
		Coordinate crd1 = new Coordinate();
		Coordinate crd2 = new Coordinate();
		crd1.setLongitudeInDegree(9.40);
		crd1.setLatitudeInDegree(47.6);
		crd2.setLongitudeInDegree(9.6);
		crd2.setLatitudeInDegree(47.58);

		/*
		 * Set the graph with long/lat nodes and Get the graph with decision
		 * tree
		 */
		de.graphe(crd1, crd2,m,n);
		de.programmationDynamique(start, spread);
		graphList = de.getGraphList();// 15
		loc = de.getLoc();
		
		positionLongLats = new int[de.getMaxi()][de.getMaxj()][2];
		calculateMinMax(de);
	}
	/**
	 * Determine the smallest/highest longitude and latitude of the hole
	 * decision net. This will be used to normalize the graphic.
	 * 
	 * @param de
	 *            - object of the class Decision
	 * @param loc
	 *            - Contains long & lat information of the nodes
	 */
	private void calculateMinMax(Decision de) {

		longMin = loc.get(0).get(0).getLongitudeInDegree();
		latMin = loc.get(0).get(0).getLatitudeInDegree();
		longMax = loc.get(0).get(0).getLongitudeInDegree();
		latMax = loc.get(0).get(0).getLatitudeInDegree();
		for (int i = 0; i < de.getMaxi(); i++) {
			for (int j = 0; j < de.getMaxj(); j++) {
				if (loc.get(i).get(j).getLongitudeInDegree() < longMin) {
					longMin = loc.get(i).get(j).getLongitudeInDegree();
				} else if (loc.get(i).get(j).getLongitudeInDegree() > longMax) {
					longMax = loc.get(i).get(j).getLongitudeInDegree();
				}
				if (loc.get(i).get(j).getLatitudeInDegree() < latMin) {
					latMin = loc.get(i).get(j).getLatitudeInDegree();
				} else if (loc.get(i).get(j).getLatitudeInDegree() > latMax) {
					latMax = loc.get(i).get(j).getLatitudeInDegree();
				}
			}
		}
	}

	/**
	 * Draws the points on the decision-net related to longitudes and latitudes
	 * values. Draws also on every node the interpolated windvector. If the
	 * point has a connection to the column before, it will be BLUE. Otherwise
	 * if it hasn't any connection, it will be DARK_GRAY. The windvectors with
	 * the arrows will be ORANGE.
	 * 
	 * @param g
	 *            - graphics (to draw)
	 * @param de
	 *            - object of the class Decision
	 * @param positionLongLat
	 *            - Contains long & lat information to draw (Not the same as
	 *            loc)
	 * @param longMin
	 *            - The min value of longitude -> Needed to normalize the graph
	 */
	private void drawPointAndWindVector(Graphics g, Decision de,
			int[][][] positionLongLat, double step) {
		for (int i = 1; i <= de.getMaxi(); i++) {
			for (int j = 1; j <= de.getMaxj(); j++) {
				/*
				 * We have now to multiply the value with the step to normalize
				 * the graph to the screen 50 is the padding to left and top
				 */
				positionLongLat[i - 1][j - 1][0] = (int) ((loc.get(i - 1)
						.get(j - 1).getLongitudeInDegree() - longMin) * step) + 50;
				positionLongLat[i - 1][j - 1][1] = 360 - (int) ((loc.get(i - 1)
						.get(j - 1).getLatitudeInDegree() - latMin) * step) + 50;

				/*
				 * Draw the points which are TimeOfArrival>=100000 black
				 * otherwise blue.
				 */
				if (graphList.get(i - 1).get(j - 1).getTimeOfArrival() >= 1000000d) {
					g.fillOval(positionLongLat[i - 1][j - 1][0],
							positionLongLat[i - 1][j - 1][1], 4, 4);
				} else {
					g.setColor(Color.BLUE);
					g.fillOval(positionLongLat[i - 1][j - 1][0],
							positionLongLat[i - 1][j - 1][1], 4, 4);
				}

				// System.out.println("WV "+i+j+":"+de.getWv().get(i-1).get(j-1).toString());
				g.setColor(Color.orange);
				// TODO Draw the different wf's
				drawLineWithArrow(g, de, positionLongLat, step, i, j);
				g.setColor(Color.DARK_GRAY);
			}
		}
	}

	/**
	 * Draws the windvectors with an arrow on every point.
	 * 
	 * @param g
	 *            - graphics (to draw)
	 * @param de
	 *            - object of the class Decision
	 * @param positionLongLat
	 *            - Contains long & lat information to draw (Not the same as
	 *            loc)
	 * @param step
	 *            - The step between two points -> Needed to normalize the graph
	 * @param i
	 *            - Number of column
	 * @param j
	 *            - Number of row
	 */
	private void drawLineWithArrow(Graphics g, Decision de,
			int[][][] positionLongLat, double step, int i, int j) {
		double l = 0.8;
		double f = 0.05;
		double factor = 0.005;

//		double v = de.getWv().get(i - 1).get(j - 1).getV() * step * factor;
//		double u = -de.getWv().get(i - 1).get(j - 1).getU() * step * factor;
		double v = de.getWvAdjusted().get(i - 1).get(j - 1).getV() * step * factor;
		double u = -de.getWvAdjusted().get(i - 1).get(j - 1).getU() * step * factor;
		
		double x1 = positionLongLat[i - 1][j - 1][0];
		double y1 = positionLongLat[i - 1][j - 1][1];
		double x2 = x1 + l * v;
		double y2 = y1 + l * u;
		double x3 = x1 + l * v - f * u;
		double y3 = y1 + l * u + f * v;
		double x4 = x1 + l * v + f * u;
		double y4 = y1 + l * u - f * v;

		double calcV = positionLongLat[i - 1][j - 1][0] + v;
		double calcU = positionLongLat[i - 1][j - 1][1] + u;

		g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
		g.drawLine((int) x2, (int) y2, (int) x3, (int) y3);
		g.drawLine((int) x3, (int) y3, (int) calcV, (int) calcU);
		g.drawLine((int) calcV, (int) calcU, (int) x4, (int) y4);
		g.drawLine((int) x4, (int) y4, (int) x2, (int) y2);
	}

	/**
	 * Draws the shortest path in red, this is shortest route from the start node up
	 * to the last column. The arrival-node may not be the same as the destination-node.
	 * Hence it isn't the most optimal route.
	 * 
	 * @param g
	 *            - graphics (to draw)
	 * @param de
	 *            - object of the class Decision
	 * @param positionLongLat
	 *            - Contains long & lat information to draw (Not the same as
	 *            loc)
	 * @param positionOfMinArrival
	 *            - Contains all connections of the shortest Path
	 */
	private void drawShortestPath(Graphics g, Decision de,
			int[][][] positionLongLat, double[][] positionOfMinArrival) {
		g.setColor(Color.RED);
		int z = 0;
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(3));
		for (int i = 1; i < de.getMaxi(); i++) {
			g.drawLine(
					positionLongLat[i - 1][(int) positionOfMinArrival[z][0]][0],
					positionLongLat[i - 1][(int) positionOfMinArrival[z][0]][1],
					positionLongLat[i][(int) positionOfMinArrival[z][1]][0],
					positionLongLat[i][(int) positionOfMinArrival[z][1]][1]);
			z++;
		}
	}

	/**
	 * Beginning from the last column, it draws for every node the shortest path
	 * to the column before, always one path for one node. 
	 * The destination point is in our algorithm exactly in the middle of the last 
	 * column. So it also draws started there the most optimal route in green.
	 * 
	 * @param g
	 *            - graphics (to draw)
	 * @param de
	 *            - object of the class Decision
	 * @param graphList
	 *            - Contains some information of the nodes
	 * @param positionLongLat
	 *            - Contains long & lat information to draw (Not the same as
	 *            loc)
	 * @param positionOfMinArrival
	 *            - Contains all connections of the shortest Path
	 */
	private void drawAllShortestPathOfNodes(Graphics g, Decision de,
			int[][][] positionLongLat, double[][] positionOfMinArrival) {
		int x;
		double pos;
		double posx;
		int shortestPoint = de.getMaxj() / 2;
		boolean atFirstTime = true;
		Graphics2D g2 = (Graphics2D) g;
		for (int i = 0; i < de.getMaxi(); i++) {
			x = de.getMaxi() - i - 1;
			if (x <= 0)
				break;
			for (int j = 0; j < de.getMaxj(); j++) {
				/* Ignore the nodes with TOA <1000000 */
				if (graphList.get(x).get(j).getTimeOfArrival() < 1000000) {
					/* Save the previous and the current node */
					pos = graphList.get(x).get(j).getPreviousNode()[1];
					posx = graphList.get(x).get(j).getNode()[1];
					/* The path from the destination node will be drawn green */
					
					if (j == shortestPoint && atFirstTime) {
						g2.setStroke(new BasicStroke(3));
						g.setColor(Color.BLACK);
						DecimalFormat f = new DecimalFormat("#0.00");
						g.drawString(
								""
										+ f.format(graphList.get(x).get(j)
												.getTimeOfArrival()),
								positionLongLat[x][(int) posx][0],
								positionLongLat[x][(int) posx][1]);
						g.setColor(Color.GREEN);
						shortestPoint = (int) pos;
						atFirstTime = false;

					} else {
						g.setColor(Color.lightGray);

						g2.setStroke(new BasicStroke(1));
						/* Disallow to draw the shortest path again */
						if (j == (int) positionOfMinArrival[x - 1][1])
							continue;
					}
					g.drawLine(positionLongLat[x][(int) posx][0],
							positionLongLat[x][(int) posx][1],
							positionLongLat[x - 1][(int) pos][0],
							positionLongLat[x - 1][(int) pos][1]);
				}
			}
			atFirstTime = true;
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {		
		if(txtFieldStart.getText()!=null && !txtFieldStart.getText().equals("")){
			start = Integer.parseInt(txtFieldStart.getText());
		}
		if(txtFieldLength.getText()!=null && !txtFieldLength.getText().equals("")){
			m = Integer.parseInt(txtFieldLength.getText());
		}
		if(txtFieldWidth.getText()!=null && !txtFieldWidth.getText().equals("")){
			n = Integer.parseInt(txtFieldWidth.getText());
		}
		if(txtFieldSpread.getText()!=null && !txtFieldSpread.getText().equals("")){
			spread = Integer.parseInt(txtFieldSpread.getText());
		}
		
		initializeVariables(start,m, n,spread);
		repaint();
	}
}