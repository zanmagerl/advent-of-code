import java.util.*;
import java.io.*;

public class Day22 extends AdventOfCode{

	@Override
	protected Object partOne(){

		char[][] matrix = new char[targetY+1][targetX+1];

		int[][] geologicalIndex = new int[matrix.length][matrix[0].length];
		int[][] erosionLevel = new int[matrix.length][matrix[0].length];

		setGeologicalIndex(geologicalIndex, erosionLevel);
		setErosionLevel(erosionLevel, geologicalIndex);

		return sum(erosionLevel);
	}


	private void print(int[][] t){
		for(int i = 0; i < t.length; i++){
			for(int j = 0; j < t[0].length; j++){
				System.out.print(t[i][j] + " ");
			}
			System.out.println();
		}
	}
	private void print(char[][] t){
		for(int i = 0; i < t.length; i++){
			for(int j = 0; j < t[0].length; j++){
				System.out.print(t[i][j]);
			}
			System.out.println();
		}
	}

	private int sum(int[][] erosionLevel){
		int result = 0;
		for(int i = 0; i < erosionLevel.length; i++){
			for(int j = 0; j < erosionLevel[i].length; j++){
				result += erosionLevel[i][j];
			}
		}
		return result;
	}

	private void fill(char[][] matrix, int[][] erosionLevel){
		for(int i = 0; i < erosionLevel.length; i++){
			for(int j = 0; j < erosionLevel[i].length; j++){
				switch(erosionLevel[i][j]){
					case(0):
						matrix[i][j] = '.';
						break;
					case(1):
						matrix[i][j] = '=';
						break;
					case(2):
						matrix[i][j] = '|';
						break;
				}
			}
		}
	}

	private void setErosionLevel(int[][] erosionLevel, int[][] geologicalIndex){
		for(int i = 0; i < erosionLevel.length; i++){
			for(int j = 0; j < erosionLevel[i].length; j++){
				erosionLevel[i][j] = erosionLevel[i][j] % 3;
			}
		}
	}

	private void setGeologicalIndex(int[][] geologicalIndex, int erosionLevel[][]){

		for(int i = 0; i < geologicalIndex.length; i++){
			for(int j = 0; j < geologicalIndex[i].length; j++){
				if(i == 0 && j == 0 || i == targetY && j == targetX){
					geologicalIndex[i][j] = 0;
				}
				else if(i == 0){
					geologicalIndex[i][j] = j*16807;
				}
				else if(j == 0){
					geologicalIndex[i][j] = i*48271;
				}else{
					geologicalIndex[i][j] = erosionLevel[i][j-1]*erosionLevel[i-1][j];
				}
				erosionLevel[i][j] = (geologicalIndex[i][j] + depth) % 20183;
			}
		}
	}

	String[] gear = {"neither", "torch", "climbing gear"};

	private boolean cave(char c, String equippedTool){
		if(c == '.' && equippedTool == gear[0]){
			return false;
		}
		if(c == '=' && equippedTool == gear[1]){
			return false;
		}
		if(c == '|' && equippedTool == gear[2]){
			return false;
		}
		return true;
	}

	private String equip(char currentCave, char nextCave, String equippedTool){
		for(String tool : gear){
			if(tool != equippedTool && cave(currentCave, tool) && cave(nextCave, tool)){
				return tool;
			}
		}
		return null;
	}

	@Override
	protected Object partTwo(){
		char[][] matrix = new char[1000][1000];

		int[][] geologicalIndex = new int[matrix.length][matrix[0].length];
		int[][] erosionLevel = new int[matrix.length][matrix[0].length];

		String equippedTool = gear[1];

		setGeologicalIndex(geologicalIndex, erosionLevel);
		setErosionLevel(erosionLevel, geologicalIndex);
		fill(matrix, erosionLevel);
		
		Object[] obj = {0, 0, equippedTool, 0, 0};

		Queue<Object[]> q  = new LinkedList<>();
		Map<String, Integer> visited = new HashMap<>();

		q.add(obj);

		int min = Integer.MAX_VALUE;

		while(q.peek() != null){

			Object[] temp = q.remove();

			String visit = (Integer)temp[0] + "," + (Integer)temp[1] +  "," + temp[2];
			if(visited.containsKey(visit) && (Integer)temp[3] >=  visited.get(visit)){
				continue;
			}
			visited.put(visit, (Integer)temp[3]);
			
			if((Integer)temp[0] == targetX && (Integer)temp[1] == targetY && temp[2] == gear[1]){
				if((Integer)temp[3] < min){
					min = (Integer)temp[3];
				}
			}

			String e = equip(matrix[(Integer)temp[1]][(Integer)temp[0]], matrix[(Integer)temp[1]][(Integer)temp[0]], (String)temp[2]);

			Object[] ob = {(Integer)temp[0], (Integer)temp[1], e, (Integer)temp[3]+7, (Integer)temp[4]+1};
			q.add(ob);

			for(int i = 0; i < vector.length; i++){

				int x = (Integer)temp[0] + vector[i][0];
				int y = (Integer)temp[1] + vector[i][1];

				if(y < 0 || x < 0 || x >= matrix[0].length || y >= matrix.length){
					continue;
				}

				if(cave(matrix[y][x], (String)temp[2])){
					Object[] o = {x, y, temp[2], ((Integer)temp[3])+1, temp[4]};
					q.add(o);
				}
			}
		}

		return min;
	}
	int[][] vector = {{0,1},{1, 0},{0,-1},{-1,0}};

	private int depth;
	private int targetX;
	private int targetY;

	@Override
	protected void parseInput(String inputFile){

		try{

			Scanner sc = new Scanner(new File(inputFile));

			depth = Integer.parseInt(sc.nextLine().split(" ")[1]);

			String line = sc.nextLine().split(" ")[1];
			targetX = Integer.parseInt(line.split(",")[0]);
			targetY = Integer.parseInt(line.split(",")[1]);

		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
