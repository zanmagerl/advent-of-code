#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <limits.h>

#include "hash_table.h"

#define MAX_NUMBER_OF_INSTRUCTIONS 1000
#define MAX_NAME_LENGTH 10

typedef struct instruction{
    char registerName[MAX_NAME_LENGTH];
    int mode;
    int value;
    char conditionRegister[MAX_NAME_LENGTH];
    char condition[3];
    int conditionValue;
}instruction;

int numberOfInstructions;

instruction** parse(FILE* file){

    instruction** instructions = malloc(MAX_NUMBER_OF_INSTRUCTIONS * sizeof(instruction*));

    instruction* instr = malloc(sizeof(instruction));

    int i;
    for(i = 0; fscanf(file, "%s", instr->registerName) != EOF ; i++){

        char mode[4];
        fscanf(file, "%s", mode);
        if(strcmp(mode, "inc") == 0){
            instr->mode = 1;
        }else{
            instr->mode = -1;
        }

        fscanf(file, "%d", &instr->value);

        char trash[3];
        fscanf(file, "%s", trash);

        fscanf(file, "%s", instr->conditionRegister);

        fscanf(file, "%s", instr->condition);

        fscanf(file, "%d", &instr->conditionValue);

        instructions[i] = malloc(sizeof(instruction*));
        instructions[i] = instr;
        instr = malloc(sizeof(instruction));
    }
    numberOfInstructions = i;

    return instructions;
}

int calculateKey(char* name){
    
    int result = 0;

    int length = strlen(name);

    for(int i = 0; i < length; i++){
        result *= 42;
        result += name[i];
    }
    return result;
}

void changeValue(instruction** instructions, int index){

    if(search(calculateKey(instructions[index]->registerName)) == NULL){
        insert(instructions[index]->mode * instructions[index]->value, calculateKey(instructions[index]->registerName), instructions[index]->registerName);
    }else{
        
        item* item = search(calculateKey(instructions[index]->registerName));

        if(strcmp(item->id, instructions[index]->registerName) == 0){
            item->value += instructions[index]->mode * instructions[index]->value;
        }else{
            insert(instructions[index]->mode * instructions[index]->value, calculateKey(instructions[index]->registerName), instructions[index]->registerName);
        }
    }
}

int partOne(instruction** instructions){

    init();

    for(int i = 0; i < numberOfInstructions; i++){

        int valueInRegister = 0;
        item* item = search(calculateKey(instructions[i]->conditionRegister));
        if(item != NULL){
            valueInRegister = item->value;
        }

        if(strcmp(instructions[i]->condition, ">") == 0){
            
            if(valueInRegister > instructions[i]->conditionValue){
                changeValue(instructions, i);
            }
        }
        else if(strcmp(instructions[i]->condition, "<") == 0){
            if(valueInRegister < instructions[i]->conditionValue){
                changeValue(instructions, i);
            }
        }
        else if(strcmp(instructions[i]->condition, ">=") == 0){

            if(valueInRegister >= instructions[i]->conditionValue){
                changeValue(instructions, i);
            }
        }
        else if(strcmp(instructions[i]->condition, "==") == 0){
            if(valueInRegister == instructions[i]->conditionValue){
                changeValue(instructions, i);
            }
        }else if(strcmp(instructions[i]->condition, "<=") == 0){
            if(valueInRegister <= instructions[i]->conditionValue){
                changeValue(instructions, i);
            }
        }else if(strcmp(instructions[i]->condition, "!=") == 0){
            if(valueInRegister != instructions[i]->conditionValue){
                changeValue(instructions, i);
            }
        }else{
            printf("NAPAKA!\n");
        }

    }

    int max = INT_MIN;

    for(int i = 0; i < size; i++){
        if(hash_table[i] != NULL && hash_table[i]->value > max){
            max = hash_table[i]->value;
        }
    }
    return max;
}

int partTwo(instruction** instructions){

    init();

    int max = INT_MIN;

    for(int i = 0; i < numberOfInstructions; i++){

        int valueInRegister = 0;
        item* item = search(calculateKey(instructions[i]->conditionRegister));
        if(item != NULL){
            valueInRegister = item->value;
        }

        if(strcmp(instructions[i]->condition, ">") == 0){
            
            if(valueInRegister > instructions[i]->conditionValue){
                changeValue(instructions, i);
            }
        }
        else if(strcmp(instructions[i]->condition, "<") == 0){
            if(valueInRegister < instructions[i]->conditionValue){
                changeValue(instructions, i);
            }
        }
        else if(strcmp(instructions[i]->condition, ">=") == 0){

            if(valueInRegister >= instructions[i]->conditionValue){
                changeValue(instructions, i);
            }
        }
        else if(strcmp(instructions[i]->condition, "==") == 0){
            if(valueInRegister == instructions[i]->conditionValue){
                changeValue(instructions, i);
            }
        }else if(strcmp(instructions[i]->condition, "<=") == 0){
            if(valueInRegister <= instructions[i]->conditionValue){
                changeValue(instructions, i);
            }
        }else if(strcmp(instructions[i]->condition, "!=") == 0){
            if(valueInRegister != instructions[i]->conditionValue){
                changeValue(instructions, i);
            }
        }else{
            printf("NAPAKA!\n");
        }

        for(int i = 0; i < size; i++){
            if(hash_table[i] != NULL && hash_table[i]->value > max){
                max = hash_table[i]->value;
            }
        }

    }

    

    
    return max;
}

int main(){

    FILE* file = fopen("input08.txt", "r");

    instruction** instructions = parse(file);
    
    printf("%d\n", partOne(instructions));

    return 0;
}