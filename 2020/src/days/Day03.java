package days;

import java.util.*;
import java.io.*;
import utilities.Parser;

public class Day03 {
    
    private static int countTreesForGivenSlope(char[][] terrain, int xDirection, int yDirection){
        
        int xPosition = 0;
        int yPosition = 0;

        int numberOfTrees = 0;

        while(yPosition + yDirection < terrain.length){
            xPosition = (xPosition + xDirection) % terrain[0].length;
            yPosition += yDirection;

            if(terrain[yPosition][xPosition] == '#') numberOfTrees++;
        }
        return numberOfTrees;
    }

    private static int partOne(char[][] terrain){
        return countTreesForGivenSlope(terrain, 3, 1);
    }

    private static int partTwo(char[][] terrain){

        int[][] directions = {
            {1,1},
            {3,1},
            {5,1},
            {7,1},
            {1,2}
        };

        int result = 1;

        for(int[] direction : directions){
            result *= countTreesForGivenSlope(terrain, direction[0], direction[1]);
        }

        return result;
    }

    public static void main(String[] args) throws Exception{

        Scanner sc = new Scanner(System.in);
        
        char[][] terrain = Parser.get2DCharArray(sc);
        
        System.out.println(partOne(terrain));
        System.out.println(partTwo(terrain));

        sc.close();
    }
}
