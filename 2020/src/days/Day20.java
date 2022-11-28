package days;

import java.util.*;

import java.io.*;

import utilities.Array;
import utilities.Parser;

public class Day20{

    private static class Tile{

        int id;
        char[][] image;

        int[] borders;
        char[][][] orientations;

        Tile[] neighbors;
        int flipConfiguration;
        int rotationConfiguration;


        public Tile(int id, char[][] image){
            this.id = id;
            this.image = image;

            this.neighbors = new Tile[4];
            this.flipConfiguration = -1;
            this.rotationConfiguration = -1;

            calculateOrientations();
        }

        public void addNeighbor(Tile t){
            this.neighbors[this.getNumNeighbors()] = t;
        }

        private void calculateOrientations(){

            int n = image.length;
            int numFlips = 4;

            borders = new int[16];
            orientations = new char[n][n][numFlips*4];

            char[][] iter = image;

            for(int j = 0; j < numFlips; j++){

                for(int i = 0; i < iter[0].length; i++){
                    if(iter[0][i] == '#') borders[0+j*4] += (int)Math.pow(2,(n-1)-i);
                }
    
                for(int i = iter.length-1; i > -1; i--){
                    if(iter[i][0] == '#') borders[1+j*4] += (int)Math.pow(2,(n-1)-i);
                }
    
                for(int i = iter[n-1].length - 1; i > -1; i--){
                    if(iter[n-1][i] == '#') borders[2+j*4] += (int)Math.pow(2,(n-1)-i);
                }
    
                for(int i = 0; i < iter.length; i++){
                    if(iter[i][n-1] == '#') borders[3+j*4] += (int)Math.pow(2,(n-1)-i);
                
                }



                iter = flip(image, j);
            }
        }

        private static char[][] flip(char[][] init, int index){
            
            char[][] iter = Array.copy(init);
            
            if(index == 0 || index == 2){
                for(int i = 0; i < iter.length; i++){
                    for(int j = 0; j < iter[0].length/2; j++){
                        char temp = iter[i][j];
                        iter[i][j] = iter[i][iter[0].length - j - 1];
                        iter[i][iter[0].length - j - 1] =  temp;
                    }
                }
            }

            if(index == 1 || index == 2){
                for(int i = 0; i < iter[0].length; i++){
                    for(int j = 0; j < iter.length/2; j++){
                        char temp = iter[j][i];
                        iter[j][i] = iter[iter.length - j - 1][i];
                        iter[iter.length - j - 1][i] = temp;
                    }
                }
            }

            return iter;
        }

        public int getBorderIndex(int border){
            return -1;
        }

        public int getNumNeighbors(){
            int num = 0;
            for(Tile t : neighbors){
                if(t != null) num++;
            }
            return num;
        }

        public boolean alreadyNeigbor(Tile t){
            for(Tile n : neighbors){
                if(t == n) return true;
            }
            return false;
        }

        @Override
        public String toString(){
            String neigh = "";
            for(Tile t : neighbors){
                if(t == null) neigh += "null ";
                else neigh += t.id + " ";
            }
            return String.format("ID: %d, flipOrientation: %d, %s", this.id, this.flipConfiguration, neigh);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Tile tile = (Tile) o;
            return tile.id == this.id;
        }

        @Override
        public int hashCode(){
            return this.id;
        }
    }

    private static List<Tile> findCorners(List<Tile> tiles){
        
        List<Tile> corners = new ArrayList<>();

        for(Tile t : tiles){
            int pairs = 0;

            HashSet<Integer> nonmatchIndexes = new HashSet<>();

            for(int i = 0; i < t.borders.length; i++){
                int b = t.borders[i];
                for(Tile t1: tiles){
                    if(t != t1){
                        for(int b1 : t1.borders){
                            if(b == b1) pairs++;
                            else nonmatchIndexes.add(i);
                        }
                    }
                }
            }
            if(pairs == 16){

                System.out.println(nonmatchIndexes);

                corners.add(t);
            }
        }

        return corners;
    }

    private static char[][] createImage(Tile start, List<Tile> tiles){

        int n = (int)Math.sqrt(tiles.size());

        start.flipConfiguration = 0;
        start.rotationConfiguration = 0;

        Tile[][] puzzle = new Tile[n][n];

        for(int row = 0; row < n; row++){
            for(int column = 0; column < n; column++){
                if(row == 0 && column == 0){
                    puzzle[row][column] = start;
                }else{
                    //for(int i = 0; i < )
                }
            }
        }

        for(int i = 0; i < n ; i++){
            for(int j = 0; j < n; j++){
                System.out.println(puzzle[i][j]);
            }
        }        

        return null;
    }

    private static long partOne(List<Tile> tiles){

        long result = 1;

        List<Tile> corners = findCorners(tiles);

        for(Tile corner : corners){
            result *= corner.id;
        }

        return result;
    }


    private static int partTwo(List<Tile> tiles){

        List<Tile> corners = findCorners(tiles);

        char[][] image = createImage(corners.get(0), tiles);

        return 0;
    }

    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);

        List<Tile> tiles = new ArrayList<>();

        while(sc.hasNextLine()){
            int id = Integer.parseInt(sc.nextLine().split(" ")[1].replace(":", ""));
            tiles.add(new Tile(id, Parser.get2DCharArray(sc, "")));
        }
        /*
        for(Tile t : tiles){
            System.out.println(t.id);
            Array.printArray(t.image);
        }*/

        System.out.println(partOne(tiles));
        System.out.println(partTwo(tiles));

        sc.close();
    }
}
