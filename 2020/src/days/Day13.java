package days;

import java.util.*;

import java.io.*;

import utilities.Parser;

public class Day13{

    private static int partOne(int departTime, List<Integer> buses){

        int minId = 0;
        int minWait = Integer.MAX_VALUE;

        for(int bus : buses){
            int numberOfCycles = departTime / bus;

            int waitTime = numberOfCycles * bus + bus  - departTime;

            if(waitTime < minWait){
                minWait = waitTime;
                minId = bus;
            }
        }

        return minId * minWait;
    }


    private static long partTwo(List<Integer> ids, List<Integer> offsets){
    
        long timeStamp = 0L;
        long nextStart = ids.get(0);

        for(int i = 1; i < ids.size(); i++){
            while((timeStamp + offsets.get(i)) % ids.get(i) != 0){
                timeStamp += nextStart;
            }
            nextStart *= ids.get(i); // to make sure that result so far will hold also in the future
        }

        return timeStamp;
    }

    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);
        
        int departTime = Integer.parseInt(sc.nextLine());
        List<Integer> buses = new ArrayList<>();

        String[] splittedLine = sc.nextLine().split(",");

        for(String id : splittedLine){
            if(!id.equals("x")) buses.add(Integer.parseInt(id));
        }
        
        System.out.println(partOne(departTime, buses));
        
        List<Integer> offsets = new ArrayList<>();
        int currentOffset = 0;

        for(String id : splittedLine){
            if(!id.equals("x")){
                offsets.add(currentOffset);   
            }
            currentOffset++;
        }

        System.out.println(partTwo(buses, offsets));

        sc.close();
    }
}
