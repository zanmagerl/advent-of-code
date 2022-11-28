package days;

import java.util.*;

import java.io.*;

import utilities.Parser;

public class Day17{


    private static class Key{

        int x;
        int y;
        int z;

        public Key(int x, int y, int z){
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Key key = (Key) o;
            return x == key.x && y == key.y && z == key.z;
        }

        @Override
        public int hashCode(){
            return x + y * 100 + z * 10000;
        }

        @Override
        public String toString(){
            return String.format("z: %d, y: %d, x: %d", this.z, this.y, this.x);
        }
    }

    private static class HyperKey extends Key{

        int w;

        public HyperKey(int x, int y, int z, int w){
            super(x,y,z);
            this.w = w;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            HyperKey key = (HyperKey) o;
            return x == key.x && y == key.y && z == key.z && w == key.w;
        }

        @Override
        public int hashCode(){
            return x + y * 100 + z * 10000 + w * 1000000;
        }
    }

    

    private static int countNeighbors(HashMap<Key,Boolean> state, int x, int y, int z){

        int num = 0;

        int[] directions = {-1, 0, 1};
        for(int zi = 0; zi < directions.length; zi++){
            for(int yi = 0; yi < directions.length; yi++){
                for(int xi = 0; xi < directions.length; xi++){
                    Key key = new Key(x+directions[xi], y+directions[yi], z+directions[zi]); 
                    if(state.get(key) != null && state.get(key) == true && !(xi == 1 && yi == 1 && zi == 1)) num++;
                }
            }
        }
        return num;
    }

    private static int countNeighbors(HashMap<Key,Boolean> state, int x, int y, int z, int w){

        int num = 0;

        int[] directions = {-1, 0, 1};
        for(int wi = 0; wi < directions.length; wi++){
            for(int zi = 0; zi < directions.length; zi++){
                for(int yi = 0; yi < directions.length; yi++){
                    for(int xi = 0; xi < directions.length; xi++){
                        Key key = new HyperKey(x+directions[xi], y+directions[yi], z+directions[zi], w+directions[wi]); 
                        if(state.get(key) != null && state.get(key) == true && !(xi == 1 && yi == 1 && zi == 1 && wi == 1)) num++;
                    }
                }
            }
        }
        return num;
    }


    private static HashMap<Key, Boolean> applyCycle(HashMap<Key, Boolean> state, int iteration, int width, int height){
            
        HashMap<Key, Boolean> newState = new HashMap<>();

        for(int zi = -iteration; zi <= iteration; zi++){
            for(int yi = - iteration; yi < height + iteration; yi++){
                for(int xi = - iteration; xi < width + iteration; xi++){
                    int numNeighbors = countNeighbors(state, xi, yi, zi);
                    Key key = new Key(xi, yi, zi);
                    if(state.get(key) == null){
                        newState.put(key, (numNeighbors == 3) ? true : false);
                    }else{
                        if(state.get(key) == true){
                            newState.put(key, (numNeighbors == 2 || numNeighbors == 3) ? true : false);
                        }else{
                            newState.put(key, (numNeighbors == 3) ? true : false);
                        }
                    }
                }
            }
        }

        return newState;
    }

    private static HashMap<Key, Boolean> applyCyclePartTwo(HashMap<Key, Boolean> state, int iteration, int width, int height){
            
        HashMap<Key, Boolean> newState = new HashMap<>();
        for(int wi = -iteration; wi <= iteration; wi++){
            for(int zi = -iteration; zi <= iteration; zi++){
                for(int yi = - iteration; yi < height + iteration; yi++){
                    for(int xi = - iteration; xi < width + iteration; xi++){
                        int numNeighbors = countNeighbors(state, xi, yi, zi, wi);
                        HyperKey key = new HyperKey(xi, yi, zi, wi);
                        if(state.get(key) == null){
                            newState.put(key, (numNeighbors == 3) ? true : false);
                        }else{
                            if(state.get(key) == true){
                                newState.put(key, (numNeighbors == 2 || numNeighbors == 3) ? true : false);
                            }else{
                                newState.put(key, (numNeighbors == 3) ? true : false);
                            }
                        }
                    }
                }
            }
        }

        return newState;
    }


    private static int countActive(HashMap<Key,Boolean> state){
        
        int count = 0;
        
        for(Key key : state.keySet()){
            if(state.get(key) == true){
                count++;
            }
        }

        return count;
    }

    private static int partOne(char[][] initialState){

        HashMap<Key,Boolean> state = new HashMap<>();

        for(int i = 0; i < initialState.length; i++){
            for(int j = 0; j < initialState[i].length; j++){
                state.put(new Key(i, j, 0), initialState[i][j] == '#');
            }
        }

        for(int i = 1; i <= 6; i++){
            state = applyCycle(state, i, initialState[0].length, initialState.length);
        }

        return countActive(state);
    }


    private static int partTwo(char[][] initialState){

        HashMap<Key,Boolean> state = new HashMap<>();

        for(int i = 0; i < initialState.length; i++){
            for(int j = 0; j < initialState[i].length; j++){
                state.put(new HyperKey(i, j, 0, 0), initialState[i][j] == '#');
            }
        }

        for(int i = 1; i <= 6; i++){
            state = applyCyclePartTwo(state, i, initialState[0].length, initialState.length);
        }

        return countActive(state);
    }

    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);

        char[][] initialState = Parser.get2DCharArray(sc);

        System.out.println(partOne(initialState));
        System.out.println(partTwo(initialState));

        sc.close();
    }
}
