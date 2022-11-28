package days;

import java.util.*;
import java.io.*;

import utilities.*;

public class Day02{


    private static int partOne(List<String> passwordList) throws Exception{

        int numberOfValidPasswords = 0;

        for(String password : passwordList){

            String[] splittedPassword = password.split(" ");

            int[] interval = Parser.getIntArray(splittedPassword[0], "-");
            
            int numberOfRepeats = 0;

            for(char c : splittedPassword[2].toCharArray()){
                if(c == splittedPassword[1].charAt(0)){
                    numberOfRepeats++;
                }
            }
            if(numberOfRepeats >= interval[0] && numberOfRepeats <= interval[1]){
                numberOfValidPasswords++;
            }
        }
        return numberOfValidPasswords;
    }

    private static int partTwo(List<String> passwordList){
        int numberOfValidPasswords = 0;

        for(String password : passwordList){

            String[] splittedPassword = password.split(" ");

            int[] interval = Parser.getIntArray(splittedPassword[0], "-");
            
            char first = splittedPassword[2].toCharArray()[interval[0]-1];
            char second = splittedPassword[2].toCharArray()[interval[1]-1];

            char wanted = splittedPassword[1].charAt(0);

            if(((first == wanted) && (second != wanted)) || ((first != wanted) && (second == wanted))){
                    numberOfValidPasswords++;
            }

        }

        return numberOfValidPasswords;
    }

    public static void main(String[] args) throws Exception{

        Scanner sc = new Scanner(System.in);

        List<String> passwordList = new ArrayList<>();

        while(sc.hasNextLine()){
            passwordList.add(sc.nextLine());
        }

        System.out.println(partOne(passwordList));
        System.out.println(partTwo(passwordList));
        
        sc.close();
    }
}

