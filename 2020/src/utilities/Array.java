package utilities;

public class Array {

    public static char[][] copy(char[][] arr){
        char[][] array = new char[arr.length][arr[0].length];

        for(int i = 0; i < arr.length; i++){
            for(int j = 0; j < arr[0].length; j++){
                array[i][j] = arr[i][j];
            }
        }
        return array;
    }

    public static void printArray(char[][] arr){
        for(int i = 0; i < arr.length; i++){
            for(int j = 0; j < arr[0].length; j++){
                System.out.print(arr[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
    
}
