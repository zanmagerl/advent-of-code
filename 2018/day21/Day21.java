import java.util.*;
import java.io.*;

public class Day21 extends AdventOfCode{

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

	private int[] registers;

	@Override
	protected Object partOne(){

		return execute(1);
	}

	@Override
	protected Object partTwo(){

		return execute(2);
	}

	private Set<Integer> setOfRegister3 = new HashSet<>();
	private int lastOne = 0;

	private boolean conditionForPart2(){

		if(setOfRegister3.contains(registers[3])){
			return true;
		}

		setOfRegister3.add(registers[3]);
		lastOne = registers[3];

		return false;
	}

	private Object execute(int part){

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
			if(instructionPointer == 28){
				if(part == 1){
					return registers[3];
				}else{
					if(conditionForPart2()){
						return lastOne;
					}
				}
			}
			instructionPointer = registers[start];
			instructionPointer++;
		}
		return null;
	}

	private int start;

	private ArrayList<Instruction> instructions;

	@Override
	protected void parseInput(String inputFile){

		instructions = new ArrayList<Instruction>();

		try{

			Scanner sc = new Scanner(new File(inputFile));

			start = Integer.parseInt(sc.nextLine().split(" ")[1]);

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
