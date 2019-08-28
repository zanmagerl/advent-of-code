import java.util.*;
import java.io.*;

public class Day13 extends AdventOfCode{

	private static class Cart{

		int x;
		int y;
		char direction;

		int indexOfDirection;
		// up down left right
		int[] dx = {0, 0, -1, 1};
		int[] dy = {-1, 1, 0, 0};

		int turn = 0;

		char[][] matrix;
		ArrayList<Cart> carts;

		public Cart(int x, int y, char direction){
			this.x = x;
			this.y = y;
			this.direction = direction;
		}

		private void smer(){
			if(direction == '>'){
				indexOfDirection = 3;
			}
			else if(direction == '<'){
				indexOfDirection = 2;
			}
			else if(direction == 'v'){
				indexOfDirection = 1;
			}else{
				indexOfDirection = 0;
			}
		}
		public Cart[] move(){

			smer();

			this.x += dx[indexOfDirection];
			this.y += dy[indexOfDirection];

			for(Cart c : carts){
				if(c != this && this.x == c.x && this.y == c.y){

					return new Cart[]{this, c};
				}
			}


			if(matrix[y][x] == '/'){
				if(direction == '^'){
					direction = '>';
				}
				else if(direction == '>'){
					direction = '^';
				}
				else if(direction == 'v'){
					direction = '<';
				}else{
					direction = 'v';
				}
			}

			else if(matrix[y][x] == '+'){

				if(turn == 0){
					if(direction == '^'){
						direction = '<';
					}else if(direction == 'v'){
						direction = '>';
					}else if(direction == '>'){
						direction = '^';
					}else{
						direction = 'v';
					}
				}
				else if(turn == 2){
					if(direction == '^'){
						direction = '>';
					}else if(direction == 'v'){
						direction = '<';
					}else if(direction == '>'){
						direction = 'v';
					}else{
						direction = '^';
					}
				}
				turn = (turn + 1) % 3;
			}
			// '\'
			else if(matrix[y][x] != '-' && matrix[y][x] != '|'){

				if(direction == '^'){
					direction = '<';
				}else if(direction == '<'){
					direction = '^';
				}else if(direction == '>'){
					direction = 'v';
				}else{
					direction = '>';
				}

			}
			return null;
		}
	}

	@Override
	protected Object partOne(){

		while(true){

			sort(carts);
			Cart[] crashedCars = null;

			for(int i = 0; i < carts.size(); i++){
				crashedCars = carts.get(i).move();

				if(crashedCars != null){
					return crashedCars[0].x + " " + crashedCars[0].y;
				}
			}
		}
	}

	@Override
	protected Object partTwo(){

		carts = carts2;

		while(true){

			sort(carts);

			Cart[] crashedCars = null;

			for(int i = 0; i < carts.size(); i++){
				crashedCars = carts.get(i).move();

				if(crashedCars != null){

					if(carts.indexOf(crashedCars[0]) > carts.indexOf(crashedCars[1])){
						i -= 2;
					}else{
						i--;
					}

					carts.remove(crashedCars[0]);
					carts.remove(crashedCars[1]);
				}
				if(carts.size() == 1){
					return carts.get(0).x + " " + carts.get(0).y;
				}
			}
		}
	}

	private char[][] matrix;

	private ArrayList<Cart> carts;
	private ArrayList<Cart> carts2;

	@Override
	protected void parseInput(String inputFile){

		carts = new ArrayList<>();
		carts2 = new ArrayList<>();

		try{

			Scanner sc = new Scanner(new File(inputFile));
			BufferedWriter bw;

			matrix = new char[151][151];

			for(int i = 0; sc.hasNextLine(); i++){
				String line = sc.nextLine();
				char[] a = line.toCharArray();
				matrix[i] = a;
			}

			for(int i = 0; i < matrix.length; i++){
				for(int j = 0; j < matrix[i].length; j++){
					if(matrix[i][j] == '<' || matrix[i][j] == '>'
						|| matrix[i][j] == '^' || matrix[i][j] == 'v'){
						Cart c = new Cart(j, i, matrix[i][j]);
						Cart c2 = new Cart(j, i, matrix[i][j]);
						carts.add(c);
						carts2.add(c2);
					}
				}
			}
			/*
			We substitute cart's symbols with tracks
			*/
			for(Cart c : carts){

				c.matrix = matrix;
				c.carts = carts;

				int x = c.x;
				int y = c.y;

				if(c.direction == '>' || c.direction == '<'){
					matrix[y][x] = '-';
				}else{
					matrix[y][x] = '|';
				}

			}

			for(Cart c : carts2){

				c.matrix = matrix;
				c.carts = carts2;

				int x = c.x;
				int y = c.y;

				if(c.direction == '>' || c.direction == '<'){
					matrix[y][x] = '-';
				}else{
					matrix[y][x] = '|';
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void sort(ArrayList<Cart> carts){
		ArrayList<Cart> novi = new ArrayList<>();


		for(int i = 0; i < carts.size() - 1; i++){
			Cart min = carts.get(i);
			int iMin = i;
			for(int j = i+1; j < carts.size(); j++){
				if(carts.get(j).y < min.y){
					min = carts.get(j);
					iMin = j;
				}
				else if(carts.get(j).y == min.y && carts.get(j).x < min.x){
					min = carts.get(j);
					iMin = j;
				}
			}
			carts.set(iMin, carts.get(i));
			carts.set(i, min);
		}


	}
}
