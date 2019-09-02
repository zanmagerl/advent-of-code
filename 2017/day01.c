#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define NUMBER_OF_CHAR 10000

FILE* file;
char* input;

int firstPart(){

	int result = 0;

	for(int i = 0; i < strlen(input) - 1; i++){
		if(input[i] == input[i+1]){
			result += input[i] - '0';
		}
	}

	if(input[0] == input[strlen(input)-1]){
		result += input[0] - '0';
	}

	return result;
}

int secondPart(){

	int result = 0;

	int lengthOfList = strlen(input);

	for(int i = 0; i < lengthOfList; i++){
		if(input[i] == input[(i + lengthOfList/2) % lengthOfList]){
			result += input[i] - '0';
		}
	}

	return result;
}

int main(){

	file = fopen("input01.txt", "r");
	
	input = malloc(NUMBER_OF_CHAR);
	
	fread(input, 1, NUMBER_OF_CHAR, file);
	
	printf("Solution to the first part: %d\n", firstPart());

	printf("Solution to the second part: %d\n", secondPart());

	return 0;
}


