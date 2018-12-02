import java.util.*;
import java.io.*;

public class Day2 extends AdventOfCode{

	ArrayList<String> list;

	@Override
	protected Object partOne(){

		int numberOfTwos = 0;
		int numberOfThrees = 0;
		
		for(int i = 0; i < list.size(); i++){

			char[] letters = list.get(i).toCharArray();

			int[] counterOfLetters = new int[31];

			for(char x : letters){
				counterOfLetters[(int)(x - 'a')]++;
			}

			boolean isTwo = false;
			boolean isThree = false;

			for(int x : counterOfLetters){

				if(x == 2){
					isTwo = true;
				}
				if(x == 3){
					isThree = true;
				}
			}
			
			if(isTwo){
				numberOfTwos++;
			}
			if(isThree){
				numberOfThrees++;
			}
		}

		return numberOfTwos*numberOfThrees;
	}

	@Override
	protected Object partTwo(){

		String word1 = "";
		String word2 = "";

		int difference = Integer.MAX_VALUE;

		for(int i = 0; i < list.size()-1; i++){

			for(int j = i+1; j < list.size(); j++){

				int localDifference = differ(list.get(i), list.get(j));

				if(localDifference < difference){
					difference = localDifference;
					word1 = list.get(i);
					word2 = list.get(j);
				}
			}
		}

		return sameLetters(word1, word2);
	}

	private String sameLetters(String a, String b){
		
		String result = "";

		for(int i = 0; i < a.length(); i++){
			if(a.charAt(i) == b.charAt(i)){
				result += a.charAt(i);
			}
		}
		return result;
	}


	private int differ(String a, String b){
		
		int result = 0;
		
		for(int i = 0; i <a.length(); i++){
			if(a.charAt(i) != b.charAt(i)){
				result++;
			}
		}
		return result;
	}

	@Override
	protected void parseInput(String inputFile){
			
		list = new ArrayList<>();

		try{

			Scanner sc = new Scanner(new File(inputFile));
			while(sc.hasNextLine()){
				String vhod = sc.nextLine();
				list.add(vhod);	
			}
			

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}