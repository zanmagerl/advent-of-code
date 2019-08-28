import java.util.*;
import java.io.*;

public class Day25 extends AdventOfCode{

	private static class Star{
		int x;
		int y;
		int z;
		int t;

		public Star(int x, int y, int z, int t){
			this.x = x;
			this.y = y;
			this.z = z;
			this.t = t;
		}

		public String toString(){
			return this.x + " " + this.y + " " + this.z + " " + this.t;
		}
	}


	private static class Constellation{

		ArrayList<Star> stars;

		public Constellation(){
			stars = new ArrayList<>();
		}
	}

	ArrayList<Constellation> constelations;

	@Override
	protected Object partOne(){

		constelations = new ArrayList<>();

		boolean added = true;
		
		while(stars.size() > 0){
			constelations.add(new Constellation());
			Constellation c = constelations.get(constelations.size()-1);
			constelations.get(constelations.size()-1).stars.add(stars.get(0));
			stars.remove(stars.get(0));
			added = true;
			while(added){

				added = false;

				for(int i = 0; i < stars.size(); i++){

					for(Star s : c.stars){

						if(manhattan(s, stars.get(i)) <= 3){
							c.stars.add(stars.get(i));
							stars.remove(stars.get(i));
							i--;
							added = true;
							break;
						}
					}
				}
			}
		}
		return constelations.size();
	}

	private int manhattan(Star a, Star b){
		return (Math.abs(a.x - b.x) + Math.abs(a.y - b.y) + Math.abs(a.z - b.z)
				+ Math.abs(a.t - b.t));
	}

	@Override
	protected Object partTwo(){

		return "Done!";
	}

	ArrayList<Star> stars;

	@Override
	protected void parseInput(String inputFile){

		stars = new ArrayList<>();

		try{

			Scanner sc = new Scanner(new File(inputFile));
			while(sc.hasNextLine()){
				String[] line = sc.nextLine().split(",");
				stars.add(new Star(Integer.parseInt(line[0]),
				Integer.parseInt(line[1]), Integer.parseInt(line[2]),
				Integer.parseInt(line[3])));
			}


		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
