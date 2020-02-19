#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct move_{
    char type;
    int spin_number; //if move is a spin
    int position_a, position_b; //if move is a exchange
    char a, b; //if move is partner
}move;

typedef struct item_{
    int value;
    struct item_* next;
}item;

item* head = NULL;
int number_of_all_moves = 0;

void createList(int startValue, int endValue){
    
    item* old = NULL;

    for(int i = startValue; i <= endValue; i++){
        item* el = malloc(sizeof(item));
        el->value = i;

        if(i == startValue){
            head = el;
            old = el;
            continue;
        }else{
            old->next = el;
            old = el;
        }

        if(i == endValue){
            el->next = head;
        }
    }
}

int get_number(char c, FILE* file){
    
    int step = 0;
    c = fgetc(file);

    while(c >= '0' && c <= '9'){
        step *= 10;
        step += c - '0';
        c = fgetc(file);   
    }

    return step;
}

move** parse(FILE* file){

    int max_number_of_moves = 1;
    int number_of_moves = 0;

    move** moves = malloc(max_number_of_moves * sizeof(move*));

    move* current_move = malloc(sizeof(move));

    char c = ' ';

    while((c = fgetc(file)) != EOF){
        //end of a move -> save it
        if(c == ','){
            if(number_of_moves >= max_number_of_moves){
                max_number_of_moves *= 2;
                moves = realloc(moves, max_number_of_moves * sizeof(move*));
            }
            moves[number_of_moves] = malloc(sizeof(move));
            moves[number_of_moves] = current_move;

            current_move = malloc(sizeof(move));
            number_of_moves++;
        }
        else{
            //spin
            if(c == 's'){

                current_move->type = 's';
                current_move->spin_number = get_number(c, file);
                ungetc(',', file);
            }

            else if(c == 'x'){

                current_move->type = 'x';
                current_move->position_a = get_number(c, file);
                current_move->position_b = get_number(c, file);
                ungetc(',', file);

            }
            else if(c == 'p'){

                current_move->type = 'p';
                current_move->a = fgetc(file);
                fgetc(file);
                current_move->b = fgetc(file);
            }

            else{
                printf("ERROR! %c\n", c);
                exit(-1);
            }
        }
    }
    if(number_of_moves >= max_number_of_moves){
        max_number_of_moves *= 2;
        moves = realloc(moves, max_number_of_moves * sizeof(move*));
    }

    moves[number_of_moves] = malloc(sizeof(move));
    moves[number_of_moves] = current_move;

    number_of_all_moves = number_of_moves;

    return moves;
}

int list_length(){
    item* iter = head->next;
    int length = 1;
    while(iter != head){
        length++;
        iter = iter->next;
    }
    return length;
}

void execute(move* current_move){
    item* iter_a;
    item* iter_b;
    item* iter;
    int value_a;
    int length = list_length();

    switch (current_move->type){
        case 's':

            for(int i = 0; i < length - current_move->spin_number; i++){
                head = head->next;
            }
            break;

        case 'x':
            iter_a = head;
            for(int i = 0; i < current_move->position_a; i++){
                iter_a = iter_a -> next;
            }
            iter_b = head;
            for(int i = 0; i < current_move->position_b; i++){
                iter_b = iter_b -> next;
            }
            //printf("%d %d %c %c\n", current_move->position_a, current_move->position_b, iter_a->value, iter_b->value);
            value_a = iter_a->value;
            iter_a->value = iter_b->value;
            iter_b->value = value_a;

            break;
        case 'p':

            iter_a = NULL;
            iter_b = NULL;
            
            iter = head;

            while(iter_a == NULL || iter_b == NULL){
                if(current_move->a == iter->value){
                    iter_a = iter;
                }
                if(current_move->b == iter->value){
                    iter_b = iter;
                }
                iter = iter->next;
            }
            value_a = iter_a->value;
            iter_a->value = iter_b->value;
            iter_b->value = value_a;
            break;
    }
}

char* get_string_from_list(){
    char* string = malloc(100* sizeof(char));

    int startValue = head->value;
    item* iter = head->next;
    string[0] = (char)startValue;
    int counter = 1;
    while(iter->value != startValue){
        string[counter] = (char)iter->value;
        iter = iter->next;
        counter++;
    }
    string[counter] = '\0';

    return string;
}

char* partOne(move** moves){

    for(int i = 0; i <= number_of_all_moves; i++){
        execute(moves[i]);
    }

    return get_string_from_list();
}

int check_for_repetition(char** permutations, int end){
    for(int i = 0; i <= end - 1; i++){
        for(int j = i + 1; j <= end; j++){
            if(strcmp(permutations[i], permutations[j]) == 0){
                return 1;
            }
        }
    }
    return 0;
}

char* partTwo(move** moves){

    char** permutations = malloc(100000 * sizeof(char*));

    for(int i = 0; i < 100000; i++){

        permutations[i] = malloc(17 * sizeof(char));
        permutations[i] = partOne(moves);
        
        if(check_for_repetition(permutations, i)){
                        
            int cycle_length = i;
            
            return permutations[1000000000 % cycle_length - 1]; //-1 because I start with 0
        }
    }



    return get_string_from_list();
}

int main(){

    FILE* file = fopen("input16.txt", "r");

    move** moves = parse(file);

    createList('a', 'p');

    //printf("%s\n", partOne(moves));
    printf("%s\n", partTwo(moves));
    return 0;
}