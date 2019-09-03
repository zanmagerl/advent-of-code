#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_NAME_LENGTH 20
#define MAX_NUMBER_OF_SONS 10

typedef struct node_{
    char* name;
    int weight;
    char** sons;
    struct node_** realSons;
    int numberOfSons;
}node;

node** nodes;
int nodesNumber;

int refineWeight(char* rawWeight){

    int value = 0;

    char* c = rawWeight+1;
    
    while(*c != ')'){
        value *= 10;
        value += *c - '0';
        c++;
    }

    return value;
}

int insertSonsInNode(FILE* file, node* father){
    
    char c = ' ';
    char* name = malloc(MAX_NAME_LENGTH * sizeof(char));

    int letterCount = 0;
    int wordCount = 0;

    while((c = fgetc(file)) != '\n'){

        if(c == ' '){
            continue;
        }
        if(c >= 'a' && c <= 'z'){
            name[letterCount] = c;
            letterCount++;         
        }else if(c == ','){
            
            name[letterCount] = '\0';
            
            //To overwrite whatever was already on father->sons address.
            if(wordCount == 0){
                father->sons = NULL;
            }
            
            father->sons = realloc(father->sons, (wordCount + 1)*sizeof(char*));
            father->sons[wordCount] = malloc(MAX_NAME_LENGTH * sizeof(char));

            strcpy(father->sons[wordCount], name);
            free(name);

            name = malloc(MAX_NAME_LENGTH * sizeof(char));
            
            letterCount = 0;
            wordCount++;
        }else{
            name[letterCount] = '\0';

            father->sons = realloc(father->sons, (wordCount + 1)*sizeof(char*));
            father->sons[wordCount] = malloc(MAX_NAME_LENGTH * sizeof(char));

            strcpy(father->sons[wordCount], name);
            
            free(name);
            
            wordCount++;
            father->numberOfSons = wordCount;
            
            return 1;
        }
    }
    
    name[letterCount] = '\0';

    father->sons = realloc(father->sons, (wordCount + 1)*sizeof(char*));
    father->sons[wordCount] = malloc(MAX_NAME_LENGTH * sizeof(char));

    strcpy(father->sons[wordCount], name);
    free(name);
    wordCount++;
    father->numberOfSons = wordCount;
    
    return 0;
}

node** parse(FILE* file){

    int numberOfNodes = 1;

    nodes = malloc(numberOfNodes * sizeof(node*));

    int isOver = 0;
    int i;

    for(i = 0; ; i++){
        node* newNode = malloc(sizeof(node));
        newNode->name = malloc(MAX_NAME_LENGTH * sizeof(char));
        fscanf(file, "%s", newNode->name);

        char rawWeight[10];
        fscanf(file, "%s", rawWeight);
        newNode->weight = refineWeight(rawWeight);
        char c = fgetc(file);
        
        //Detecting newline
        if(c == ' '){
            char trash[10];
            fscanf(file, "%s", trash); // ->
            fgetc(file);
            isOver = insertSonsInNode(file, newNode);
        }else{
           //node has no sons
            newNode->numberOfSons = 0;
        }

        if(i >= numberOfNodes){
            numberOfNodes *= 2;
            nodes = realloc(nodes, numberOfNodes * sizeof(node*));
        }
       
        nodes[i] = malloc(sizeof(node*));
        nodes[i] = newNode;
        
        if(isOver || c == EOF){
            nodesNumber = i+1;
            return nodes;
        }
    }
} 

int isSon(char* name, char** names, int numberOfNames){
    for(int i = 0; i < numberOfNames; i++){
        if(strcmp(name, names[i]) == 0){
            return 1;
        }
    }
    return 0;
}

char* partOne(node** nodes){
    
    char** names = malloc((nodesNumber-1) * sizeof(char*));
    int counter = 0;

    for(int i = 0; i < nodesNumber; i++){
        if(nodes[i]->numberOfSons != 0){
            for(int j = 0; j < nodes[i]->numberOfSons; j++){
                names[counter] = malloc(MAX_NAME_LENGTH * sizeof(char));                
                strcpy(names[counter], nodes[i]->sons[j]);
                counter++;
            }
        }
    }

    for(int i = 0; i < nodesNumber; i++){
        if(!isSon(nodes[i]->name, names, nodesNumber-1)){
            return nodes[i]->name;
        }
    }
}

void linkThem(node** nodes){
    for(int i = 0; i < nodesNumber; i++){
        for(int j = 0; j < nodes[i]->numberOfSons; j++){
            for(int n = 0; n < nodesNumber; n++){
                if(strcmp(nodes[i]->sons[j], nodes[n]->name) == 0){
                    
                    if(j == 0){
                        nodes[i]->realSons = NULL;
                    }
                    nodes[i]->realSons = realloc(nodes[i]->realSons,(j + 1) * sizeof(node*));
                    nodes[i]->realSons[j] = nodes[n];
                
                }
            }
        }
        
    }
}

int recurse(node* root){

    if(root->numberOfSons == 0){
        return root->weight;
    }

    int* w = malloc(root->numberOfSons * sizeof(int));

    for(int i = 0; i < root->numberOfSons; i++){
        w[i] = recurse(root->realSons[i]);
    }
    
    int sum = w[0];
    int different = 0;

    int numberOfOccurences = 1;

    for(int i = 1; i < root->numberOfSons; i++){
       if(w[i] == w[0]){
           numberOfOccurences++;
       }else{
           different = i;
           numberOfOccurences--;
       }
       sum += w[i];
    }
    if(numberOfOccurences < 0){
        different = 0;
    }
    if(numberOfOccurences != root->numberOfSons){
        printf("%d\n", root->realSons[different]->weight + (w[0] - w[different]));
        exit(0);
    }

    return sum + root->weight;

}

int partTwo(node** nodes, node* root){
    linkThem(nodes);

    return recurse(root);
}

node* findRoot(char* rootName, node** nodes){
    for(int i = 0; i < nodesNumber; i++){
        if(strcmp(nodes[i]->name, rootName) == 0){
            return nodes[i];
        }
    }
}

int main(){

    FILE* file = fopen("input07.txt", "r");

    node** nodes = parse(file);
    char* rootName = partOne(nodes);
    printf("%s\n", rootName);

    partTwo(nodes, findRoot(rootName, nodes));

    return 0;    
}