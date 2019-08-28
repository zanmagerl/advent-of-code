import java.util.*;
import java.io.*;

public class Day16 extends AdventOfCode{

	private static class Instruction{

		int opcode;
		int registerA;
		int registerB;
		int registerC;

		public Instruction(int o, int a, int b, int c){
			this.opcode = o;
			this.registerA = a;
			this.registerB = b;
			this.registerC = c;
		}
	}

	int counterForPartOne = 0;

	@Override
	protected Object partOne(){

		return counterForPartOne;
	}

	@Override
	protected Object partTwo(){

		int[] registers = new int[4];

		for(int i = 0; i < instructions.size(); i++){
			Instruction x = instructions.get(i);

			int c = x.registerC;
			int a = x.registerA;
			int b = x.registerB;

			switch(x.opcode){
				case(13):{
					registers[c] = registers[a] + registers[b];
					break;
				}
				case(15):{
					registers[c] = registers[a] + b;
					break;
				}
				case(10):{
					registers[c] = registers[a] * registers[b];
					break;
				}
				case(8):{
					registers[c] = registers[a] * b;
					break;
				}
				case(7):{
					registers[c] = registers[a] & registers[b];
					break;
				}
				case(0):{
					registers[c] = registers[a] & b;
					break;
				}
				case(5):{
					registers[c] = registers[a] | registers[b];
					break;
				}
				case(6):{
					registers[c] = registers[a] | b;
					break;
				}
				case(12):{
					registers[c] = registers[a];
					break;
				}
				case(2):{
					registers[c] = a;
					break;
				}
				case(14):{
					if(a > registers[b]){
						registers[c] = 1;
					}else{
						registers[c] = 0;
					}
					break;
				}
				case(1):{
					if(registers[a] > b){
						registers[c] = 1;
					}else{
						registers[c] = 0;
					}
					break;
				}
				case(11):{
					if(registers[a] > registers[b]){
						registers[c] = 1;
					}else{
						registers[c] = 0;
					}
					break;
				}
				case(3):{
					if(a == registers[b]){
						registers[c] = 1;
					}else{
						registers[c] = 0;
					}
					break;
				}
				case(9):{
					if(registers[a] == b){
						registers[c] = 1;
					}else{
						registers[c] = 0;
					}
					break;
				}
				case(4):{
					if(registers[a] == registers[b]){
						registers[c] = 1;
					}else{
						registers[c] = 0;
					}
					break;
				}
			}
		}

		return registers[0];
	}
	/*
	Commented code in method check() was used to determine
	which opcode corresponds to specific instruction
	Method used: Deduction
	*/
	private void check(String ab, String ba, Instruction instruction){

		/*
		ArrayList<Integer> instr = new ArrayList<>();
		instr.add(3);
		instr.add(1);
		instr.add(7);
		instr.add(6);
		instr.add(0);
		instr.add(2);
		instr.add(9);
		instr.add(10);
		instr.add(5);
		instr.add(4);
		instr.add(8);
		instr.add(14);
		instr.add(13);
		instr.add(12);
		instr.add(11);
		instr.add(15);
		*/

		int[] before = new int[4];
		int[] after = new int[4];

		for(int i = 0; i < before.length; i++){
			before[i] = Integer.parseInt(ab.substring(0+i,1+i));
		}
		for(int i = 0; i < after.length; i++){
			after[i] = Integer.parseInt(ba.substring(0+i,1+i));
		}

		String[] ins = {"addr", "addi", "mulr", "muli", "banr", "bani",
						"borr", "bori", "setr", "seti", "gtir", "gtri",
						"gtrr", "eqir", "eqri", "eqrr"};

		int counter = 0;
		int lastTrue = 0;
		for(int j = 0; j < ins.length; j++){

			String in = ins[j];

			int c = instruction.registerC;
			int a = instruction.registerA;
			int b = instruction.registerB;

			int[] result = new int[4];
			for(int i = 0; i < result.length; i++){
				result[i] = before[i];
			}
			switch(in){
				case("addr"):{
					result[c] = before[a] + before[b];
					break;
				}
				case("addi"):{
					result[c] = before[a] + b;
					break;
				}
				case("mulr"):{
					result[c] = before[a] * before[b];
					break;
				}
				case("muli"):{
					result[c] = before[a] * b;
					break;
				}
				case("banr"):{
					result[c] = before[a] & before[b];
					break;
				}
				case("bani"):{
					result[c] = before[a] & b;
					break;
				}
				case("borr"):{
					result[c] = before[a] | before[b];
					break;
				}
				case("bori"):{
					result[c] = before[a] | b;
					break;
				}
				case("setr"):{
					result[c] = before[a];
					break;
				}
				case("seti"):{
					result[c] = a;
					break;
				}
				case("gtir"):{
					if(a > before[b]){
						result[c] = 1;
					}else{
						result[c] = 0;
					}
					break;
				}
				case("gtri"):{
					if(before[a] > b){
						result[c] = 1;
					}else{
						result[c] = 0;
					}
					break;
				}
				case("gtrr"):{
					if(before[a] > before[b]){
						result[c] = 1;
					}else{
						result[c] = 0;
					}
					break;
				}
				case("eqir"):{
					if(a == before[b]){
						result[c] = 1;
					}else{
						result[c] = 0;
					}
					break;
				}
				case("eqri"):{
					if(before[a] == b){
						result[c] = 1;
					}else{
						result[c] = 0;
					}
					break;
				}
				case("eqrr"):{
					if(before[a] == before[b]){
						result[c] = 1;
					}else{
						result[c] = 0;
					}
					break;
				}
			}
			if(compare(result, after) /*&& !instr.contains(j)*/){
				lastTrue = j;
				counter++;
			}
		}
		/*
		if(counter == 1){
			System.out.println(instruction.opcode + " " + lastTrue);
		}
		*/
		if(counter >= 3){
			counterForPartOne++;
		}
	}

	private boolean compare(int[] a, int[] b){
		for(int i = 0; i < a.length; i++){
			if(a[i] != b[i])
				return false;
		}
		return true;
	}

	ArrayList<Instruction> instructions;

	@Override
	protected void parseInput(String inputFile){

		instructions = new ArrayList<>();

		try{

			Scanner sc = new Scanner(new File(inputFile));
			//Reading input for part 1
			while(sc.hasNextLine() ){
				String before = sc.nextLine();
				/*
				Input for the next part of the task is separated with
				more than one empty line => empty line means the end of input
				for part 1
				*/
				if(before.isEmpty()){
					break;
				}
				before = before.substring(8, 20);
				String a = before.substring(1,2);
				String b = before.substring(4,5);
				String c = before.substring(7,8);
				String d = before.substring(10,11);
				before = (a + b + c + d);
				String[] n = sc.nextLine().split(" ");

				instructions.add(new Instruction(Integer.parseInt(n[0]),
						Integer.parseInt(n[1]), Integer.parseInt(n[2]), Integer.parseInt(n[3])));

				String after = sc.nextLine().substring(8, 20);

				a = after.substring(1,2);
				b = after.substring(4,5);
				c = after.substring(7,8);
				d = after.substring(10,11);
				after = (a + b + c + d);
				check(before, after, instructions.get(instructions.size()-1));

				sc.nextLine();
			}
			//Reading input for part 2
			sc.nextLine();
			instructions.clear();

			while(sc.hasNextLine()){

				String[] n = sc.nextLine().split(" ");
				instructions.add(new Instruction(Integer.parseInt(n[0]),
						Integer.parseInt(n[1]), Integer.parseInt(n[2]), Integer.parseInt(n[3])));
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
