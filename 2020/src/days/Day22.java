package days;

import java.util.*;

public class Day22{

    private static int calculateScore(Queue<Integer> cards){
        int score = 0;

        while(!cards.isEmpty()){
            score += cards.size() * cards.poll();
        }

        return score;
    }

    private static Queue<Integer> copyDeck(Queue<Integer> cards, int numberOfCards){
        Queue<Integer> copiedDeck = new LinkedList<>();

        for(int i = 0; i < numberOfCards; i++){
            int card = cards.poll();
            copiedDeck.add(card);
            cards.add(card);
        }
        for(int j = numberOfCards; j < cards.size(); j++){
            int card = cards.poll();
            cards.add(card);
        }

        return copiedDeck;
    }

    private static int partOne(Queue<Integer> cardsPlayerOne, Queue<Integer> cardsPlayerTwo){

        while(!cardsPlayerOne.isEmpty() && !cardsPlayerTwo.isEmpty()){
            int cardOne = cardsPlayerOne.poll();
            int cardTwo = cardsPlayerTwo.poll();

            if(cardOne > cardTwo){
                cardsPlayerOne.add(cardOne);
                cardsPlayerOne.add(cardTwo);
            }else{
                cardsPlayerTwo.add(cardTwo);
                cardsPlayerTwo.add(cardOne);
            }
        }

        return calculateScore(cardsPlayerOne.isEmpty() ? cardsPlayerTwo : cardsPlayerOne);
    }


    private static int playGame(Queue<Integer> cardsPlayerOne, Queue<Integer> cardsPlayerTwo){
        List<String> previousConfigurations = new LinkedList<>();

        while(!cardsPlayerOne.isEmpty() && !cardsPlayerTwo.isEmpty()){

            String configuration = cardsPlayerOne.toString() + "|" + cardsPlayerTwo.toString();

            if (previousConfigurations.contains(configuration)) {
                return 1;
            }

            previousConfigurations.add(configuration);

            int cardOne = cardsPlayerOne.poll();
            int cardTwo = cardsPlayerTwo.poll();
            if (cardOne <= cardsPlayerOne.size() && cardTwo <= cardsPlayerTwo.size()){
                int winner = playGame(copyDeck(cardsPlayerOne, cardOne), copyDeck(cardsPlayerTwo, cardTwo));
                if (winner == 1){
                    cardsPlayerOne.add(cardOne);
                    cardsPlayerOne.add(cardTwo);
                } else {
                    cardsPlayerTwo.add(cardTwo);
                    cardsPlayerTwo.add(cardOne);
                }
            } else {
                if(cardOne > cardTwo){
                    cardsPlayerOne.add(cardOne);
                    cardsPlayerOne.add(cardTwo);
                }else{
                    cardsPlayerTwo.add(cardTwo);
                    cardsPlayerTwo.add(cardOne);
                }
            }
        }
        return cardsPlayerOne.size() > cardsPlayerTwo.size() ? 1 : 2;
    }

    private static int partTwo(Queue<Integer> cardsPlayerOne, Queue<Integer> cardsPlayerTwo){

        playGame(cardsPlayerOne, cardsPlayerTwo);

        return calculateScore(cardsPlayerOne.isEmpty() ? cardsPlayerTwo : cardsPlayerOne);
    }

    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);
        
        Queue<Integer> cardsPlayerOne = new LinkedList<>();
        Queue<Integer> cardsPlayerTwo = new LinkedList<>();
        
        boolean playerOne = true;

        while(sc.hasNextLine()){
            String line = sc.nextLine();
            if(line.equals("")){
                playerOne = false;
                continue;
            }else if(line.contains("Player")) continue;

            if(playerOne){
                cardsPlayerOne.add(Integer.parseInt(line));
            }else{
                cardsPlayerTwo.add(Integer.parseInt(line));
            }
        }

        System.out.println(partOne(copyDeck(cardsPlayerOne, cardsPlayerOne.size()), copyDeck(cardsPlayerTwo, cardsPlayerTwo.size())));
        System.out.println(partTwo(copyDeck(cardsPlayerOne, cardsPlayerOne.size()), copyDeck(cardsPlayerTwo, cardsPlayerTwo.size())));

        sc.close();
    }
}
