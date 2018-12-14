import java.util.*;
import java.io.*;

public class Day12 extends AdventOfCode{

	private static class Instruction{

		String left;
		String right;
		String pot;

		String result;

		public Instruction(String left, String pot, String right, String result){
			this.left = left;
			this.pot = pot;
			this.right = right;
			this.result = result;
		}

	}

	private ArrayList<String> newGeneration(ArrayList<String> generation){

		ArrayList<String> generation2 = new ArrayList<>();
		generation2.add(".");
		generation2.add(".");
		for(int i = 2; i < generation.size() - 2; i++){
			for(Instruction x : instructions){
				if(x.left.equals(generation.get(i-2) + generation.get(i-1))
					&& x.pot.equals(generation.get(i))
					&& x.right.equals(generation.get(i+1) + generation.get(i+2))){
					generation2.add(x.result);
					break;
				}
			}
		}
		generation2.add(".");
		generation2.add(".");

		return generation2;
	}

	@Override
	protected Object partOne(){

		ArrayList<String> generation = new ArrayList<>();

		String[] init = initialState.split("");

		int numberOfGenerations = 20;

		//borders
		for(int i = 0; i < numberOfGenerations*2; i++){
			generation.add(".");
		}
		for(String x : init){
			generation.add(x);
		}
		//borders
		for(int i = 0; i < numberOfGenerations*2; i++){
			generation.add(".");
		}

		int indexOfCenterPot = numberOfGenerations*2;
		int prej = 0;

		for(long i = 0; i < numberOfGenerations; i++){
			generation = newGeneration(generation);
		}

		int sum = 0;

		for(int i = 0; i < generation.size(); i++){
			if(generation.get(i).equals("#")){
				sum += i - indexOfCenterPot;
			}
		}
		
		return sum;
	}

	@Override
	protected Object partTwo(){

		//Solution was found using method: find (linear) pattern 

		long sum = 2543;
		
		return 2543 + 22*(50000000000L-94);
	}

	private ArrayList<Instruction> instructions;
	private String initialState;

	@Override
	protected void parseInput(String inputFile){
			
		instructions = new ArrayList<>();

		try{

			Scanner sc = new Scanner(new File(inputFile));

			initialState = sc.nextLine().split(" ")[2];
			sc.nextLine();

			while(sc.hasNextLine()){
				String vhod = sc.nextLine();
				
				String[] a = vhod.split(" ");	
				String b = a[0].substring(0, 2);
				String c = a[0].substring(2,3);
				String d = a[0].substring(3,5);
				String r = a[2];

				instructions.add(new Instruction(b, c, d, r));
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}