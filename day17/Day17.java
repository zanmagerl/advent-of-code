import java.util.*;
import java.io.*;

public class Day17 extends AdventOfCode{

	@Override
	protected Object partOne(){

		matrix[1][500] = '|';
		recurse(500, 1);

		return countAllWaterTiles();
	}


	@Override
	protected Object partTwo(){

		return countStagnantWaterTiles();
	}

	private int countStagnantWaterTiles(){

		int result = 0;

		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix[i].length; j++){
				if(matrix[i][j] == '~'){
					if(i >= minY)
						result++;
				}
			}
		}

		return result;
	}

	private int countAllWaterTiles(){

		int result = 0;

		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix[i].length; j++){
				if(matrix[i][j] == '~' || matrix[i][j] == '|'){
					if(i >= minY)
						result++;
				}
			}
		}

		return result;
	}

	private boolean isSupported(int x, int y){

		if(x < 0 || x >= matrix[y].length || matrix[y+1][x] == '.'){
			return false;
		}
		return checkRight(x+1, y) && checkLeft(x-1, y);
	}

	private boolean checkRight(int x, int y){
		if(x < 0 || x >= matrix[y].length || matrix[y+1][x] == '.'){
			return false;
		}
		if(matrix[y][x] == '#'){
			return true;
		}
		return checkRight(x+1, y);
	}
	private boolean checkLeft(int x, int y){
		if(x < 0 || x >= matrix[y].length || matrix[y+1][x] == '.'){
			return false;
		}
		if(matrix[y][x] == '#'){
			return true;
		}
		return checkLeft(x-1, y);
	}

	private void recurse(int x, int y){

		if(	x< 0 ||
			y < 0 ||
			x >= matrix[0].length ||
			y >= matrix.length ||
			matrix[y][x] == '#' ||
			y+1 > maxY){
			return;
		}
		if(matrix[y][x] == '|' && matrix[y+1][x] == '.'){
			matrix[y+1][x] = '|';
			recurse(x, y+1);
		}
		if(matrix[y][x] == '|' &&
			((matrix[y+1][x] == '#') || matrix[y+1][x] == '~' || isSupported(x,y))){
				if(isSupported(x, y)){
					matrix[y][x] = '~';
				}else{
					if(matrix[y][x+1] == '.'){
						matrix[y][x+1] = '|';
						recurse(x+1, y);
					}
					if(matrix[y][x-1] == '.'){
						matrix[y][x-1] = '|';
						recurse(x-1, y);
					}
				}
		}
		if(matrix[y][x] == '~'){
			if(matrix[y][x+1] == '.' || matrix[y][x+1] == '|'){
				matrix[y][x+1] = '~';
				recurse(x+1, y);
			}
			if(matrix[y][x-1] == '.'|| matrix[y][x+1] == '|'){
				matrix[y][x-1] = '~';
				recurse(x-1, y);
			}
		}
	}

	private void write(){
		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix[i].length; j++){
				System.out.print(matrix[i][j]);
			}
			System.out.println();
		}
	}

	private void fill(){
		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix[i].length; j++){
				(matrix[i][j]) = '.';
			}
		}
	}

	private char[][] matrix;
	/*
	Watch out for min and max values
	*/
	int maxY = 0;
	int minY = Integer.MAX_VALUE;
	@Override
	protected void parseInput(String inputFile){

		matrix = new char[2000][2000];
		fill();


		try{

			Scanner sc = new Scanner(new File(inputFile));

			while(sc.hasNextLine()){
				String fC = sc.next();
				char[] firstCoordianate = fC.toCharArray();
				int number = 0;
				boolean isX = false;

				for(int i = 0; i < firstCoordianate.length; i++){
					if(firstCoordianate[i] == 'x'){
						isX = true;
					}
					if(firstCoordianate[i] >= 48 && firstCoordianate[i] <= 57){
						number*= 10;
						number += firstCoordianate[i] - 48;
					}
				}

				String sC = sc.next();
				int beginning = 0;
				int end = 0;
				char[] secondCoordinate = sC.toCharArray();
				boolean jeKonecZac = false;

				for(int i = 0; i < secondCoordinate.length; i++){
					if(secondCoordinate[i] == '.'){
						jeKonecZac = true;
					}
					if(secondCoordinate[i] >= 48 && secondCoordinate[i] <= 57){
						if(!jeKonecZac){
							beginning *= 10;
							beginning += secondCoordinate[i] - 48;
						}else{
							end *= 10;
							end += secondCoordinate[i] - 48;
						}
					}
				}

				if(isX){
					for(int i = beginning; i <= end; i++){
						if(i < minY){
							minY = i;
						}
						if(i  > i){
							maxY = number;
						}
						matrix[i][number] = '#';
					}
				}else{
					if(number < minY){
						minY = number;
					}
					if(number  > maxY){
						maxY = number;
					}
					for(int i = beginning; i <= end; i++){
						matrix[number][i] = '#';
					}
				}
			}
			matrix[0][500] = '+';

		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
