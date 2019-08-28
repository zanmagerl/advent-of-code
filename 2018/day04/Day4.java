import java.util.*;
import java.io.*;
import java.awt.*;

public class Day4 extends AdventOfCode{

	ArrayList<Guard> guards;
	ArrayList<String> data;
	
	private static class Guard{
		
		int id;

		ArrayList<String> datesOfWatch;
		ArrayList<Integer> minutes;

		int minutesOfSleep;

		public Guard(int id){
			this.id = id;
			this.datesOfWatch = new ArrayList<>();
			this.minutes = new ArrayList<>();
			this.minutesOfSleep = 0;
		}
	}

	@Override
	protected Object partOne(){

		for(Guard g : guards){

			int counter = 0;
			boolean sleeps = false;

			for(String date : g.datesOfWatch){

				int max = 0;
				
				if(counter >= g.minutes.size()){
					break;
				}
				int a = g.minutes.get(counter);

				while(a >= max){
					//System.out.println(a);
					if(sleeps){
						g.minutesOfSleep += (a - max);
						sleeps = false; 	
					}else{
						sleeps = true;;
					}
					max = a;
					counter++;
					if(counter >= g.minutes.size()){
						break;
					}
					a = g.minutes.get(counter);
				}

			}
		}

		int iMin = 0;

		for(int i = 0; i < guards.size(); i++){
			if(guards.get(i).minutesOfSleep > guards.get(iMin).minutesOfSleep){
				iMin = i;
			}
		}

		Guard sleepyHead = guards.get(iMin);

		int[] day = new int[60];
		int counter = 0;
		boolean sleeps = false;

		for(String date : sleepyHead.datesOfWatch){
		
			int max = 0;
			int a = sleepyHead.minutes.get(counter);
			while(a >= max){
				
				if(sleeps){
					for(int i = max; i < a; i++){
						day[i]++;
					}
					sleeps = false;; 	
				}else{
					sleeps = true;
				}
				max = a;
				counter++;
				if(counter >= sleepyHead.minutes.size()){
					break;
				}
				a = sleepyHead.minutes.get(counter);
			}
		}
		int jMin = 0;
		for(int i = 0; i < day.length; i++){
			if(day[i] > day[jMin]){
				jMin = i;
			}
		}

		return jMin*sleepyHead.id;
	}

	@Override
	protected Object partTwo(){

		int maxId = 0;
		int maxDay = 0;

		int maxI = 0;
		
		for(Guard g : guards){

			int[] day = new int[60];
			int counter = 0;
			boolean sleeps = false;

			for(String date : g.datesOfWatch){
			
				int max = 0;
				if(counter >= g.minutes.size()){
					break;
				}
				
				int a = g.minutes.get(counter);
				
				while(a >= max){

					if(sleeps){
						
						for(int i = max; i < a; i++){
							day[i]++;
						}
						sleeps = false;; 	
					}else{
						sleeps = true;
					}
					max = a;
					counter++;
					
					if(counter >= g.minutes.size()){
						break;
					}
					
					a = g.minutes.get(counter);
				}
			}

			for(int i = 0; i < day.length; i++){
				if(day[i] > maxDay){
					maxDay = day[i];
					maxId = g.id;
					maxI = i;
				}
			}
		}



		return maxI*maxId;
	}
	
	@Override
	protected void parseInput(String inputFile){

		try{

			data = new ArrayList<>();

			Scanner sc = new Scanner(new File(inputFile));
			
			String line;
			while(sc.hasNextLine()){
				line = sc.nextLine();
				data.add(line);
			}

			Collections.sort(data);

			guards = new ArrayList<>();

			for(int record = 0; record < data.size(); record++){
				String s = data.get(record);
				if(s.contains("#")){
					
					String a = s;
					a = a.substring(s.indexOf("#")+1);
					int id = Integer.parseInt(a.split(" ")[0]);
					
					Guard g = null;
						
					boolean exists = false;
						
					for(int i = 0; i < guards.size(); i++){
						if(guards.get(i).id == id){
							g = guards.get(i);
							g.datesOfWatch.add(s.substring(1, 17));
							exists = true;	
						}
					}
					if(!exists){
						g = new Guard(id);
						g.datesOfWatch.add(s.substring(1, 17));
						guards.add(g);
					}

					record++;
				
					String nextLine = data.get(record);
				
					while(!nextLine.contains("#")){

						if(nextLine.substring(19,24).equals("falls")){
							int startOfSleep = Integer.parseInt(nextLine.substring(15,17));
							g.minutes.add(startOfSleep);	
						}

						else if(nextLine.substring(19,24).equals("wakes")){
							int endOfSleep = Integer.parseInt(nextLine.substring(15,17));
							g.minutes.add(endOfSleep);
						}

						record++;
						
						if(record >= data.size()){
							break;
						}

						nextLine = data.get(record);
					}
					record--;
				}
			}


		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}