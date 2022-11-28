package utilities;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Parser{

    public static char[][] get2DCharArray(Scanner sc){
        
        List<String> lines = new ArrayList<>();

        while(sc.hasNextLine()){
            lines.add(sc.nextLine());
        }

        char[][] charArray = new char[lines.size()][lines.get(0).length()];

        int i = 0;
        for(String line : lines){
            charArray[i] = line.toCharArray();
            i++;
        }

        return charArray;
    }

    public static char[][] get2DCharArray(Scanner sc, String endMark){
        
        List<String> lines = new ArrayList<>();
        
        String l = sc.nextLine();
        
        while(!l.equals(endMark)){
            lines.add(l);
            if(!sc.hasNextLine()){
                break;
            }
            l = sc.nextLine();
        }

        char[][] charArray = new char[lines.size()][lines.get(0).length()];

        int i = 0;
        for(String line : lines){
            charArray[i] = line.toCharArray();
            i++;
        }

        return charArray;
    }

    public static int[] getIntArray(Scanner sc){
        List<Integer> list = new ArrayList<>();

        while(sc.hasNextLine()){
            String line = sc.nextLine();
            list.add(Integer.parseInt(line));
        }

        int[] result = new int[list.size()];

        for(int i = 0; i < list.size(); i++){
            result[i] = list.get(i);
        }

        return result;
    }

    public static int[] getIntArray(String s, String delimeter){
        String[] splittedString = s.split(delimeter);

        int[] result = new int[splittedString.length];

        for(int i = 0; i < splittedString.length; i++){
            result[i] = Integer.parseInt(splittedString[i]);
        }

        return result;
    }

    public static String removeWordsFromString(String s, String[] words){

        List<String> wordsList = new ArrayList<>(Arrays.asList(words));

        String[] stringWords = s.split(" ");

        StringBuffer buffer = new StringBuffer(); 

        for(String word : stringWords){
            if(!wordsList.contains(word)){
                if(buffer.length() != 0){
                    buffer.append(" ");
                }
                buffer.append(word);
            }
        }
        return buffer.toString();
    }

    public static String removePunctuations(String s, String[] punctuations){

        for(String punctuation : punctuations){
            s = s.replace(punctuation, "");
        }

        return s;
    }

}