#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_NUMBER_OF_PASSPHRASES 1000
#define MAX_NUMBER_OF_WORDS 100
#define MAX_WORD_LENGTH 100

FILE* file;

typedef struct input{
    char*** data;
    int numberOfPass;
    int* numberOfWords;
}input;


input* parse(){

    input* myInput = (input*)malloc(sizeof(input*));
    myInput->data = (char***)malloc(MAX_NUMBER_OF_PASSPHRASES * sizeof(char**));
    myInput->numberOfWords = (int*)malloc(MAX_NUMBER_OF_PASSPHRASES * sizeof(int));

    char c = '#';

    for(int i = 0; i < MAX_NUMBER_OF_PASSPHRASES; i++){

        myInput->data[i] = (char**)malloc(MAX_NUMBER_OF_WORDS * sizeof(char*));

        char** row = (char**)malloc(MAX_NUMBER_OF_WORDS * sizeof(char*));
        int wordCount = 0;
        int letterCount = 0;

        char* word = (char*)malloc(MAX_WORD_LENGTH * sizeof(char));

        while((c = fgetc(file)) != '\n'){
            if(c >= 'a' && c <= 'z'){
                word[letterCount] = c;
                letterCount++;
            }else if(c == ' '){
                row[wordCount] = (char*)malloc(MAX_WORD_LENGTH * sizeof(char));
                word[letterCount] = '\0';
                strcpy(row[wordCount], word);
                
                letterCount = 0;
                wordCount++;
                
                word = (char*)malloc(MAX_WORD_LENGTH * sizeof(char));
            }else{
                row[wordCount] = (char*)malloc(MAX_WORD_LENGTH * sizeof(char));
                word[letterCount] = '\0';
                strcpy(row[wordCount], word);
                
                wordCount++;

                myInput->numberOfWords[i] = wordCount;
                myInput->numberOfPass = i+1;
                myInput->data[i] = row;
                
                return myInput;
            }
        }
        row[wordCount] = (char*)malloc(MAX_WORD_LENGTH * sizeof(char));
        word[letterCount] = '\0';
        strcpy(row[wordCount], word);

        wordCount++;
        
        myInput->numberOfWords[i] = wordCount;
        myInput->data[i] = row;
    }
}

int cmpfunc (const void * a, const void * b) {
   return ( *(char*)a - *(char*)b );
}

int isValid(char** row, int numberOfWords){
    for(int j = 0; j < numberOfWords - 1; j++){
        for(int n = j+1; n < numberOfWords; n++){
            if(strcmp(row[j], row[n]) == 0){
                return 0;
            }
        }
    }
    return 1;
}

int isValidTwo(char** row, int numberOfWords){
    for(int j = 0; j < numberOfWords - 1; j++){
        for(int n = j+1; n < numberOfWords; n++){
            qsort(row[j], strlen(row[j]), sizeof(char), cmpfunc);
            qsort(row[n], strlen(row[n]), sizeof(char), cmpfunc);
            if(strcmp(row[j], row[n]) == 0){
                return 0;
            }
        }
    }
    return 1;
}



int partOne(input* myInput){
    
    int result = 0;

    for(int i = 0; i < myInput->numberOfPass; i++){
        result += isValid(myInput->data[i], myInput->numberOfWords[i]);
    }

    return result;    
}

int partTwo(input* myInput){
    
    int result = 0;

    for(int i = 0; i < myInput->numberOfPass; i++){
        result += isValidTwo(myInput->data[i], myInput->numberOfWords[i]);
    }

    return result;
}

int main(){

    file = fopen("input04.txt", "r");

    input* myInput = parse();
    
    printf("%d\n", partOne(myInput));
    printf("%d\n", partTwo(myInput));
    
    fclose(file);

    return 0;
}