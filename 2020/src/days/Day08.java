package days;

import java.util.*;
import java.io.*;

import utilities.Parser;

public class Day08 {

    private static class Instruction{

        String name;
        int argument;

        public Instruction(String name, int argument){
            this.name = name;
            this.argument = argument;
        }
    }

    private static boolean itTerminates(List<Instruction> list){

        HashMap<Integer, Instruction> ranInstructions = new HashMap<>();

        int pc = 0;
        
        while(ranInstructions.get(pc) == null){

            if(pc >= list.size()){
                return true;
            }

            Instruction i = list.get(pc);
            ranInstructions.put(pc, i);

            if(i.name.equals("jmp")){
                pc += i.argument;
                continue;
            }
            pc++;
        }

        return false;
    }


    private static int partOne(List<Instruction> list){
        
        HashMap<Integer, Instruction> ranInstructions = new HashMap<>();

        int accumulator = 0;

        int pc = 0;
        
        while(ranInstructions.get(pc) == null){

            if(pc >= list.size()){
                return accumulator;
            }

            Instruction i = list.get(pc);
            ranInstructions.put(pc, i);

            if(i.name.equals("acc")){
                accumulator += i.argument;
            }else if(i.name.equals("jmp")){
                pc += i.argument;
                continue;
            }else{

            }
            pc++;
        }

        return accumulator;
    }


    private static int partTwo(List<Instruction> list){

        int i = 0;

        while(i < list.size()){
            Instruction in = list.get(i);
            
            if(in.name.equals("nop")){
                list.remove(i);
                list.add(i, new Instruction("jmp", in.argument));
            }else if(in.name.equals("jmp")){
                list.remove(i);
                list.add(i, new Instruction("nop", in.argument));
            }

            if(itTerminates(list)){
                return partOne(list);
            }

            list.remove(i);
            list.add(i, in);
            i++;
        }

        return 0;
    }

    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);

        List<Instruction> list = new ArrayList<>();

        while(sc.hasNextLine()){
            String line = sc.nextLine();

            String[] splittedString = line.split(" ");

            list.add(new Instruction(splittedString[0], Integer.parseInt(splittedString[1])));
        }

        System.out.println(partOne(list));
        System.out.println(partTwo(list));

        sc.close();
    }
}
