import java.util.*;
import java.io.*;

public class Day6 extends AdventOfCode{
	
	private int[][] matrix;
	ArrayList<Point> points;
	
	private static class Point{

		int id;
		int x;
		int y;

		int area = 0;

		public Point(int id, int x, int y){
			this.id = id;
			this.x = x;
			this.y = y;
		}
	}

	private int manhattan(int x, int y, int x1, int y1){
		return (int)(Math.abs(x - x1) + Math.abs(y - y1));
	}

	private int closestsID(int x, int y){
		int min = Integer.MAX_VALUE;
		int idpoints = 0;
		boolean vsajDva = false;
		Point p = null;
		for(Point t : points){
			int r = manhattan(x, y, t.x, t.y); 
			if(r < min){
				min = r;
				idpoints = t.id;
				p = t;
				vsajDva = false;
			}
			else if(r == min){
				vsajDva = true;
			}
		
		}
		if(vsajDva){
			return 0;
		}
		p.area++;
		return idpoints;
	}

	private int cumulativeDistance(int x, int y){
		int result = 0;
		for(Point t : points){
			result += manhattan(x, y, t.x, t.y);
			if(result > 10000){
				break;
			}
		}
		return result;
	}

	@Override
	protected Object partOne(){

		matrix = new int[1000][1000];

		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix[i].length; j++){

				matrix[i][j] = closestsID(i, j);

			}
		}

		for(int i = 0; i < matrix.length; i++){

			if(i == 0 || i == matrix.length - 1){
				for(int j = 0; j < matrix[i].length; j++){
					int id = matrix[i][j];
					for(Point t : points){
						if(t.id == id){
							t.area = 0;
							break;
						}
					}
				}
			}else{
				int id = matrix[i][0];
				for(Point t : points){
					if(t.id == id){
						t.area = 0;
						break;
					}
				}
				id = matrix[i][matrix.length - 1];
				for(Point t : points){
					if(t.id == id){
						t.area = 0;
						break;
					}
				}
			}

		}
		int iMin = 0;
		for(int i = 0; i < points.size(); i++){
			if(points.get(iMin).area < points.get(i).area){
				iMin = i;
			}
		}

		return points.get(iMin).area;
	}

	@Override
	protected Object partTwo(){

		matrix = new int[1000][1000];

		for(Point t : points){
			matrix[t.x][t.y] = t.id;
		}

		int totalArea = 0;

		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix[i].length; j++){
				if(cumulativeDistance(i, j) < 10000){
					totalArea++;
				}
			}
		}	

		return totalArea;
	}

	@Override
	protected void parseInput(String inputFile){

		points = new ArrayList<>();

		try{

			Scanner sc = new Scanner(new File(inputFile));
			int counter = 0;
			
			while(sc.hasNextLine()){
				String line = sc.nextLine();
				String[] a = line.split(",");
				points.add(new Point(counter,  Integer.parseInt(a[0]), 
					Integer.parseInt(a[1].substring(1))));
				counter++;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}