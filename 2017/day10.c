#include <stdio.h>
#include <stdlib.h>

typedef struct item_{
    int value;
    struct item_* next;
}item;

item* head = NULL;

item* createList(int index, int length){
    
    if(index == length){
        return head;
    }

    item* newItem = malloc(sizeof(item));
    newItem->value = index;

    if(index == 0){
        head = newItem;
    }

    newItem->next = createList(index+1, length);

    return newItem;
}

void print_list(item* el, int length){

    if(length == 0){
        return;
    }

    printf("%d ", el->value);
    print_list(el->next, length-1);
}

item* get_item_of_index(int index, item* el){
    if(index != 0){
        return get_item_of_index(index-1, el->next);
    }
    return el;
}

item* shift_item(item* it, int length){

    for(int i = 0; i < length; i++){
        it = it->next;
    }

    return it;
}

void reverse_list(item* el, int length){
    
    item* iter = el;
    
    while(length > 1){
        
        item* dest = shift_item(iter, length-1);

        int value = iter->value;
        iter->value = dest->value;
        dest->value = value;
        iter = iter->next;

        length -= 2;
    }
}

int partOne(int* lengths, int numberOfLengths){
    
    int skip_size = 0;
    item* position = head;

    for(int i = 0; i < numberOfLengths; i++){
        
        reverse_list(position, lengths[i]);
        
        int shift = lengths[i] + skip_size;
        
        position = shift_item(position, shift);
        
        skip_size++;
    }

    return head->value * head->next->value;
}

char* partTwo(int* lengths, int number_of_lengths){

    int skip_size = 0;
    item* position = head;
    //creating sparse hash
    for(int step = 0; step < 64; step++){

        for(int i = 0; i < number_of_lengths; i++){
            
            reverse_list(position, lengths[i]);
            
            int shift = lengths[i] + skip_size;
            
            position = shift_item(position, shift);
            
            skip_size++;
        }
    }

    //creating dense hash

    int* dense_hash = malloc(16 * sizeof(int));

    for(int i = 0; i < 16; i++){

        int value = (shift_item(head, i*16))->value;

        for(int j = 1; j < 16; j++){
            value ^= (shift_item(head, i*16 + j))->value;
        }
        dense_hash[i] = value;
    }

    //conversion to hash
    char* knot_hash = malloc(33 * sizeof(char));
    knot_hash[32] = '\0';

    for(int i = 0; i < 16; i++){

        int value = dense_hash[i];

        int first = value / 16;
        int second = value % 16;

        if(first < 10){
            knot_hash[i*2] = first + '0';
        }else{
            knot_hash[i*2] = first - 10 + 'a';
        }

        if(second < 10){
            knot_hash[i*2 + 1] = second + '0';
        }else{
            knot_hash[i*2 + 1] = second - 10 + 'a';
        }
    }

    return knot_hash;
}

int main(){
    
    int number_of_lengths = 16;
    int* lengths = malloc(number_of_lengths * sizeof(int));
    
    FILE* file = fopen("input10.txt", "r");

    for(int i = 0; i < number_of_lengths; i++){
        fscanf(file, "%d", &lengths[i]);
        fgetc(file);
    }
    fclose(file);

    createList(0, 256);
    
    printf("%d\n", partOne(lengths, number_of_lengths));
    free(lengths);

    file = fopen("input10.txt", "r");

    lengths = malloc(100 * sizeof(int));

    char c = ' ';
    int i;
    for(i = 0; (c = fgetc(file)) != EOF; i++){
        lengths[i] = c;
    }
    lengths[i] = 17;
    lengths[i+1] = 31;
    lengths[i+2] = 73;
    lengths[i+3] = 47;
    lengths[i+4] = 23;

    number_of_lengths = i + 5;

    createList(0, 256);

    printf("%s\n", partTwo(lengths, number_of_lengths));

    return 0;
}