import java.util.*;
import java.io.*;

public class Day9 extends AdventOfCode{


	public static class Celica{

		int value;
		Celica previous;
		Celica next;

		public Celica(int v){
			this.value = v;
		}
	}

	public static class Seznam{

		Celica first;

		public void add(Celica c, int v){

			Celica nova = new Celica(v);

			nova.next = c.next;

			if(c.next == null){
				c.next = first;
			}

			nova.previous = c;
			c.next = nova;

		}
	}


	@Override
	protected Object partOne(){

		ArrayList<Integer> marbles = new ArrayList<>();

		long players[] = new long[numberOfPlayers+1];

		marbles.add(0);
		marbles.add(1);

		int currentIndex = 1;

		int playerNumber = 2;

		for(int i = 2; i <= numberOfMarbles-2; i++){
			if(i % 23 != 0){

				int index = (currentIndex + 2);

				if(index > marbles.size()){
					index %= marbles.size();
				}

				marbles.add(index, i);

				currentIndex = index;

				playerNumber++;
				
				if(playerNumber > numberOfPlayers){
					playerNumber = 1;
				}
			}else{

				players[playerNumber] += i;

				int index = currentIndex - 7;
				
				if(index < 0){
					index += marbles.size();
				}
				
				players[playerNumber] += marbles.get(index);
				marbles.remove(index);
				currentIndex = index;

				playerNumber++;
				
				if(playerNumber > numberOfPlayers){
					playerNumber = 1;
				}
			}

		}

		long max = 0;

		for(int i = 0; i < players.length; i++){
			if(players[i] > max){
				max = players[i];
			}
		}
		return max;
	}

	@Override
	protected Object partTwo(){

		Seznam marbles = new Seznam();

		long players[] = new long[numberOfPlayers+1];

		marbles.first = new Celica(0);
		marbles.first.next = new Celica(1);
		marbles.first.next.previous = marbles.first;
		marbles.first.next.next = marbles.first;

		int currentIndex = 1;

		int playerNumber = 2;
		int size = 2;
		Celica current = marbles.first.next;

		for(int i = 2; i <= 100*numberOfMarbles; i++){
			
			if(i % 23 != 0){

				Celica nova = new Celica(i);
				Celica pos = current.next;
				
				nova.previous = pos;
				nova.next = pos.next;
				nova.previous.next = nova;
				nova.next.previous = nova;

				size++;

				playerNumber++;
				
				if(playerNumber > numberOfPlayers){
					playerNumber = 1;
				}
				
				current = nova;
			}else{
				
				players[playerNumber] += i;
				
				for(int j = 0; j < 7; j++){
					current = current.previous;
				}
				
				players[playerNumber] += current.value;
				current.previous.next = current.next;
				current.next.previous = current.previous;
				current = current.next;
				
				size--;
				playerNumber++;
				
				if(playerNumber > numberOfPlayers){
					playerNumber = 1;
				}
			}
		}

		long max = 0;
		for(int i = 0; i < players.length; i++){
			if(players[i] > max){
				max = players[i];
			}
		}

		return max;
	}

	private int numberOfPlayers;
	private int numberOfMarbles;

	@Override
	protected void parseInput(String inputFile){

		try{

			Scanner sc = new Scanner(new File(inputFile));
			String line = sc.nextLine();

			String[] data = line.split(" ");
			numberOfPlayers = Integer.parseInt(data[0]);
			numberOfMarbles = Integer.parseInt(data[6]);

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}