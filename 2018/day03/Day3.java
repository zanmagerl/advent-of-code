import java.util.*;
import java.io.*;
import java.awt.*;

public class Day3 extends AdventOfCode{

	private ArrayList<Rect> data;

	private static class Rect extends Rectangle{
	
		public Rect(int x, int y, int width, int height){
			super(x,y,width,height);
		}
	
	}

	private int checkCollision(Rect a, Rect b){
		
		int numberOfCollisions = 0;

		for(int i = b.x; i < b.x + b.width; i++){
			for(int j = b.y; j < b.y + b.height; j++){

				if(a.contains(i, j)){
					points.add(i+","+j);
					numberOfCollisions++;
				}

			}
		}
		return numberOfCollisions;
	}

	Set<String> points;

	@Override
	protected Object partOne(){
		
		points = new HashSet<>();

		for(int i = 0; i < data.size(); i++){

			for(int j = i+1; j < data.size(); j++){

				checkCollision(data.get(i), data.get(j));

			}
		}

		return points.size();
		
	}

	@Override
	protected Object partTwo(){

		points = new HashSet<>();
		
		for(int i = 0; i < data.size(); i++){
			
			int numberOfCollisions = 0;

			for(int j = 0; j < data.size(); j++){
				
				if(j != i)
					numberOfCollisions += checkCollision(data.get(i), data.get(j));

			}
			if(numberOfCollisions == 0){
				return i+1;
			}
		}

		return null;
	}

	@Override
	protected void parseInput(String inputFile){
		
		data = new ArrayList<>();

		try{

			Scanner sc = new Scanner(new File(inputFile));
			
			while(sc.hasNext()){
				sc.next();
				sc.next();
				String[] a = sc.next().split(",");
				String[] b = sc.next().split("x");
				a[1] = a[1].substring(0, a[1].length()-1);
				//System.out.println(a[1]);
				Rect rect = new Rect(Integer.parseInt(a[0]), Integer.parseInt(a[1]),
					Integer.parseInt(b[0]), Integer.parseInt(b[1]));
				data.add(rect);
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}