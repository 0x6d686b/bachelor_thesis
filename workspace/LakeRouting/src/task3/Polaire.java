package task3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Polaire {

	private ArrayList<Double> knotTitle = new ArrayList<Double>();
	private ArrayList<Double[]> matrix = new ArrayList<Double[]>();
	private double[] angleTitle;
	
	public void readCSV(String filename){
		Double[] matrixLine;
		
		int nr=0;
		int cols=0;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line;

			while((line = br.readLine()) != null) {
			    String[] splittedLine = line.split(",");
		    	cols = splittedLine.length;
		    	matrixLine = new Double[cols-1];
			    if(nr==0)
			    {
			    	angleTitle = new double[cols-1];
			    	for (int i = 0; i < cols-1; i++) {
						angleTitle[i]=Double.parseDouble(splittedLine[i+1]);
					    nr=1;
					}
			    }else{
			    	knotTitle.add(Double.parseDouble((splittedLine[0])));
			    	for (int i = 0; i < cols-1; i++) {
			    		matrixLine[i] = Double.parseDouble(splittedLine[i+1]);
			    	}
			    	matrix.add(matrixLine);
			    }
			}
		}catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public double[] getAngleTitle() {
		return angleTitle;
	}
	public ArrayList<Double> getKnotTitle() {
		return knotTitle;
	}
	public ArrayList<Double[]> getMatrix() {
		return matrix;
	}

	public static void main(String[] args) {
		Polaire pol = new Polaire();
		pol.readCSV("C:\\Users\\fevzi\\Desktop\\ZHAW\\BA(furu)\\git\\lakerouting\\workspace\\LakeRouting\\Polaire.csv");
		for (int i = 0; i < pol.getAngleTitle().length; i++) {
			System.out.print(pol.getAngleTitle()[i]+" ");
			
		}
		System.out.println("");
		for (int i = 0; i < pol.getMatrix().size(); i++) {
			System.out.print(pol.getKnotTitle().get(i)+": ");
			
			for (int j = 0; j < pol.getMatrix().get(i).length; j++) {
				System.out.print(pol.getMatrix().get(i)[j]+" ");
			}
			System.out.println("");
		}
	}
	
}
