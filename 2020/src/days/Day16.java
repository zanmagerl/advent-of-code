package days;

import java.util.*;

import java.io.*;

import utilities.Parser;

public class Day16{

    private static class TicketField{
        
        String name;
        int startFirst;
        int endFirst;
        int startSecond;
        int endSecond;

        public TicketField(String name, int startFirst, int endFirst, int startSecond, int endSecond){
            this.name = name;
            this.startFirst = startFirst;
            this.endFirst = endFirst;
            this.startSecond = startSecond;
            this.endSecond = endSecond;
        }

        public boolean isValid(int number){
            return (number >= this.startFirst && number <= this.endFirst) || (number >= this.startSecond && number <= this.endSecond);
        }
    }

    private static int partOne(List<TicketField> ticketFields, List<int[]> tickets){

        int sumOfInvalidNumbers = 0;

        for(int[] ticket : tickets){
            for(int t : ticket){
                boolean isValid = false;
                for(TicketField f : ticketFields){
                    if(f.isValid(t)){
                        isValid = true;
                        break;
                    }
                }
                if(!isValid) sumOfInvalidNumbers += t;
            }
        }

        return sumOfInvalidNumbers;
    }


    private static long partTwo(List<TicketField> ticketFields, List<int[]> tickets, int[] myTicket){

        List<int[]> validTickets = new ArrayList<>();

        for(int[] ticket : tickets){
            boolean isValid = true;
            for(int t : ticket){
                boolean isFieldValid = false;
                for(TicketField f : ticketFields){
                    if(f.isValid(t)){
                        isFieldValid = true;
                        break;
                    }
                }
                if(!isFieldValid) isValid = false;
            }
            if(isValid) validTickets.add(ticket);
        }

        validTickets.add(myTicket);

        HashMap<TicketField, Integer> mappings = new HashMap<>();


        /**
         * `incorrect` contains at index i a list of field indexes that cannot be at position i of the ticket
         */
        List<List<Integer>> incorrect = new ArrayList<>(); 

        for(int i : validTickets.get(0)){
            incorrect.add(new ArrayList<>());
        }

        for(int[] validTicket : validTickets){
            for(int t = 0; t < validTicket.length; t++){
                for(int i = 0; i < ticketFields.size(); i++){
                    if(!ticketFields.get(i).isValid(validTicket[t])){
                        incorrect.get(t).add(i);
                    }
                }
            }
        }
        
        do{
            for(int j = 0; j < incorrect.size(); j++){
                List<Integer> invalidFieldsForPositionJ = incorrect.get(j);
                if(ticketFields.size() - (invalidFieldsForPositionJ.size() + mappings.size()) == 1){
                    for(int i = 0; i < ticketFields.size(); i++){
                        if(!invalidFieldsForPositionJ.contains(i) && !mappings.containsKey(ticketFields.get(i))){
                            mappings.put(ticketFields.get(i), j);
                        }
                    }
                }
            }
        }while(mappings.size() != ticketFields.size());
        
        long result = 1;

        for(TicketField key : mappings.keySet()){
            if(key.name.startsWith("departure")) result *= myTicket[mappings.get(key)];
        }

        return result;
    }

    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);

        List<TicketField> ticketFields = new ArrayList<>();
        List<int[]> tickets = new ArrayList<>();


        String line = sc.nextLine();

        while(!line.equals("")){
            String[] numberValues = line.split(":");
            String[] values = numberValues[1].split("or");
            int[] first = Parser.getIntArray(values[0].trim(), "-");
            int[] second = Parser.getIntArray(values[1].trim(), "-");

            ticketFields.add(new TicketField(numberValues[0], first[0], first[1], second[0], second[1]));
            line = sc.nextLine();
        }

        sc.nextLine();

        int[] myTicket = Parser.getIntArray(sc.nextLine(), ",");

        sc.nextLine();
        sc.nextLine();

        while(sc.hasNextLine()){
            tickets.add(Parser.getIntArray(sc.nextLine(), ","));
        }

        System.out.println(partOne(ticketFields, tickets));
        System.out.println(partTwo(ticketFields, tickets, myTicket));

        sc.close();
    }
}
