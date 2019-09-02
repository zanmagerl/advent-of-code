#include <stdio.h>
#include <stdlib.h>

#define MAX_NUMBER_OF_INSTRUCTIONS 2000

typedef struct input{
    int* instructions;
    int numberOfInstructions;
}input;

input* copyOfInput(input* myInput){
    
    input* duplicate = malloc(sizeof(input*));
    duplicate->instructions = malloc(myInput->numberOfInstructions * sizeof(int));

    for(int i = 0; i < myInput->numberOfInstructions; i++){
        duplicate->instructions[i] = myInput->instructions[i];
    }

    duplicate->numberOfInstructions = myInput->numberOfInstructions;

    return duplicate;
}

int partOne(int* instructions, int numberOfInstructions){

    int pc = 0;

    int numberOfSteps = 0;

    while(pc < numberOfInstructions){
        int instruction = instructions[pc];
        instructions[pc]++;
        pc += instruction;
        numberOfSteps++;
    }

    return numberOfSteps;
}

int partTwo(int* instructions, int numberOfInstructions){

    int pc = 0;

    int numberOfSteps = 0;

    while(pc < numberOfInstructions){
        
        int instruction = instructions[pc];
        
        if(instruction >= 3){
            instructions[pc]--;
        }else{
            instructions[pc]++;
        }
        pc += instruction;
        numberOfSteps++;
    }

    return numberOfSteps;
}

int main(){

    input* myInput = malloc(sizeof(input*));
    myInput->instructions = malloc(MAX_NUMBER_OF_INSTRUCTIONS * sizeof(int));

    FILE* file = fopen("input05.txt", "r");
    
    int number = 0;
    int counter = 0;

    while(fscanf(file, "%d", &number) != -1){
        myInput->instructions[counter] = number;
        counter++;
    }

    myInput->numberOfInstructions = counter;

    input* duplicate = copyOfInput(myInput);

    printf("%d\n", partOne(myInput->instructions, myInput->numberOfInstructions));
    printf("%d\n", partTwo(duplicate->instructions, duplicate->numberOfInstructions));

    return 0;
}