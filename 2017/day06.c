#include <stdio.h>
#include <stdlib.h>


typedef struct debugger{
    int** history;
    int numberOfRedistributions;
}debugger;

int findMax(int* array, int size){
    
    int iMax = 0;

    for(int i = 1; i < size; i++){
        if(array[i] > array[iMax]){
            iMax = i;
        }
    }
    return iMax;
}

int arraysAreTheSame(int* a, int* b, int size){
    for(int i = 0; i < size; i++){
        if(a[i] != b[i]){
            return 0;
        }
    }
    return 1;
}

int hasNotBeenSeenBefore(int** history, int numberOfRedistributions, int numberOfBlocks){

    for(int i = 0; i < numberOfRedistributions - 1; i++){
        for(int j = i+1; j < numberOfRedistributions; j++){

            if(arraysAreTheSame(history[i], history[j], numberOfBlocks)){

                return 0;
            }

        }
    }

    return 1;
}

void printArray(int* array, int size){
    for(int i = 0; i < size; i++){
        printf("%d ", array[i]);
    }
    printf("\n");
}

debugger* partOne(int* input, int numberOfBlocks){

    int sizeOfHistory = 1;
    int** history = malloc(sizeof(int*) * sizeOfHistory);

    history[0] = malloc(sizeof(int) * numberOfBlocks);
    
    for(int i = 0; i < numberOfBlocks; i++){
        history[0][i] = input[i];
    }
    int i;
    for(i = 0; hasNotBeenSeenBefore(history, i+1, numberOfBlocks) ; i++){

        int indexOfMax = findMax(history[i], numberOfBlocks);

        int value = history[i][indexOfMax];
        
        int* newArray = malloc(sizeof(int) * numberOfBlocks);
        
        for(int j = 0; j < numberOfBlocks; j++){
            newArray[j] = history[i][j];
        }

        newArray[indexOfMax] = 0;
        //printf("%d\n", value);
        for(int j = 0; j < value; j++){
            newArray[(indexOfMax + (j+1)) % numberOfBlocks]++;
        }
        //printArray(newArray, numberOfBlocks);

        if(i + 1 >= sizeOfHistory){
            sizeOfHistory *= 2;
            history = realloc(history, sizeOfHistory * sizeof(int*));
        }
        history[i+1] = malloc(sizeof(int) * numberOfBlocks);
        history[i+1] = newArray;
    }
    
    debugger* result = malloc(sizeof(debugger*));
    result->history = malloc(sizeof(int**));
    result->history = history;
    result->numberOfRedistributions = i+1;

    return result;
}

int partTwo(debugger* result, int numberOfBlocks){

    for(int i = 0; i < result->numberOfRedistributions - 1; i++){
        for(int j = i+1; j < result->numberOfRedistributions; j++){

            if(arraysAreTheSame(result->history[i], result->history[j], numberOfBlocks)){
                return j-i;
            }

        }
    }

    return 1;
}

int main(){

    FILE* file = fopen("input06.txt", "r");

    int numberOfBlocks = 16;
    int input[numberOfBlocks];
    

    for(int i = 0; fscanf(file, "%d", input+i) != -1; i++);

    debugger* result = partOne(input, numberOfBlocks);

    printf("%d\n", result->numberOfRedistributions-1);
    printf("%d\n", partTwo(result, numberOfBlocks));

    fclose(file);
    return 0;
}