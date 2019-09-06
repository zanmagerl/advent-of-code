#include <stdlib.h>
#include <string.h>

int size = 10000;

typedef struct item{
    int key;
    int value;
    char* id;
}item;

int hashCode(int key){
    int prime = 37;
    return (prime * key) % size;
}

item** hash_table;

void init(){
    hash_table = malloc(size * sizeof(item*));
}

void rehash(){

    size *= 2;

    item** newTable = malloc(size * sizeof(item*));

    for(int i = 0; i < size/2; i++){
        if(hash_table[i] != NULL){
            int newKey = hashCode(hash_table[i]->key);
            newTable[newKey] = malloc(sizeof(item)); 
            newTable[newKey] = hash_table[i];
            free(hash_table[i]);
        }
    }

    free(hash_table);
    hash_table = newTable;
}

void insert(int value, int key, char* id){

    item* newItem = malloc(sizeof(item));
    newItem->key = key;
    newItem->value = value;
    newItem->id = malloc(strlen(id) * sizeof(char));
    strcpy(newItem->id, id);

    int index = hashCode(key);

    while(hash_table[index] != NULL){
        rehash();
    }

    hash_table[index] = malloc(sizeof(item));
    hash_table[index] = newItem;
}

item* search(int key){
    return hash_table[hashCode(key)];
}

