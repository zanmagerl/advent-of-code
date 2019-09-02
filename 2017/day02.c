#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <limits.h>

#define MAX_NUMBER_OF_COL 100
#define MAX_NUMBER_OF_ROWS 100

FILE* file;
char* input;

int* numberOfColumns;
int numberOfRows;

int** parser(){

    int** input = malloc(MAX_NUMBER_OF_COL * sizeof(int*));

    numberOfColumns = malloc(MAX_NUMBER_OF_ROWS * sizeof(int));

    file = fopen("input02.txt", "r");
	
    char c = '_';

    int firstPartResult = 0;

    int* row = malloc(MAX_NUMBER_OF_COL * sizeof(int));
    int columnNumber = 0;
    int rowNumber = 0;
    int tempNumber = 0;

    while((c = fgetc(file)) != EOF){
        if(c >= 48 && c < 58){
            tempNumber *= 10;
            tempNumber += c - '0';
        }else if(c == '\n'){
            row[columnNumber] = tempNumber;

            input[rowNumber] = row;
            numberOfColumns[rowNumber] = columnNumber + 1;
            
            columnNumber = 0;
            tempNumber = 0;
            
            row = malloc(MAX_NUMBER_OF_COL * sizeof(int));
            rowNumber++;
        }else{
            row[columnNumber] = tempNumber;
            columnNumber++;
            tempNumber = 0;
        }
    } 

    row[columnNumber] = tempNumber;
    
    input[rowNumber] = row;
    numberOfColumns[rowNumber] = columnNumber + 1;
    numberOfRows = rowNumber + 1;

    fclose(file);

    return input;
}


int getDifference(int* row, int numberOfElements){

    int min = INT_MAX;
    int max = INT_MIN;

    for(int i = 0; i < numberOfElements; i++){
        int value = *(row + i);
        if(value > max){
            max = value; 
        }
        if(value < min){
            min = value;
        }
    }

    return max - min;
}

int getResultOfDivision(int* row, int numberOfElements){

    for(int i = 0; i < numberOfElements - 1; i++){
        
        int value = *(row + i);

        for(int j = i + 1; j < numberOfElements; j++){
            if(value % *(row + j) == 0){
                return value / *(row + j);
            }
            if(*(row + j) % value == 0){
                return *(row + j) / value;
            }
        }
    }
    //According to the instructions, program should never come to this return statement. 
    return 0;
}

int firstPart(int** input){

    int checksum = 0;

    for(int i = 0; i < numberOfRows; i++){
        checksum += getDifference(input[i], numberOfColumns[i]);
    }

	return checksum; 
}

int secondPart(int** input){

    int result = 0;

    for(int i = 0; i < numberOfRows; i++){
        
        result += getResultOfDivision(input[i], numberOfColumns[i]);

    }
    
    return result;
}

int main(){

	int** input = parser();
	
	printf("Solution to the first part: %d\n", firstPart(input));
    
	printf("Solution to the second part: %d\n", secondPart(input));
    
	return 0;
}


