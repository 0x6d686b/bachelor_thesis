import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;

public class GridFrame extends JFrame {

    public GridFrame(String newTitel) {
        super.setTitle(newTitel);
    }

    public static void main(String str[]) {
        GridFrame fenster = new GridFrame("Optimalste Route zeichnen");
        fenster.setSize(700, 700);
        fenster.setVisible(true);
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(new Color(0, 0, 0));
        
        Decision de = new Decision();
		de.setLoc(de.graphe(8.05, 47.3, 226.88, -47.22));
		de.setMaxi(de.getLoc().length);
		de.setMaxj(de.getLoc()[0].length);
		de.setCoord(de.getLoc()[0][0].length);
		System.out.println("Maxi: "+de.getMaxi()+" Maxj: "+de.getMaxj()+" Coord:"+de.getCoord());
		
		ArrayList <ArrayList<Graph>> graphList = de.programmationDynamique(15);
		int x;
		int y;
		double min;
		double[][] position = new double[de.getMaxi()][2];
		for(int i=1;i<=de.getMaxi();i++){
			x=i-1;
			min = 1000000; 
			for (int j = 1; j <= de.getMaxj(); j++) {
				y=j-1;
				if(graphList.get(i-1).get(j-1).getTimeOfArrival()>=1000000d)
				{
					g.fillOval(50*i-20*i, 50*j-20*j, 10, 10);
				}else{
					g.setColor(new Color(40, 150, 140));
					g.fillOval(50*i-20*i, 50*j-20*j, 10, 10);
					g.setColor(new Color(0, 0, 0));
				}
				if(graphList.get(x).get(y).getTimeOfArrival()<min)
				{
					min = graphList.get(x).get(y).getTimeOfArrival();
					position[x][0] = graphList.get(x).get(y).getPreviousNode()[1];
					position[x][1] = y;
				}
			}
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
		
		int z = 0;
		for(int i=1;i<de.getMaxi();i++){
			g.drawLine(50*(i)-20*(i)+5, 50*((int)position[z][0]+1)-20*((int)position[z][0]+1)+5, 50*(i+1)-20*(i+1)+5, 50*((int)position[z][1]+1)-20*((int)position[z][1]+1)+5);
			z++;
		}
		
    }

}