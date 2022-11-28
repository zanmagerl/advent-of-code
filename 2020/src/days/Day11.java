package days;

import java.util.*;

import java.io.*;

import utilities.Parser;

public class Day11 {

    private static int numberOfOccupied(char[][] seating, int r, int c){
        int result = 0;
        if(r > 0){
            if(c > 0 && seating[r-1][c-1] == '#') result++;
            if(seating[r-1][c] == '#') result++;
            if(c < seating[0].length - 1 && seating[r-1][c+1] == '#') result++;
        }
        if(r < seating.length-1){
            if(c > 0 && seating[r+1][c-1] == '#') result++;
            if(seating[r+1][c] == '#') result++;
            if(c < seating[0].length - 1 && seating[r+1][c+1] == '#') result++;
        }
        if(c > 0 && seating[r][c-1] == '#') result++;
        if(c < seating[0].length - 1 && seating[r][c+1] == '#') result++;
        

        return result;
    }

    private static int numberOfSeenOccupied(char[][] seating, int r, int c){
        
        int result = 0;

        int[][] directions = {{-1,-1}, {-1,0}, {-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};

        for(int[] direction : directions){

            int x = c;
            int y = r;
            boolean inValid = false;
            do{

                x += direction[1];
                y += direction[0];

                if(y < 0 || y >= seating.length || x < 0 || x >= seating[0].length){
                    inValid = true;
                    break;
                }

            }while(seating[y][x] == '.');

            if(!inValid){
                if(seating[y][x] == '#'){
                    result++;
                }
            }
        }


        return result;
    }

    private static char[][] applySeatingRules(char[][] layout){

        char[][] result = new char[layout.length][layout[0].length];

        for(int i = 0; i < layout.length; i++){
            for(int j = 0; j < layout[i].length; j++){
                if(layout[i][j] == 'L'){
                    int occupied = numberOfOccupied(layout, i, j);
                    if(occupied == 0){
                        result[i][j] = '#';
                    }else{
                        result[i][j] = 'L';
                    }
                }else if(layout[i][j] == '#'){
                    int occupied = numberOfOccupied(layout, i, j);
                    if(occupied >= 4){
                        result[i][j] = 'L';
                    }else{
                        result[i][j] = '#';
                    } 
                }else{
                    result[i][j] = layout[i][j];
                }
            }
        }

        return result;
    }

    private static char[][] applyCorrectSeatingRules(char[][] layout){

        char[][] result = new char[layout.length][layout[0].length];

        for(int i = 0; i < layout.length; i++){
            for(int j = 0; j < layout[i].length; j++){
                if(layout[i][j] == 'L'){
                    int occupied = numberOfSeenOccupied(layout, i, j);
                    if(occupied == 0){
                        result[i][j] = '#';
                    }else{
                        result[i][j] = 'L';
                    }
                }else if(layout[i][j] == '#'){
                    int occupied = numberOfSeenOccupied(layout, i, j);
                    if(occupied >= 5){
                        result[i][j] = 'L';
                    }else{
                        result[i][j] = '#';
                    } 
                }else{
                    result[i][j] = layout[i][j];
                }
            }
        }

        return result;
    }

    private static boolean hasChanged(char[][] first, char[][] second){
        for(int i = 0; i < first.length; i++){
            for(int j = 0; j < first[0].length; j++){
                if(first[i][j] != second[i][j]) return true;
            }
        }
        return false;
    }

    private static int countOccupied(char[][] arr){
        int result = 0;

        for(int i = 0; i < arr.length; i++){
            for(int j = 0; j < arr[0].length; j++){
                if(arr[i][j] == '#') result++;
            }
        }
        return result;
    }

    private static int partOne(char[][] layout){
        
        char[][] iter = layout;

        do{
            layout = iter;
            iter = applySeatingRules(layout);
        }while(hasChanged(layout, iter));

        return countOccupied(iter);
    }


    private static int partTwo(char[][] layout){
        
        char[][] iter = layout;

        do{
            layout = iter;
            iter = applyCorrectSeatingRules(layout);
        }while(hasChanged(layout, iter));

        return countOccupied(iter);
    }

    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);
        
        char[][] layout = Parser.get2DCharArray(sc);

        System.out.println(partOne(layout));
        System.out.println(partTwo(layout));

        sc.close();
    }
}
