import java.util.*;
import java.io.*;

public class Day20 extends AdventOfCode{

	char[] instructions;

	char[][] matrix = new char[10000][10000];
	int positionX = matrix[0].length/2;
	int positionY = matrix.length;

	@Override
	protected Object partOne(){

		findShortestPath(5000, 5000, 1, -1);

		return mostDistantRoom;
	}


		@Override
		protected Object partTwo(){

			findNumberOfRooms(5000, 5000, -1, 0);

			return numberOfRooms;
		}

	int mostDistantRoom = 0;

	//up right down, left
	int[][] vector = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};

	private void findShortestPath(int x, int y, int numberOfRooms, int forbiddenDirection){
		if(matrix[y][x] == '#'){
			return;
		}
		if(numberOfRooms > mostDistantRoom){
			mostDistantRoom = numberOfRooms;
		}
		if(matrix[y][x] == '.'){
			numberOfRooms++;
		}
		for(int i = 0; i < vector.length; i++){
			if(i != forbiddenDirection)
				findShortestPath(x + vector[i][0], y + vector[i][1], numberOfRooms, (i + 2) % 4);
		}
	}

	private int numberOfRooms = 0;

	private void findNumberOfRooms(int x, int y, int forbiddenDirection, int doors){

		if(matrix[y][x] == '#'){
			return;
		}
		if(matrix[y][x] == '.'){
			doors++;
			if(doors >= 1000){
				numberOfRooms++;
			}
		}

		for(int i = 0; i < vector.length; i++){
			if(i != forbiddenDirection){
				findNumberOfRooms(x + vector[i][0], y + vector[i][1], (i + 2) % 4, doors);
			}
		}
	}

	private int[] recurse(int positionX, int positionY, int i){
		for( ; i < instructions.length; i++){
			char c = instructions[i];
			if(c == ')' || c == '|'){
				return new int[]{i, c == '|' ? 1 : 0, positionX, positionY};
			}
			//Is letter
			if(c >= 65 && c <= 90){
				int[] direction = vectorize(c);
				//then we have horizontal doors
				if(direction[0] == 0){
					matrix[positionY + direction[1]][positionX + direction[0]] = '-';
					matrix[positionY + direction[1]][positionX + direction[0] + 1] = '#';
					matrix[positionY + direction[1]][positionX + direction[0] - 1] = '#';
				}else{
					matrix[positionY + direction[1]][positionX + direction[0]] = '|';
					matrix[positionY + direction[1] + 1][positionX + direction[0]] = '#';
					matrix[positionY + direction[1] - 1][positionX + direction[0]] = '#';
				}
				positionX = positionX + 2*direction[0];
				positionY = positionY + 2*direction[1];
				matrix[positionY][positionX] = '.';
			}
			else if(c == '('){
				int[] result = recurse(positionX, positionY, i+1);
				while(result[1] == 1){
					result = recurse(positionX, positionY, result[0]+1);
				}
				i = result[0];
				positionX = result[2];
				positionY = result[3];
			}
		}
		return null;
	}

	private int[] vectorize(char a){
		if(a == 'N'){
			return new int[]{0, -1};
		}
		if(a == 'E'){
			return new int[]{1, 0};
		}
		if(a == 'S'){
			return new int[]{0, 1};
		}
		return new int[]{-1, 0};
	}

	@Override
	protected void parseInput(String inputFile){

		try{

			Scanner sc = new Scanner(new File(inputFile));

			String input = sc.nextLine();
			input = input.substring(1, input.length()-1);
			instructions = input.toCharArray();

			matrix[5000][5000] = 'X';

			recurse(5000, 5000, 0);

			for(int i = 3000; i < 8000; i++){
				for(int j = 3000; j < 8000; j++){
					if(matrix[i][j] == '\u0000'){
						matrix[i][j] = '#';
					}
				}
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
