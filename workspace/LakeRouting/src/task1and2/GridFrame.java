package task1and2;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;
/**
 * This class demonstrates the Decision-Matrix in a graphical way.
 * @author Fevzi Y�kseldi, Mathias Habl�tzel
 *
 */
public class GridFrame extends JFrame{

    public GridFrame(String newTitel) {
        super.setTitle(newTitel);
    }

    public static void main(String str[]) {
        GridFrame fenster = new GridFrame("Optimalste Route zeichnen");
        fenster.setSize(900, 700);
        fenster.setVisible(true);
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.BLACK);
        
        Decision de = new Decision();
		de.setLoc(de.graphe(8.05, 47.3, 226.88, -47.22));
		de.setMaxi(de.getLoc().length);
		de.setMaxj(de.getLoc()[0].length);
		de.setCoord(de.getLoc()[0][0].length);
		System.out.println("Maxi: "+de.getMaxi()+" Maxj: "+de.getMaxj()+" Coord:"+de.getCoord());
		
		ArrayList <ArrayList<Graph>> graphList = de.programmationDynamique(15);
		//longitudes, latitudes
		double[][][] loc = de.getLoc();
		int[][][] positionLoLa = new int[de.getMaxi()][de.getMaxj()][2];
		double longMin = loc[0][0][0];
		double latMin = loc[0][0][1];
		double longMax=loc[0][0][0];
		double latMax=loc[0][0][1];
		int x;
		int y;
		for(int i=0;i<de.getMaxi();i++){
			for (int j = 0; j < de.getMaxj(); j++) {
				if(loc[i][j][0]<longMin)
				{
					longMin = loc[i][j][0];
				}else if(loc[i][j][0]>longMax)
				{
					longMax = loc[i][j][0];
				}
				if(loc[i][j][1]<latMin)
				{
					latMin=loc[i][j][1];
				}else if(loc[i][j][1]>latMax)
				{
					latMax=loc[i][j][1];
				}
			}
		}
		int maxLaenge = 500/(Math.abs((int)longMin) + Math.abs((int)longMax));
		int maxBreite = 500/(Math.abs((int)latMin) + Math.abs((int)latMax));
		for(int i=1;i<=de.getMaxi();i++){
			for (int j = 1; j <= de.getMaxj(); j++) {
				positionLoLa[i-1][j-1][0]=((int)loc[i-1][j-1][0]+Math.abs((int)longMin))*3+50;
				positionLoLa[i-1][j-1][1]=(90-((int)loc[i-1][j-1][1]))*3+50;
				if(graphList.get(i-1).get(j-1).getTimeOfArrival()>=1000000d)
				{
					g.fillOval(positionLoLa[i-1][j-1][0], positionLoLa[i-1][j-1][1], 4, 4);
				}else{
					g.setColor(Color.BLUE);
					g.fillOval(positionLoLa[i-1][j-1][0], positionLoLa[i-1][j-1][1], 4, 4);
					g.setColor(Color.BLACK);
				}
				
			}
		}
		
		
		double[][] position = new double[de.getMaxi()][2];
		for(int i=1;i<=de.getMaxi();i++){
			x=i-1;
			Graph obj = (Graph)Collections.min(graphList.get(x));
			position[x][0] = obj.getPreviousNode()[1];
			position[x][1] = graphList.get(x).indexOf(obj);
//			System.out.println("MINIMUM: "+obj.getTimeOfArrival() +" POS: "+graphList.get(x).get((int)position[x][1]).getTimeOfArrival());
		}
		int w;
		for(int i=1;i<=de.getMaxi();i++){
			w = de.getMaxi()-i;
			if(w>=1)
			{
				position[w-1][1]=position[w][0];
				position[w-1][0]=graphList.get(w).get((int)position[w][0]).getPreviousNode()[1];
			}
		}
		
		g.setColor(Color.RED);
		int z = 0;
//		for(int i=1;i<de.getMaxi();i++){
//			g.drawLine(50*(i)-20*(i)+5, 50*((int)position[z][0]+1)-20*((int)position[z][0]+1)+5, 50*(i+1)-20*(i+1)+5, 50*((int)position[z][1]+1)-20*((int)position[z][1]+1)+5);
//			z++;
//		}
		for(int i=1;i<de.getMaxi();i++){
			g.drawLine(positionLoLa[i-1][(int)position[z][0]][0], positionLoLa[i-1][(int)position[z][0]][1], positionLoLa[i][(int)position[z][1]][0], positionLoLa[i][(int)position[z][1]][1]);
			z++;
		}
		g.setColor(Color.GRAY);
		z = 0;
		double pos;
		double posx;
		for(int i=0;i<de.getMaxi();i++){
			x=de.getMaxi()-i-1;
			z=de.getMaxi()-i;
			for (int j = 0; j < de.getMaxi(); j++) {
				if(x<=0) break;
				if(j!=(int)position[x-1][1] && graphList.get(x).get(j).getTimeOfArrival()<1000000)
				{
					pos = graphList.get(x).get(j).getPreviousNode()[1];
					posx = graphList.get(x).get(j).getNode()[1];
					g.drawLine(positionLoLa[x][j][0], positionLoLa[x][(int)posx][1], positionLoLa[x-1][(int)pos][0], positionLoLa[x-1][(int)pos][1]);
				}
				
			}
		}
//		for(int i=0;i<de.getMaxi();i++){
//			x=de.getMaxi()-i-1;
//			z=de.getMaxi()-i;
//			for (int j = 0; j < de.getMaxi(); j++) {
//				if(x<=0) break;
//				if(j!=(int)position[x-1][1] && graphList.get(x).get(j).getTimeOfArrival()<1000000)
//				{
//					pos = graphList.get(x).get(j).getPreviousNode()[1];
//					posx = graphList.get(x).get(j).getNode()[1];
//					g.drawLine(50*(z)-20*(z)+5, 50*((int)posx+1)-20*((int)posx+1)+5, 50*(z-1)-20*(z-1)+5, 50*((int)pos+1)-20*((int)pos+1)+5);
//				}
//				
//			}
//		}
		
    }
}