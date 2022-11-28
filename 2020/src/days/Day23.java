package days;

import java.util.*;
import java.util.stream.Collectors;

public class Day23{

    private static int PART1_NUMBER_OF_ROUNDS = 100;
    private static int PART2_NUMBER_OF_ROUNDS = 10000000;


    private static String extractSolution(CircularList<Integer> cups){

        StringBuilder stringBuilder = new StringBuilder();

        int index = cups.indexOf(Integer.valueOf(1));

        int currIndex = index + 1;
        while(cups.get(currIndex) != 1){
            stringBuilder.append(cups.get(currIndex++));
        }

        return stringBuilder.toString();
    }

    private static int findDestination(int current, List<Integer> pickedUp, int max){

        int destination = current;

        do{
            destination--;

            if(destination < 1){
                destination = max;
            }
        }while(pickedUp.contains(destination));

        return destination;
    }

    private static String partOne(CircularList<Integer> cups){

        int currentIndex = 0;

        for(int i = 0; i < PART1_NUMBER_OF_ROUNDS; i++){
            int current = cups.get(currentIndex);

            List<Integer> pickedUp = new ArrayList<>(Arrays.asList(cups.remove(cups.indexOf(current)+1), cups.remove(cups.indexOf(current)+1), cups.remove(cups.indexOf(current)+1)));

            int destinationCup = findDestination(current, pickedUp, 9);
            int destinationIndex = cups.indexOf(destinationCup);

            for(Integer pickedUpCup : pickedUp){
                cups.add(++destinationIndex, pickedUpCup);
            }

            currentIndex = cups.indexOf(current) + 1;

            currentIndex %= cups.size();

        }

        return extractSolution(cups);
    }

    private static String partTwo(CircularList<Integer> cups){

        int currentIndex = 0;

        for(int i = 0; i < PART1_NUMBER_OF_ROUNDS; i++){
            System.out.println(i);
            int current = cups.get(currentIndex);

            List<Integer> pickedUp = new ArrayList<>(Arrays.asList(cups.remove(cups.indexOf(current)+1), cups.remove(cups.indexOf(current)+1), cups.remove(cups.indexOf(current)+1)));

            int destinationCup = findDestination(current, pickedUp, 1000000);
            int destinationIndex = cups.indexOf(destinationCup);

            for(Integer pickedUpCup : pickedUp){
                cups.add(++destinationIndex, pickedUpCup);
            }

            currentIndex = cups.indexOf(current) + 1;

            currentIndex %= cups.size();

        }

        return extractSolution(cups);
    }

    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);

        String input = sc.nextLine();

        sc.close();

        CircularList<Integer> cups = new CircularList<Integer>();
        cups.addAll(Arrays.stream(input.split(""))
                        .mapToInt(Integer::parseInt)
                        .boxed()
                        .collect(Collectors.toList()));

        System.out.println(partOne(cups));

        List<Integer> longerCups = new ArrayList<>();
        longerCups.addAll(Arrays.asList(input.split("")).stream().mapToInt(Integer::parseInt).boxed().collect(Collectors.toList()));
        for(int i = 10; i < 1000000; i++){
            longerCups.add(i);
        }

        cups = new CircularList<>();
        cups.addAll(longerCups);

        System.out.println(partTwo(cups));

    }
}