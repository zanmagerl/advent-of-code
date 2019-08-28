import java.util.*;
import java.io.*;

public class Day14 extends AdventOfCode{

	private String input;

	@Override
	protected Object partOne(){

		int input = Integer.parseInt(this.input);

		ArrayList<Integer> recipes = new ArrayList<>();

		recipes.add(3);
		recipes.add(7);

		String x = "077201";

		int indexFirst = 0;
		int indexSecond = 1;

		for(int i = 2; i < input + 10; i++){

			int sum = recipes.get(indexFirst) + recipes.get(indexSecond);

			if(sum < 10){
				recipes.add(sum);
			}else{
				recipes.add(sum / 10);
				recipes.add(sum % 10);
			}

			indexFirst = (indexFirst + (recipes.get(indexFirst) + 1)) % recipes.size();
			indexSecond = (indexSecond + (recipes.get(indexSecond) + 1)) % recipes.size();

		}

		String result = "";
		for(int i = input; i < input + 10; i++){
			result += Integer.toString(recipes.get(i));
		}

		return result;
	}

	@Override
	protected Object partTwo(){

		int[] recipes = new int[30000000];

		recipes[0] = 3;
		recipes[1] = 7;

		StringBuilder result = new StringBuilder();

		int indexFirst = 0;
		int indexSecond = 1;

		int numberOfElements = 2;

		for(int i = 2; i < 30000000; i++){

			int sum = recipes[indexFirst] + recipes[indexSecond];

			if(sum < 10){
				recipes[i] = (sum);
				result.append(sum);
				numberOfElements++;
			}else{
				recipes[i] = (sum / 10);
				result.append(sum/10);

				recipes[i+1] = (sum % 10);
				result.append(sum%10);

				i++;
				numberOfElements+=2;
			}

			indexFirst = (indexFirst + (recipes[indexFirst] + 1)) % numberOfElements;
			indexSecond = (indexSecond + (recipes[indexSecond] + 1)) % numberOfElements;

		}

		return result.indexOf(this.input);
	}

	@Override
	protected void parseInput(String inputFile){

		try{

			Scanner sc = new Scanner(new File(inputFile));

			this.input = sc.next();

		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
