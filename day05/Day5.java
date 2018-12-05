import java.util.*;
import java.io.*;
import java.awt.*;

public class Day5 extends AdventOfCode{



	@Override
	protected Object partOne(){
		
		return partOne(listPartOne);
	}

	protected Object partOne(ArrayList<Character> data){
		for(int i = 0; i < data.size()-1; i++){
			if(Math.abs(data.get(i) - data.get(i+1)) == 32){
				
				data.remove(i);
				data.remove(i);
				
				i-=2;
				
				if(i < 0){
					i = -1;
				}
			}
		}

		return data.size();
	}

	@Override
	protected Object partTwo(){

		int minLength = Integer.MAX_VALUE;;
		
		for(int i = 0; i <= 25; i++){

			ArrayList<Character> temp = new ArrayList<>();
			
			for(int j = 0; j < listPartTwo.size(); j++){
				if(listPartTwo.get(j) != 65+i && listPartTwo.get(j) != 97+i){
					temp.add(listPartTwo.get(j));
				}
			}

			Integer size = (Integer)partOne(temp);
			
			if(size < minLength){
				minLength = size;
			}
			 
		}
		return minLength;
	}

	private ArrayList<Character> listPartOne;
	private ArrayList<Character> listPartTwo;

	@Override
	protected void parseInput(String inputFile){

		try{

			Scanner sc = new Scanner(new File(inputFile));
			
			String input = sc.next();
			char[] inputInChars = input.toCharArray();

			listPartOne = new ArrayList<>();
			listPartTwo = new ArrayList<>();
			for(char c : inputInChars){
				listPartOne.add(c);
				listPartTwo.add(c);
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}