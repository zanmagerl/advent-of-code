package days;

import java.util.*;

import java.io.*;

import utilities.Parser;

public class Day12{


    private static class Instruction{

        String action;
        int value;

        public Instruction(String action, int value){
            this.action = action;
            this.value = value;
        }

    }

    private static String changeDirection(String origin, String direction, int degrees){

        String[] values = {"N", "W", "S", "E"};
        int index = 0;
        for(int i = 0; i < values.length; i++){
            if(values[i].equals(origin)){
                index = i;
            }
        }

        if(direction.equals("L")){
            return values[((index + degrees/90) + 4) % 4];
        }else{
            return values[((index - degrees/90) + 4) % 4];
        }

    }

    private static int[] rotateWaypoint(int x, int y, String direction, int degrees){

        int result[] = {x,y};

        for(int i = 0; i < degrees; i += 90){

            if(direction.equals("L")){
                int temp = result[0];
                result[0] = result[1];
                result[1] = temp;
                result[0] = - result[0];
            }else{
                int temp = result[0];
                result[0] = result[1];
                result[1] = temp;
                result[1] = - result[1];     
            }
        }
        
        return result;
    }


    private static int partOne(List<Instruction> instructions){
        
        String direction = "E";
        int x = 0;
        int y = 0;

        for(Instruction instruction : instructions){
            switch(instruction.action){
                case "N":
                    y += instruction.value;
                    break;
                case "S": 
                    y -= instruction.value;
                    break;
                case "E": 
                    x += instruction.value;
                    break;
                case "W":
                    x -= instruction.value;
                    break; 
                case "F":
                    switch(direction){
                        case "N":
                        y += instruction.value;
                        break;
                    case "S": 
                        y -= instruction.value;
                        break;
                    case "E": 
                        x += instruction.value;
                        break;
                    case "W":
                        x -= instruction.value;
                        break;
                        }
                    break;
                case "L":
                    direction = changeDirection(direction, "L", instruction.value);
                    break;
                case "R":
                    direction = changeDirection(direction, "R", instruction.value);
                    break;
            }
        }

        return Math.abs(x) + Math.abs(y);
    }


    private static int partTwo(List<Instruction> instructions){

        int xShip = 0;
        int yShip = 0;

        int xWayPoint = 10;
        int yWayPoint = 1;

        for(Instruction instruction : instructions){
            switch(instruction.action){
                case "N":
                    yWayPoint += instruction.value;
                    break;
                case "S": 
                    yWayPoint -= instruction.value;
                    break;
                case "E": 
                    xWayPoint += instruction.value;
                    break;
                case "W":
                    xWayPoint -= instruction.value;
                    break; 
                case "F":
                    xShip += instruction.value * xWayPoint;
                    yShip += instruction.value * yWayPoint;
                    break;
                case "L":
                    int[] pos = rotateWaypoint(xWayPoint, yWayPoint, "L", instruction.value);
                    xWayPoint = pos[0];
                    yWayPoint = pos[1];
                    break;
                case "R":
                    int[] pos2 = rotateWaypoint(xWayPoint, yWayPoint, "R", instruction.value);
                    xWayPoint = pos2[0];
                    yWayPoint = pos2[1];
                    break;
             }

        }

        return Math.abs(xShip) + Math.abs(yShip);
    }

    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);
        
        List<Instruction> instructions = new ArrayList<>();

        while(sc.hasNextLine()){
            String line = sc.nextLine();

            String action = line.substring(0,1);
            int value = Integer.parseInt(line.substring(1));

            instructions.add(new Instruction(action, value));
        }

        System.out.println(partOne(instructions));
        System.out.println(partTwo(instructions));

        sc.close();
    }
}
