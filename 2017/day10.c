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

int partOne(){

}

item* get_item_of_index(int index, item* el){
    if(index != 0){
        return get_item_of_index(index-1, el->next);
    }
    return el;
}

item* reverse_list(int index, item* el){
    
}

int main(){
    /*
    int* lengths = malloc(4 * sizeof(int));
    lengths[0] = 3;
    lengths[1] = 4;
    lengths[2] = 1;
    lengths[3] = 5;*/

    createList(0, 5);

    print_list(head, 5);

    //printf("%d\n", partOne());

    return 0;
}