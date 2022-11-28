package days;

import java.util.*;

import java.io.*;

import utilities.Array;
import utilities.Parser;

public class Day14{

    private static class WriteInstruction{

        int address;
        long value;

        public WriteInstruction(int address, long value){
            this.address = address;
            this.value = value;
        }
    }

    private static long[] getBitMasks(String mask){
        long maskOnes = 0;
        long maskZeros = 0;

        char[] maskBits = mask.toCharArray();

        for(int i = 0; i < maskBits.length; i++){
            maskOnes *= 2;
            maskZeros *= 2;
            if(maskBits[i] == '1'){
                maskOnes += 1;
                maskZeros += 1;
            }else if(maskBits[i] == '0'){

            }else{
                maskZeros += 1;
            }
            
        }
        return new long[]{maskZeros, maskOnes};
    }

    private static long[] getBitMasksX(String mask){
        long maskOnes = 0;
        long maskZeros = 0;
        long maskX = 0;

        char[] maskBits = mask.toCharArray();

        for(int i = 0; i < maskBits.length; i++){
            maskOnes *= 2;
            maskZeros *= 2;
            maskX *= 2;
            if(maskBits[i] == '1'){
                maskOnes += 1;
                maskZeros += 1;
                maskX += 1;
            }else if(maskBits[i] == '0'){
                maskX += 1;
            }else{
                maskZeros += 1;
            }
            
        }
        return new long[]{maskZeros, maskOnes, maskX};
    }


    private static long partOne(List<String> masks, List<List<WriteInstruction>> listOfWriteInstructions){

        HashMap<Integer, Long> memory = new HashMap<>();

        for(int j = 0; j < masks.size(); j++){

            String mask = masks.get(j);
            List<WriteInstruction> writeInstructions = listOfWriteInstructions.get(j);

            long[] bitMasks = getBitMasks(mask); //at [0] is mask for zeros, and at [1] is mask for ones

            for(WriteInstruction writeInstruction : writeInstructions){
                memory.put(writeInstruction.address, (writeInstruction.value | bitMasks[1]) & bitMasks[0]);
            }
        }

        long sum = 0;

        for(Integer address : memory.keySet()){
            sum += memory.get(address);
        }

        return sum;
    }


    private static int numberOfX(long x){
        int res = 0;

        String xb = Long.toBinaryString(x);
        String zeros = "";
        for(int ji = 0; ji < 36 - xb.length(); ji++) zeros += "0";

        xb = zeros + xb;
        for(char c : xb.toCharArray()){
            if(c == '0') res++;
        }

        return res;
    }

    private static int[] indexesX(long x){
        List<Integer> indexes = new ArrayList<>();

        String xb = Long.toBinaryString(x);
        String zeros = "";
                    for(int ji = 0; ji < 36 - xb.length(); ji++) zeros += "0";

                    xb = zeros + xb;
        for(int i = 0; i < xb.length(); i++){
            if(xb.charAt(i) == '0') indexes.add(i);
        }
        int[] idx = new int[indexes.size()];
        for(int i = 0; i < indexes.size(); i++){
            idx[i] = indexes.get(i);
        }

        return idx;
    }

    private static long partTwo(List<String> masks, List<List<WriteInstruction>> listOfWriteInstructions){
        
        HashMap<Long, Long> memory = new HashMap<>();

        for(int j = 0; j < masks.size(); j++){
            
            long[] bitMasks = getBitMasksX(masks.get(j));

            List<WriteInstruction> writeInstructions = listOfWriteInstructions.get(j);

            for(WriteInstruction writeInstruction : writeInstructions){
                int numberOfX = (int)Math.pow(2,numberOfX(bitMasks[2]));
                
                long[] memoryAddreses = new long[numberOfX];
                int[] indexes = indexesX(bitMasks[2]);

                int counter = 0;
                for(int i = 0; i < numberOfX; i++){
                    String xb = Integer.toBinaryString(i);
                    String zeros = "";
                    for(int ji = 0; ji < indexes.length - xb.length(); ji++) zeros += "0";

                    xb = zeros + xb;
                    //System.out.println("xb: " + xb);
                    memoryAddreses[counter] = 0;
                    memoryAddreses[counter] += ((writeInstruction.address | bitMasks[1]) & bitMasks[2]);
                    for(int n = 0; n < xb.length(); n++){
                        if(xb.charAt(n) == '1'){
                            //System.out.println(counter + ": " + n);
                            memoryAddreses[counter] += (long)(Math.pow(2, 35 - indexes[n]));
                        }
                    }
                    
                    
                    counter++;
                }

                for(long memoryAddress : memoryAddreses){
                    //System.out.println(memoryAddress);
                    memory.put(memoryAddress, writeInstruction.value);
                }
            }
        }
        long sum = 0;

        for(Long address : memory.keySet()){
            
            sum += memory.get(address);
        }

        return sum;
    }

    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);
        
        String mask = sc.nextLine().split("=")[1].strip();

        List<String> masks = new ArrayList<>();
        List<List<WriteInstruction>> listOfWriteInstructions = new ArrayList<>();
        List<WriteInstruction> writeInstructions = new ArrayList<>();

        masks.add(mask);

        while(sc.hasNextLine()){
            String line = sc.nextLine();

            if(line.contains("mask")){
                masks.add(line.split("=")[1].strip());
                listOfWriteInstructions.add(writeInstructions);
                writeInstructions = new ArrayList<>();
                continue;
            }

            String[] splittedLine = line.split("=");
            int address = Integer.parseInt((splittedLine[0].strip()).substring(4, splittedLine[0].strip().length() - 1));
            long value = Long.parseLong(splittedLine[1].strip());
            writeInstructions.add(new WriteInstruction(address, value));
        }

        listOfWriteInstructions.add(writeInstructions);

        System.out.println(partOne(masks, listOfWriteInstructions));
        System.out.println(partTwo(masks, listOfWriteInstructions));

        sc.close();
    }
}
