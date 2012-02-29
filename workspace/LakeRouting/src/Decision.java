import java.util.ArrayList;


public class Decision {

	private double[][][] loc;
	private int maxi;
	private int maxj;
	private int coord;
	
	public double ortho(double x1,double y1,double x2,double y2){
		double theta1=Math.PI*x1/180;
		double phi1=Math.PI*y1/180;
		double theta2=Math.PI*x2/180;
		double phi2=Math.PI*y2/180;
		
		double distance = 360*60*Math.acos(Math.cos(theta1-theta2)*Math.cos(phi1)*Math.cos(phi2)+Math.sin(phi1)*Math.sin(phi2))/(2*Math.PI);
		return distance;
	}
	
	public double[][][] graphe(double theta1, double phi1, double theta2, double phi2){
		double p,e,c,s;
		int m,n;
		p=0.3; 
		m = 20;
		n=10;
		
		e = Math.sqrt(Math.pow((theta2-theta1), 2.0) + Math.pow((phi2-phi1), 2.0));
		c = (theta2-theta1)/e;
		s = (phi2-phi1)/e;
		
		double[][] M = new double[2][2];
		M[0][0] = c;
		M[0][1] = -s;
		M[1][0] = s;
		M[1][1] = c;
		double[][][] tableMatrix=new double[21][21][2];
		//Tabelle erstellen
		for(int i= 0;i<=m;i++)
		{
			for(int j=-n;j<=n;j++)
			{
				tableMatrix[i][j+n][0] = M[0][0]*(e*i/m) + M[0][1]*(p*e*j/m) + theta1;
				tableMatrix[i][j+n][1] = M[1][0]*(e*i/m) + M[1][1]*(p*e*j/m) + phi1;
				System.out.println(i+" "+j+": "+tableMatrix[i][j+n][0]);
				System.out.println(i+" "+j+": "+tableMatrix[i][j+n][1]);
			}
		}
		return tableMatrix;
	}
	
	public void programmationDynamique(int start){
		ArrayList <ArrayList<Graph>> graphList = new ArrayList<ArrayList<Graph>>();
		ArrayList<Graph> innerGraphList = new ArrayList<Graph>();
		Graph graph;
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
		
		double[] init2 = new double[2];
		init2[0]=1;
		init2[1]=1;
		graphList.get(0).get(start).setPreviousNode(init2);
		graphList.get(0).get(start).setNode(init2);
		graphList.get(0).get(start).setTimeOfArrival(0);
		
		for (int i = 1; i < getMaxi(); i++) {
			graphList = progrDyn(i, graphList);
		}
		for (ArrayList<Graph> d : graphList) {
			for(int i=0;i<d.size();i++)
			{
				System.out.println("PN: "+d.get(i).getPreviousNode()[0]+" "+d.get(i).getPreviousNode()[1]+" N: "+d.get(i).getNode()[0]+" "+d.get(i).getNode()[1]+" TA: "+d.get(i).getTimeOfArrival());
			}
		}
	}	
	
	private ArrayList<ArrayList<Graph>> progrDyn(int r, ArrayList<ArrayList<Graph>> graphList){
		int spread = 4;
		double[][][] loc = getLoc();
		double[] etabli;
		double min;
		double[][] position = new double[getMaxj()][2];
		double[] node;
		for (int k = 0; k < getMaxj(); k++) {
			
			min = 1000000;
			
			etabli = new double[getMaxj()];
			for (int j = 0; j < getMaxj(); j++) {
				etabli[j] = graphList.get(r-1).get(j).getTimeOfArrival() + ortho(loc[r-1][j][0], loc[r-1][j][1], loc[r][k][0], loc[r][k][1]);
				
				if(etabli[j]<min)
				{
					min = etabli[j];
					position[k][0] = j;
					position[k][1] = etabli[j];
				}
			}
			
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
	
	public void setX(double theta, double phi){
		double[] sphere = new double[3];
		sphere[0] = Math.cos(Math.PI*theta/180)*Math.cos(Math.PI*phi/180);
		sphere[1] = Math.sin(Math.PI*theta/180)*Math.cos(Math.PI*phi/180);
		sphere[2] = Math.sin(Math.PI*phi/180);
	}
	
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
	
	public static void main(String[] args) {
		Decision de = new Decision();
		de.setLoc(de.graphe(8.05, 47.3, 226.88, -47.22));
		de.setMaxi(de.getLoc().length);
		de.setMaxj(de.getLoc()[0].length);
		de.setCoord(de.getLoc()[0][0].length);
		System.out.println("Maxi: "+de.getMaxi()+" Maxj: "+de.getMaxj()+" Coord:"+de.getCoord());
		
		de.programmationDynamique(15);
		
	}
}