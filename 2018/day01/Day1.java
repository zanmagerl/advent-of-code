import java.util.*;
import java.io.*;

public class Day1 extends AdventOfCode{

	private ArrayList<Integer> data;

	@Override
	protected Object partOne(){

		Integer frequency = 0;

		for(Integer x : data){
			frequency += x;
		}

		return frequency;
	}

	@Override
	protected Object partTwo(){

		int frequency = 0;

		Set<Integer> set = new HashSet<>();

		for(int i = 0; i < data.size(); i = (i+1) % data.size()){
			
			frequency += data.get(i);

			if(set.contains(frequency)){
				return frequency;
			}
			set.add(frequency);
		}

		return null;
	}

	@Override
	protected void parseInput(String inputFile){
		try{
			Scanner sc = new Scanner(new File(inputFile));
			data = new ArrayList<>();

			while(sc.hasNextInt()){
				data.add(sc.nextInt());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}