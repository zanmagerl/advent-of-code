import java.util.*;
import java.io.*;

public class Day11 extends AdventOfCode{

	private int input;
	private int[][] table;

	private int hundreds(int n){
		n /= 100;
		return n % 10;
	}

	private void fill(){

		table = new int[302][302];
		
		for(int y = 1; y <= 300; y++){
			for(int x = 1; x <= 300; x++){
				
				int rackID = x + 10;
				int powerLevel = y*rackID;
				powerLevel += input;
				powerLevel *= rackID;
				powerLevel = hundreds(powerLevel);
				table[y][x] = powerLevel - 5;
			}
		}
	}

	@Override
	protected Object partOne(){

		fill();

		int xMax = 0;
		int yMax = 0;
		int max = 0;
		
		for(int i = 1; i <= 300-2; i++){
			for(int j = 1; j <= 300-2; j++){
				
				int sum = 0;

				for(int a = i; a < i+3; a++){
					for(int b = j; b < j+3; b++){
						sum += table[a][b];
					}
				}

				if(sum > max){
					max = sum;
					xMax = j;
					yMax = i;
				}

			}
		}

		return String.format("%d,%d", xMax, yMax);
	}

	@Override
	protected Object partTwo(){
		
		int xMax = 0;
		int yMax = 0;
		int sizeMax = 0;
		int max = 0;
		
		for(int s = 1; s <= 300; s++){
			for(int i = 1; i <= 300 - (s-1); i++){
				for(int j = 1; j <= 300 - (s-1); j++){
			
					int sum = 0;

					for(int a = i; a < i + s; a++){
						for(int b = j; b < j + s; b++){
							sum += table[a][b];
						}

					}

					if(sum > max){
						max = sum;
						xMax = j;
						yMax = i;
						sizeMax = s;
					}

				}
			}
		}
		
		return String.format("%d,%d,%d", xMax, yMax, sizeMax);
	}

	@Override
	protected void parseInput(String inputFile){
			
		try{

			Scanner sc = new Scanner(new File(inputFile));
				
			input = sc.nextInt();

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}