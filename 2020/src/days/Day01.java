package days;

import java.util.*;
import java.io.*;

import utilities.*;
import utilities.exceptions.SolutionNotFoundException;

public class Day01{

    private static int partOne(int[] input) throws SolutionNotFoundException{
        for(int i = 0; i < input.length - 1; i++){
            for(int j = i+1; j < input.length; j++){
                if(input[i] + input[j] == 2020){
                    return input[i] * input[j];    
                }  
            }
        }
        throw new SolutionNotFoundException("Solution not found for part one!");
    }

    private static int partTwo(int[] input) throws SolutionNotFoundException{
        for(int i = 0; i < input.length - 2; i++){
            for(int j = i+1; j < input.length - 1; j++){
                for(int n = j+1; n < input.length; n++){
                    if(input[i] + input[j] + input[n] == 2020){
                        return input[i] * input[j] * input[n];    
                    }
                }
            }
        }  
        throw new SolutionNotFoundException("Solution not found for part two!"); 
    }

    public static void main(String[] args) throws SolutionNotFoundException{

        Scanner sc = new Scanner(System.in);

        int[] input = Parser.getIntArray(sc);
        
        System.out.println(partOne(input));
        System.out.println(partTwo(input));
        
        sc.close();
    }
}

