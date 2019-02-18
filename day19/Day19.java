import java.util.*;
import java.io.*;

public class Day19 extends AdventOfCode{

	private static class Instruction{

		String opcode;
		int registerA;
		int registerB;
		int registerC;

		public Instruction(String o, int a, int b, int c){
			this.opcode = o;
			this.registerA = a;
			this.registerB = b;
			this.registerC = c;
		}
	}

	int[] registers;

	@Override
	protected Object partOne(){

		registers = new int[6];

		int instructionPointer = registers[start];

		while(true){

			if(instructionPointer >= instructions.size()){
				break;
			}
			Instruction in = instructions.get((int)instructionPointer);

			int a = in.registerA;
			int b = in.registerB;
			int c = in.registerC;

			registers[start] = instructionPointer;

			switch(in.opcode){
				case("addr"):{
					registers[c] = registers[a] + registers[b];
					break;
				}
				case("addi"):{
					registers[c] = registers[a] + b;
					break;
				}
				case("mulr"):{
					registers[c] = registers[a] * registers[b];
					break;
				}
				case("muli"):{
					registers[c] = registers[a] * b;
					break;
				}
				case("banr"):{
					registers[c] = registers[a] & registers[b];
					break;
				}
				case("bani"):{
					registers[c] = registers[a] & b;
					break;
				}
				case("borr"):{
					registers[c] = registers[a] | registers[b];
					break;
				}
				case("bori"):{
					registers[c] = registers[a] | b;
					break;
				}
				case("setr"):{
					registers[c] = registers[a];
					break;
				}
				case("seti"):{
					registers[c] = a;
					break;
				}
				case("gtir"):{
					if(a > registers[b]){
						registers[c] = 1;
					}else{
						registers[c] = 0;
					}
					break;
				}
				case("gtri"):{
					if(registers[a] > b){
						registers[c] = 1;
					}else{
						registers[c] = 0;
					}
					break;
				}
				case("gtrr"):{
					if(registers[a] > registers[b]){
						registers[c] = 1;
					}else{
						registers[c] = 0;
					}
					break;
				}
				case("eqir"):{
					if(a == registers[b]){
						registers[c] = 1;
					}else{
						registers[c] = 0;
					}
					break;
				}
				case("eqri"):{
					if(registers[a] == b){
						registers[c] = 1;
					}else{
						registers[c] = 0;
					}
					break;
				}
				case("eqrr"):{
					if(registers[a] == registers[b]){
						registers[c] = 1;
					}else{
						registers[c] = 0;
					}
					break;
				}
			}

			instructionPointer = registers[start];
			instructionPointer++;
		}
		return registers[0];
	}

	@Override
	protected Object partTwo(){
		/*
		I actually solved this problem using pencil and paper,
		because I needed to figure out what the program does.
		Soon it became evidently, that certain instructions repeated
		itself for a big number of turns. I analysed algorithm
		and then figured out that it's a prime factorization.
		It adds value in [3] to [0], if product of values in [2] and [3]
		equals the value in [1]. This does for all values in [2],
		from 1 to value in [1].

		That repeats same algorithm with incremented value in [3].

		[3] is gradually increasing by one until it is bigger
		that [1].
		Then program halts.

		It theory this assembly code would have outputed
		the corret answer, but it would take a lot(!) of time.
		It is easier to implement more efficient algorithm
		that does the same thing but with O(n) time complexity.

		Pseudocode of an assembly algorithm:

		int [0] = 0
		int [3] = 1

		while([3] <= [1]){

			int [2] = 1;

			while([2] <= [1]){
				if([2]*[3] == [1]){
					[0] += [3];
				}
				[2]++;
			}
			[3]++;
		}
		*/

		//Number from register [1] is 10551410.
		int number = 10551410;

		int result = 0;

		for(int i = 1; i <= number; i++){
			if(number % i == 0){
				result += i;
			}
		}

		return result;
	}

	int start = 5;

	private ArrayList<Instruction> instructions;

	@Override
	protected void parseInput(String inputFile){

		instructions = new ArrayList<Instruction>();

		try{

			Scanner sc = new Scanner(new File(inputFile));
			while(sc.hasNextLine()){
				String[] a = sc.nextLine().split(" ");
				instructions.add(new Instruction(a[0], Integer.parseInt(a[1]),
							Integer.parseInt(a[2]), Integer.parseInt(a[3])));
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
