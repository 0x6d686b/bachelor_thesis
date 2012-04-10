package ch.zhaw.lakerouting.decisionScheme;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import ch.zhaw.lakerouting.datatypes.Coordinate;
import ch.zhaw.lakerouting.datatypes.Graph;

/**
 * This class demonstrates the Decision-Matrix in a graphical way.
 * 
 * @author Fevzi Yuekseldi, Mathias Habluetzel
 * 
 */
public class GridFrame extends JFrame{

	public GridFrame(String newTitel) {
		super.setTitle(newTitel);
	}

	public static void main(String str[]) {
		GridFrame fenster = new GridFrame("Optimalste Route zeichnen");
		fenster.setSize(940, 740);
		fenster.setVisible(true);
		fenster.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.BLACK);

		Decision de = new Decision();

		Coordinate crd1 = new Coordinate();
		Coordinate crd2 = new Coordinate();
		// 9.40, 47.6, 9.6, 47.58
		crd1.setLongitudeInDegree(9.40);
		crd1.setLatitudeInDegree(47.6);
		crd2.setLongitudeInDegree(9.6);
		crd2.setLatitudeInDegree(47.58);

		de.setLoc(de.graphe(crd1, crd2));
		de.setMaxi(de.getLoc().size());
		de.setMaxj(de.getLoc().get(0).size());
		de.setCoord(2);

		System.out.println("Maxi: " + de.getMaxi() + " Maxj: " + de.getMaxj()
				+ " Coord:" + de.getCoord());

		ArrayList<ArrayList<Graph>> graphList = de.programmationDynamique(10);// 15
		// longitudes, latitudes
		ArrayList<ArrayList<Coordinate>> loc = de.getLoc();
		int[][][] positionLoLa = new int[de.getMaxi()][de.getMaxj()][2];
		double longMin = loc.get(0).get(0).getLongitudeInDegree();
		double latMin = loc.get(0).get(0).getLatitudeInDegree();
		double longMax = loc.get(0).get(0).getLongitudeInDegree();
		double latMax = loc.get(0).get(0).getLatitudeInDegree();

		// determine the smallest/biggest longitude and latitude
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
		double stepWidth = 840 / (longMax - longMin);
		double stepHeight = 630 / (latMax - latMin);
		double step = (stepWidth<stepHeight)? stepWidth : stepHeight;
		System.out.println("Step:" + stepWidth + " " + stepHeight);
//		System.out.println("LongMin: "+longMin+" LongMax:"+longMax);
		//draw the points related to longitudes and latitudes
		for (int i = 1; i <= de.getMaxi(); i++) {
			for (int j = 1; j <= de.getMaxj(); j++) {
				// the multiplication with 3 and addition of 50 is only a
				// constant variable for this example
				positionLoLa[i - 1][j - 1][0] = (int) ((loc.get(i - 1).get(j - 1).getLongitudeInDegree() - longMin) * step) + 50;
				positionLoLa[i - 1][j - 1][1] = (int) ((loc.get(i - 1).get(j - 1).getLatitudeInDegree() - latMin) * step) + 50;
//				System.out.println("TEST: "+loc.get(i-1).get(j-1).getLongitudeInDegree()+" "+loc.get(i-1).get(j-1).getLatitudeInDegree()+" "+graphList.get(i-1).get(j-1).getNode()[0]);
//				System.out.println("Psotion: " + positionLoLa[i - 1][j - 1][0] + " " + positionLoLa[i - 1][j - 1][1]);
				if (graphList.get(i - 1).get(j - 1).getTimeOfArrival() >= 1000000d) {
					g.fillOval(positionLoLa[i - 1][j - 1][0], positionLoLa[i - 1][j - 1][1], 4, 4);
				} else {
					g.setColor(Color.BLUE);
					g.fillOval(positionLoLa[i - 1][j - 1][0],
							positionLoLa[i - 1][j - 1][1], 4, 4);
					g.setColor(Color.BLACK);
				}
				System.out.println("WV "+i+j+":"+de.getWv().get(i-1).get(j-1).toString());
//				System.out.println(positionLoLa[i - 1][j - 1][0]+(int)(de.getWv().get(i-1).get(j-1).getV()* stepWidth));
				g.setColor(Color.orange);
				g.drawLine(positionLoLa[i - 1][j - 1][0], positionLoLa[i - 1][j - 1][1], positionLoLa[i - 1][j - 1][0]+(int)(de.getWv().get(i-1).get(j-1).getV()* step*0.005), positionLoLa[i - 1][j - 1][1]+(int)(de.getWv().get(i-1).get(j-1).getU()* step*0.005));
				g.setColor(Color.BLACK);
			}
		}

		// get the minimum timeOfArrival Node of every column x and save it to
		// position
		int x;
		double[][] position = new double[de.getMaxi()][2];
		for (int i = 1; i <= de.getMaxi(); i++) {
			x = i - 1;
			Graph obj = (Graph) Collections.min(graphList.get(x));
			position[x][0] = obj.getPreviousNode()[1];
			position[x][1] = graphList.get(x).indexOf(obj);
		}

		// Now we have the last minimum timeOfArrival Node of the last column
		// We have now to go backwards started at the last minimum Node and save
		// the path
		int w;
		for (int i = 1; i <= de.getMaxi(); i++) {
			w = de.getMaxi() - i;
			if (w >= 1) {
				position[w - 1][1] = position[w][0];
				position[w - 1][0] = graphList.get(w).get((int) position[w][0])
						.getPreviousNode()[1];
			}
		}

		// draw the shortest path in red
		g.setColor(Color.RED);
		int z = 0;
		for (int i = 1; i < de.getMaxi(); i++) {
			g.drawLine(positionLoLa[i - 1][(int) position[z][0]][0],
					positionLoLa[i - 1][(int) position[z][0]][1],
					positionLoLa[i][(int) position[z][1]][0],
					positionLoLa[i][(int) position[z][1]][1]);
			z++;
		}

		// Start at the last column and draw all shortest connections of every
		// nodes
		g.setColor(Color.GRAY);
		z = 0;
		double pos;
		double posx;
		// the destination point is in our algorithm exactly in the middle of
		// the last column
		int shortestPoint = de.getMaxi() / 2;
		boolean atFirstTime = true;
		for (int i = 0; i < de.getMaxi(); i++) {
			x = de.getMaxi() - i - 1;
			z = de.getMaxi() - i;
			for (int j = 0; j < de.getMaxi(); j++) {
				if (x <= 0)
					break;
				// Disallow to draw the shortest path again and ignore the nodes
				// with TOA <1000000
				if (j != (int) position[x - 1][1]
						&& graphList.get(x).get(j).getTimeOfArrival() < 1000000) {
					// save the previous and the current node
					pos = graphList.get(x).get(j).getPreviousNode()[1];
					posx = graphList.get(x).get(j).getNode()[1];
					// the path from the destination node will be drawn green
					if (j == shortestPoint && atFirstTime) {
						g.setColor(Color.GREEN);
						shortestPoint = (int) pos;
						atFirstTime = false;
					} else {
						g.setColor(Color.GRAY);
					}
					g.drawLine(positionLoLa[x][(int) posx][0],
							positionLoLa[x][(int) posx][1],
							positionLoLa[x - 1][(int) pos][0],
							positionLoLa[x - 1][(int) pos][1]);
				}
			}
			atFirstTime = true;
		}
		
		
	}
}