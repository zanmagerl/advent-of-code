package days;

import java.util.*;

import java.io.*;

import utilities.Parser;

public class Day10 {

    private static int partOne(int[] joltages){
        
        int numberOfDifference1 = 0;
        int numberOfDifference3 = 0;

        int i = 0;
        int currentJoltage = 0;

        while(i < joltages.length){
            int difference = joltages[i] - currentJoltage;
            if(difference == 1) numberOfDifference1++;
            if(difference == 3) numberOfDifference3++;

            currentJoltage += difference;
            i++;
        }
        numberOfDifference3++;

        return numberOfDifference1 * numberOfDifference3;
    }


    private static long partTwo(int[] adapters, int index){
        
        long[] allWays = new long[adapters[adapters.length-1]+1];
        allWays[0] = 1;

        for(int adapter : adapters){
            
            long num = 1;
            if(adapter == 1) num = allWays[adapter-1];
            if(adapter == 2) num = allWays[adapter-1] + allWays[adapter-2];
            if(adapter >= 3) num = allWays[adapter-3] + allWays[adapter-2] + allWays[adapter-1];

            allWays[adapter] = num;
        }

        return allWays[allWays.length-1];
    }

    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);
        
        int[] adapters = Parser.getIntArray(sc);

        Arrays.sort(adapters);

        System.out.println(partOne(adapters));
        System.out.println(partTwo(adapters, 0));

        sc.close();
    }
}
