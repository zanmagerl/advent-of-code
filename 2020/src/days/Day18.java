package days;

import java.util.*;

import java.io.*;

import utilities.Parser;

public class Day18{

    private static long[] evaluate(char[] expression, int index){

        long result  = 0;

        char lastOperator = '+';
        int i = index;
        for(; i < expression.length; i++){
            if(expression[i] == '+'){
                lastOperator = '+';
            }else if(expression[i] == '*'){
                lastOperator = '*';
            }else if(expression[i] == '('){
                long[] evaluation = evaluate(expression, i+1);
                if(lastOperator == '*') result *= evaluation[0];
                else result += evaluation[0];
                i = (int)evaluation[1];
            }else if(expression[i] == ')'){
                return new long[]{result, i};
            }else{
                if(lastOperator == '*'){
                    result *= expression[i] - '0';
                }else{
                    result += expression[i] - '0';
                }
            }
        }

        return new long[]{result, i};
    }

    private static long[] evaluatePartTwo(char[] expression, int index, int depth, boolean paranthesisDive){

        long result  = 0;

        char lastOperator = '+';
        int i = index;
        for(; i < expression.length; i++){
            if(expression[i] == '+'){
                lastOperator = '+';
            }else if(expression[i] == '*'){
                if(depth > 0 && !paranthesisDive) return new long[]{result, i-1};
                lastOperator = '*';
                long[] evaluation = evaluatePartTwo(expression, i+1, depth + 1, false);
                result *= evaluation[0];
                i = (int)evaluation[1];
            }else if(expression[i] == '('){
                long[] evaluation = evaluatePartTwo(expression, i+1, depth+1, true);
                if(lastOperator == '*') result *= evaluation[0];
                else result += evaluation[0];
                i = (int)evaluation[1];
            }else if(expression[i] == ')'){
                if(depth > 0 && !paranthesisDive) return new long[]{result, i-1};
                else return new long[]{result, i};
            }else{
                if(lastOperator == '*'){
                    result *= expression[i] - '0';
                }else{
                    result += expression[i] - '0';
                }
            }
        }
        return new long[]{result, i};
    }


    private static long partOne(List<char[]> expressions){

        long sum = 0;

        for(char[] expression : expressions){
            sum += evaluate(expression, 0)[0];
        }

        return sum;
    }


    private static long partTwo(List<char[]> expressions){

        long sum = 0;

        for(char[] expression : expressions){
            sum += evaluatePartTwo(expression, 0, 0, true)[0];
        }

        return sum;
    }

    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);

        List<char[]> expressions = new ArrayList<>();

        while(sc.hasNextLine()){
            String line = sc.nextLine();
            char[] tokens = line.replaceAll(" ", "").toCharArray();
            expressions.add(tokens);
        }

        System.out.println(partOne(expressions));
        System.out.println(partTwo(expressions));

        sc.close();
    }
}
