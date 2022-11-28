package days;

import java.util.*;
import java.io.*;

public class Day06 {


    private static int partOne(List<List<String>> groups){
        
        int numberOfYes = 0;

        for(List<String> group : groups){

            HashSet<Character> answers = new HashSet<>();
            for(String answer : group){
                for(char c : answer.toCharArray()){
                    answers.add(c);
                }
            }
            numberOfYes += answers.size();
        }

        return numberOfYes;
    }

    private static int partTwo(List<List<String>> groups){

        int numberOfYes = 0;

        for(List<String> group : groups){

            HashMap<Character,Integer> answers = new HashMap();

            for(String answer : group){
                for(char c : answer.toCharArray()){
                    answers.putIfAbsent(c, 0);  
                    answers.put(c, answers.get(c) + 1);
                }
            }
            for(Character c : answers.keySet()){
                if(answers.get(c) == group.size()){
                    numberOfYes++;
                }
            }
        }

        return numberOfYes;   
    }

    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);

        List<List<String>> groups = new ArrayList<>();

        while(sc.hasNextLine()){
            List<String> group = new ArrayList<>();
            String line = sc.nextLine();
            group.add(line);
            while(!line.equals("") && sc.hasNextLine()){
                if(sc.hasNextLine()){
                    line = sc.nextLine();
                    if(!line.equals("")) group.add(line);
                }
            }
            groups.add(group);
        }

        System.out.println(partOne(groups));
        System.out.println(partTwo(groups));

        sc.close();
    }
}
