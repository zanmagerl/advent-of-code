#include <stdlib.h>

int size = 10000;

typedef struct item{
    int key;
    int value;
}item;

int hashCode(int key){
    int prime = 37;
    return 37 * key % size;
}

item** hash_table;

void init(){
    hash_table = malloc(size * sizeof(item*));
}

void rehash(){
    size *= 2;

    item* newTable[size];

    for(int i = 0; i < size/2; i++){
        if(hash_table[i] != NULL){
            newTable[hashCode(hash_table[i]->key)] = hash_table[i];
        }
    }

    free(hash_table);
    hash_table = newTable;
    
}

void insert(int value, int key){

    item* newItem = malloc(sizeof(item));
    newItem->key = key;
    newItem->value = value;

    int index = hashCode(key);

    while(hash_table[index] != NULL){
        rehash();
    }

    hash_table[index] = newItem;
}

item* search(int key){
    return hash_table[hashCode(key)];
}

