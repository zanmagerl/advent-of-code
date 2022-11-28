package days;

import java.util.*;

import java.io.*;

import utilities.Parser;

public class Day15{


    private static class Number{

        int number;

        int firstTurn;
        int secondTurn;

        public Number(int number, int secondTurn){
            this.number = number;
            this.secondTurn = secondTurn;
            this.firstTurn = -1;
        }

        public void update(int secondTurn){
            this.firstTurn = this.secondTurn;
            this.secondTurn = secondTurn;
        }

        public int difference(){
            if(firstTurn == -1){
                return 0;
            }
            return secondTurn - firstTurn;
        }
    }

    private static int partOne(int[] numbers){

        HashMap<Integer, Number> pastNumbers = new HashMap<>();

        Number lastNumber = null;

        int i = 0;
        for(; i < numbers.length; i++){
            Number num = new Number(numbers[i], i+1);
            pastNumbers.put(num.number, num);
            lastNumber = num;
        }

        for(; i < 2020; i++){
            int num = lastNumber.difference();
            if(pastNumbers.get(num) != null){
                lastNumber = pastNumbers.get(num);
                lastNumber.update(i+1);
            }else{
                Number newNumber = new Number(num, i+1);
                pastNumbers.put(num, newNumber);
                lastNumber = newNumber;
            }
        }   

        return lastNumber.number;
    }


    private static int partTwo(int[] numbers){

        HashMap<Integer, Number> pastNumbers = new HashMap<>();

        Number lastNumber = null;

        int i = 0;
        for(; i < numbers.length; i++){
            Number num = new Number(numbers[i], i+1);
            pastNumbers.put(num.number, num);
            lastNumber = num;
        }

        for(; i < 30000000; i++){
            int num = lastNumber.difference();
            if(pastNumbers.get(num) != null){
                lastNumber = pastNumbers.get(num);
                lastNumber.update(i+1);
            }else{
                Number newNumber = new Number(num, i+1);
                pastNumbers.put(num, newNumber);
                lastNumber = newNumber;
            }
        }   

        return lastNumber.number;
    }

    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);

        int[] numbers = Parser.getIntArray(sc.nextLine(), ",");

        System.out.println(partOne(numbers));
        System.out.println(partTwo(numbers));

        sc.close();
    }
}
