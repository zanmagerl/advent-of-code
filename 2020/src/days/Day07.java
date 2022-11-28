package days;

import java.util.*;
import java.io.*;

import utilities.Parser;

public class Day07 {

    private static class Bag{

        String color;
        String text;
        List<Integer> number;
        List<Bag> contains;

        public Bag(String color, String text){
            this.color = color;
            this.text = text;
            number = new ArrayList<>();
            contains = new ArrayList<>();
        }

        public void addBag(Bag b){
            contains.add(b);
        }

        public boolean doesContain(String color){
            if(this.color.equals(color)){
                return true;
            }
            for(Bag b: contains){
                if(b.doesContain(color)){
                    return true;
                }
            }
            return false;
        }

        public int numberOfBags(){
            int result = 0;

            for(int i = 0; i < contains.size(); i++){
                result += number.get(i);
                result += number.get(i) * contains.get(i).numberOfBags();
            }

            return result;
        }
    }

    private static int partOne(HashMap<String, Bag> bags){
        
        int result = 0;

        for(String s : bags.keySet()){
            Bag b = bags.get(s);

            if(b.doesContain("shiny gold") && !s.equals("shiny gold")){
                //System.out.println(b.color);
                result++;
            }

        }

        return result;
    }

    private static int partTwo(HashMap<String, Bag> bags){

        return bags.get("shiny gold").numberOfBags();
    }

    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);

        HashMap<String, Bag> bags = new HashMap<>();

        while(sc.hasNextLine()){
            String line = sc.nextLine();

            String[] splittedString = line.split(" ");
            String c = splittedString[0] + " " + splittedString[1];
            String s = "";
            for(int i = 2; i< splittedString.length; i++){
                s += splittedString[i] + " ";
            }
            bags.put(c, new Bag(c, s));
        }

        for(String c : bags.keySet()){

            Bag b = bags.get(c);

            String t = Parser.removePunctuations(b.text, new String[]{",","."});
            t = Parser.removeWordsFromString(t, new String[]{"bags", "bag", "contain"});
          
            String[] splittedString = t.split(" ");
            
            for(int i = 0; i < splittedString.length; i += 3){
                String num = splittedString[i];

                if(num.equals("no")){
                    continue;
                }else{
                    Integer n = Integer.parseInt(num);
                    b.number.add(n);

                    String color = splittedString[i+1] + " " + splittedString[i+2];
                    b.addBag(bags.get(color));
                }
            }
        }

        System.out.println(partOne(bags));
        System.out.println(partTwo(bags));

        sc.close();
    }
}
