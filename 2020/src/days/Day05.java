package days;

import java.util.*;
import java.io.*;
import java.lang.reflect.*;

import utilities.Parser;
import utilities.exceptions.SolutionNotFoundException;

public class Day05 {

    private static int getSeatID(String seat){
        String rows = seat.substring(0, 7);
        String columns = seat.substring(7, seat.length());

        int row = 0;
        int column = 0;

        for(char c : rows.toCharArray()){
            row *= 2;
            if(c == 'B'){
                row++;
            }
            
        }

        for(char c : columns.toCharArray()){
            column *= 2;
            if(c == 'R') column++;
        }

        return row*8 + column;
    }

    private static int partOne(List<String> seats){
        
        int max = 0;

        for(String seat : seats){
            
            int id = getSeatID(seat);

            if(id > max){
                max = id;
            }
        }

        return max;
    }

    private static int partTwo(List<String> seats) throws SolutionNotFoundException {

        int[] ids = new int[seats.size()];

        int j = 0;

        for(String seat : seats){
            ids[j] = getSeatID(seat);
            j++;
        }

        Arrays.sort(ids);

        for(int i = 1; i < ids.length; i++){
            if(ids[i] - ids[i-1] == 2){
                return ids[i]-1;
            }
        }

        throw new SolutionNotFoundException("/");
    }

    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);

        List<String> seats = new ArrayList<>();

        while(sc.hasNextLine()){
            seats.add(sc.nextLine());
        }

        System.out.println(partOne(seats));

        try{
            System.out.println(partTwo(seats));
        }catch(SolutionNotFoundException exception){
            System.out.println(exception.getMessage());
        }

        sc.close();
    }
}
