import java.util.*;
import java.io.*;

public class Day7 extends AdventOfCode{

	private static class Point{
		
		char id;
		ArrayList<Character> condition = new ArrayList<>();;
		
		public Point(char id, char p){
			this.id = id;
			this.condition.add(p);

		}
	}

	private ArrayList<Character> findRoots(){
		
		Set<Character> ids = new HashSet<>();
		Set<Character> condition = new HashSet<>();

		for(Point p : points){
			ids.add(p.id);
			for(int i = 0; i < p.condition.size(); i++){
				condition.add(p.condition.get(i));
			}
		}

		ArrayList<Character> roots = new ArrayList<>();

		for(Character p : condition){
			if(!ids.contains(p)){
				roots.add(p);
			}
		}

		return roots;
	}

	@Override
	protected Object partOne(){

		ArrayList<Character> roots = findRoots();

		for(int i = 0; i < points.size()-1; i++){

			for(int j = i+1; j < points.size(); j++){
				
				if(points.get(i).id > points.get(j).id){
					
					char id = points.get(i).id;
					ArrayList<Character> conditions = points.get(i).condition;
					
					points.get(i).id = points.get(j).id;
					points.get(i).condition = points.get(j).condition;
					
					points.get(j).id =  id;
					points.get(j).condition = conditions;
				
				}
			}
		}

		ArrayList<Character> alreadyDone = new ArrayList<>();

		int numberOfSteps = points.size() + roots.size();
		
		Character root = roots.iterator().next();
		alreadyDone.add(root);
		roots.remove(root);
		
		int last = 0;

		while(numberOfSteps != alreadyDone.size()){
			
			for(int i = 0; i < points.size(); i++){

				if(!roots.isEmpty() && points.get(i).id > roots.iterator().next()){
					alreadyDone.add(roots.iterator().next());
					roots.remove(roots.iterator().next());
					break;
				}
				boolean metConditions = true;
				for(Character c : points.get(i).condition){
					if(!alreadyDone.contains(c)){
						metConditions = false;
						break;
					}
				}
				if(metConditions){
					alreadyDone.add(points.get(i).id);
					points.remove(i);
					break;
				}

				
			}
			if(alreadyDone.size() == last){
				alreadyDone.add(roots.iterator().next());
				roots.remove(roots.iterator().next());
			}
			last = alreadyDone.size();
			
		}
		
		
		String result = ""; 
		for(int i = 0; i < alreadyDone.size(); i++){
			result += alreadyDone.get(i);
		}
		return result;
	}

	@Override
	protected Object partTwo(){

		int numberOfWorkers = 5;

		points = copyOfPoints;
		ArrayList<Character> roots = findRoots();

		for(int i = 0; i < points.size()-1; i++){
			for(int j = i+1; j < points.size(); j++){
				if(points.get(i).id > points.get(j).id){
					char id = points.get(i).id;
					ArrayList<Character> conditions = points.get(i).condition;

					points.get(i).id = points.get(j).id;
					points.get(i).condition = points.get(j).condition;
					
					points.get(j).id = id;
					points.get(j).condition = conditions;
				}
			}
		}

		int time = 0;

		int numberOfSteps = points.size() + roots.size();

		ArrayList<Character> alreadyDone = new ArrayList<>();
		ArrayList<Character> inProgress = new ArrayList<>();
		ArrayList<Integer> timesOfThoseinProgress = new ArrayList<>();
		
		int occupiedWorkers = 0;
		
		while(!roots.isEmpty()){
			inProgress.add((roots.iterator().next()));
			timesOfThoseinProgress.add(roots.iterator().next()+ 60 - 64);
			occupiedWorkers++;
			roots.remove(roots.iterator().next());
		}
		int last = 0;
			
		boolean cycle = false;
		int cycleSum = 0;
		while(numberOfSteps != alreadyDone.size()){
			
			if(occupiedWorkers == numberOfWorkers || cycle){
				
				int iMin = 0;
				int min = Integer.MAX_VALUE;
				
				for(int  i = 0; i < timesOfThoseinProgress.size(); i++){
					if(timesOfThoseinProgress.get(i) < min){
						min = timesOfThoseinProgress.get(i);
						iMin = i;
					}
				}

				occupiedWorkers--;
				alreadyDone.add(inProgress.get(iMin));
				inProgress.remove(iMin);
				timesOfThoseinProgress.remove(iMin);
				
				time += min;
				
				for(int i = 0; i < timesOfThoseinProgress.size(); i++){
					timesOfThoseinProgress.set(i, timesOfThoseinProgress.get(i) - min);
				}
				cycle = false;
			}

			for(int i = 0; i < points.size(); i++){

				boolean metConditions = true;
				for(Character c : points.get(i).condition){
					if(!alreadyDone.contains(c)){
						metConditions = false;
						break;
					}
				}
				if(metConditions){
					inProgress.add((points.get(i).id));
					timesOfThoseinProgress.add(points.get(i).id+ 60 - 64);
					occupiedWorkers++;
					points.remove(i);
					break;
				}
			
			}
			if(cycleSum == 10*alreadyDone.size() + inProgress.size() && inProgress.size() != 0){
				cycle = true;
			}
			
			cycleSum = 10*alreadyDone.size() + inProgress.size();

		}
		
		return time;
	}

	ArrayList<Point> points;
	ArrayList<Point> copyOfPoints;

	@Override
	protected void parseInput(String inputFile){

		try{

			Scanner sc = new Scanner(new File(inputFile));
			
			points = new ArrayList<>();
			copyOfPoints = new ArrayList<>();

			while(sc.hasNextLine()){
				String line = sc.nextLine();

				String[] a = line.split(" ");

				char id = a[7].toCharArray()[0];
				char condition = a[1].toCharArray()[0];

				Point p = new Point(id, condition);
				
				boolean exists = false;

				for(int i = 0; i < points.size(); i++){
					if(points.get(i).id == p.id){
						points.get(i).condition.add(condition);	
						copyOfPoints.get(i).condition.add(condition);	
						exists = true;
					}
				}
				if(!exists){
					points.add(p);	
					copyOfPoints.add(p);
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}