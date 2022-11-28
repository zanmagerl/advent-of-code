package days;

import java.util.*;
import java.io.*;

public class Day09 {

    private static boolean isSum(List<Long> numbers, long searchedNumber){

        for(int i = 0; i < numbers.size() - 1; i++){
            for(int j = i+1; j < numbers.size(); j++){
                if(numbers.get(i)+numbers.get(j) == searchedNumber){
                    return true;
                }
            }
        }
        return false;
    }

    private static long getSum(List<Long> numbers, long searchedNumber){
        
        long sum = 0;
        
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;

        for(int i = 0; i < numbers.size(); i++){
            if(numbers.get(i) < min){
                min = numbers.get(i);
            }
            if(numbers.get(i) > max){
                max = numbers.get(i);
            }
            sum += numbers.get(i);
            if(sum == searchedNumber){
                return min + max;
            }
            if(sum > searchedNumber){
                break;
            }
            
        }
        return -1;
    }

    private static long partOne(List<Long> list){
        
        int result = 0;

        for(int i = 25; i < list.size(); i++){
            if(!isSum(list.subList(i-25, i), list.get(i))){
                return list.get(i);
            }
        }

        return result;
    }


    private static long partTwo(List<Long> list){

        long number = partOne(list);

        for(int i = 0; i < list.size(); i++){
            long value = getSum(list.subList(i, list.size()), number);
            if(value != -1){
                return value;
            }
        }

        return -1;
    }

    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);

        List<Long> numbers = new ArrayList<>();
        
        while(sc.hasNextLine()){
            numbers.add(sc.nextLong());            
        }

        System.out.println(partOne(numbers));
        System.out.println(partTwo(numbers));

        sc.close();
    }
}
