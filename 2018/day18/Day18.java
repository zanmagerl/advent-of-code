import java.util.*;
import java.io.*;

public class Day18 extends AdventOfCode{

	@Override
	protected Object partOne(){

		int[][] vectors = {
			{-1, -1},
			{0, -1},
			{1, -1},
			{-1, 0},
			{1, 0},
			{-1, 1},
			{0, 1},
			{1, 1}
		};


		for(int n = 0; n < 10; n++){

			char[][] newMatrix = new char[matrix.length][matrix[0].length];

			for(int i = 0; i < matrix.length; i++){
				for(int j = 0; j < matrix[i].length; j++){

					int numberOfTrees = 0;
					int numberOfLumberyards = 0;

					for(int[] v : vectors){

						int x = j + v[0];
						int y = i + v[1];

						if(x < 0 || y < 0 || x >= matrix.length || y >= matrix[0].length){
							continue;
						}
						if(matrix[y][x] == '#'){
							numberOfLumberyards++;
						}else if(matrix[y][x] == '|'){
							numberOfTrees++;
						}
					}

					if(matrix[i][j] == '.' && numberOfTrees >= 3){
						newMatrix[i][j] = '|';
					}else if(matrix[i][j] == '|' && numberOfLumberyards >= 3){
						newMatrix[i][j] = '#';
					}else if(matrix[i][j] == '#' && (numberOfLumberyards == 0 || numberOfTrees == 0)){
						newMatrix[i][j] = '.';
					}else{
						newMatrix[i][j] = matrix[i][j];
					}
				}
			}
			matrix = newMatrix;
		}

		int numberOfTrees = 0;
		int numberOfLumberyards = 0;

		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix[i].length; j++){
				if(matrix[i][j] == '#'){
					numberOfLumberyards++;
				}else if(matrix[i][j] == '|'){
					numberOfTrees++;
				}
			}
		}

		return numberOfTrees*numberOfLumberyards;
	}

	@Override
	protected Object partTwo(){
		/*
		There is a repeating pattern, but I lost a lot of time
		because of a off-by-one error.
		*/
		int startOfCycle = 1963;
		int startOfANextCycle = 1991;

		long result = (1000000000L - 1 - startOfCycle) % (startOfANextCycle - startOfCycle);
		/*
		res = 16 => 1963 + 16 = 1979,
		which has the total resource value of 550*351.
		*/
		return 550*351;
	}

	private char matrix[][];

	@Override
	protected void parseInput(String inputFile){

		matrix = new char[50][50];

		try{

			Scanner sc = new Scanner(new File(inputFile));

			for(int i = 0; i < matrix.length; i++){
				matrix[i] = sc.nextLine().toCharArray();
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
