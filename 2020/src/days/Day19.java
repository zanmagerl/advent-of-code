package days;

import java.util.*;

import java.io.*;

import utilities.Array;
import utilities.Parser;

public class Day19{

    private static class Production{
        int start;

        public Production(int start){
            this.start = start;
        }
    }

    private static class TerminalProduction extends Production{

        char terminalSymbol;

        public TerminalProduction(int start, char terminalSymbol){
            super(start);
            this.terminalSymbol = terminalSymbol;
        }

        @Override
        public String toString(){
            return start + " -> " + terminalSymbol;
        }
    }

    private static class NonTerminalProduction extends Production{

        int firstProduction;
        int secondProduction;

        public NonTerminalProduction(int start, int firstProduction, int secondProduction){
            super(start);
            this.firstProduction = firstProduction;
            this.secondProduction = secondProduction;
        }

        @Override
        public String toString(){
            return start + " -> " + firstProduction + " " + secondProduction;
        }
    }

    private static boolean doesMatch(HashMap<Integer, List<Production>> productions, char[] word){

        int n = word.length;

        boolean[][][] table = new boolean[n+1][n+1][productions.keySet().size()];

        //System.out.println(productions.keySet());

        for(int s = 1; s <= n; s++){
            for(int v : productions.keySet()){
                for(Production p : productions.get(v)){
                    if(p instanceof TerminalProduction && ((TerminalProduction)p).terminalSymbol == word[s-1])
                    table[1][s][v] = true; 
                }
            }
        }

        for(int l = 2; l <= n; l++){
            for(int s = 1; s <= n-l+1; s++){
                for(int p = 1; p <= l-1; p++){
                    for(int v : productions.keySet()){
                        if(productions.get(v).get(0) instanceof TerminalProduction) continue;
                        for(Production pro : productions.get(v)){
                            NonTerminalProduction production = (NonTerminalProduction)pro;
                            
                                if(table[p][s][production.firstProduction] && table[l-p][s+p][production.secondProduction]) table[l][s][production.start] = true;
                        }
                    }
                }
            }
        }
        /*
        int count = 0;
        for(boolean[][] b : table){
            for(int i = 0; i < b.length; i++){
                for(int j = 0; j < b[0].length; j++){
                    System.out.print(b[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
        System.out.println(count);
        */
        return table[n][1][0];
    }

    private static int partOne(HashMap<Integer, List<Production>> productions, List<String> messages){

        int result = 0;

        for(String message : messages){

            if(doesMatch(productions, message.toCharArray())) result++;
        }

        return result;
    }


    private static int partTwo(HashMap<Integer, List<Production>> productions, List<String> messages){

        return partOne(productions, messages);
    }

    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);

        HashMap<Integer, List<Production>> productions = new HashMap<>();

        String line = sc.nextLine();

        while(!line.equals("")){
            String[] splittedLine = line.split(":");
            int start = Integer.parseInt(splittedLine[0]);
            productions.put(start, new ArrayList<>());

            if(splittedLine[1].contains("\"")){
                productions.get(start).add(new TerminalProduction(start, splittedLine[1].trim().toCharArray()[1]));
            }else{
                List<int[]> results = new ArrayList<>();

                if(splittedLine[1].contains("|")){
                    String[] prods = splittedLine[1].trim().split("\\|");
                    for(String s : prods){                    
                        results.add(Parser.getIntArray(s.trim(), " "));
                    }
                }else{
                    results.add(Parser.getIntArray(splittedLine[1].trim(), " "));
                }        

                for(int[] r : results){
                    if(r.length == 1){
                        productions.get(start).add(new NonTerminalProduction(start, r[0], -1));
                    }else{
                        productions.get(start).add(new NonTerminalProduction(start, r[0], r[1]));
                    }
                }
            }

            
            line = sc.nextLine();
        }

        for(Integer key : productions.keySet()){
            List<Production> ps = productions.get(key);
            List<Production> newPs = new ArrayList<>();
            for(Production ps1 : ps){
                if(ps1 instanceof NonTerminalProduction && ((NonTerminalProduction)ps1).secondProduction == -1){
                    List<Production> pss = productions.get(((NonTerminalProduction)ps1).firstProduction);
                    for(Production p : pss){
                        if(p instanceof NonTerminalProduction){
                            newPs.add(new NonTerminalProduction(ps1.start, ((NonTerminalProduction)p).firstProduction, ((NonTerminalProduction)p).secondProduction));
                        }else{
                            newPs.add(new TerminalProduction(ps1.start, ((TerminalProduction)p).terminalSymbol));
                        }
                    }
                }else{
                    newPs.add(ps1);
                }
            }
            productions.put(key, newPs);
        }

        List<String> messages = new ArrayList<>();

        while(sc.hasNextLine()){
            line = sc.nextLine();
            messages.add(line);
        }

        System.out.println(partOne(productions, messages));
        
        productions.get(8).add(new NonTerminalProduction(8, 42, 8));
        productions.get(11).add(new NonTerminalProduction(11, 42, productions.keySet().size()));
        productions.put(productions.keySet().size(), new ArrayList<>(Arrays.asList(new Production[]{new NonTerminalProduction(productions.keySet().size(), 11, 31)})));
        
        System.out.println(partTwo(productions, messages));

        sc.close();
    }
}
