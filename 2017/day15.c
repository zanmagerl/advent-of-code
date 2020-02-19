#include <stdio.h>
#include <stdlib.h>
#include <limits.h>

#define FACTOR_A 16807
#define FACTOR_B 48271
#define MODULO INT_MAX

int last_16_bits(long a, long b){

    for(int i = 0; i < 16; i++){
        int reminder_a = a % 2;
        int reminder_b = b % 2;

        if(reminder_a != reminder_b){
            return 0;
        }

        a /= 2;
        b /= 2;
    }

    return 1;
}

int partOne(long a, long b){

    int number_of_matches = 0;

    for(int i = 0; i < 40000000; i++){
        a = (a * FACTOR_A) % MODULO;
        b = (b * FACTOR_B) % MODULO;

        number_of_matches += last_16_bits(a, b);
    }

    return number_of_matches;
}

int partTwo(long a, long b){
    
    int number_of_matches = 0;

    for(int i = 0; i < 5000000; i++){
        do{
            a = (a * FACTOR_A) % MODULO;
        }while(a % 4 != 0);

        do{
            b = (b * FACTOR_B) % MODULO;
        }while(b % 8 != 0);

        number_of_matches += last_16_bits(a, b);
    }

    return number_of_matches;
}

int main(){

    int startA = 0;
    int startB = 0;

    FILE* file = fopen("input15.txt", "r");

    fscanf(file, "%*s %*s %*s %*s %d", &startA);
    fscanf(file, "%*s %*s %*s %*s %d", &startB);

    printf("%d\n", partOne((long)startA, (long)startB));
    printf("%d\n", partTwo((long)startA, (long)startB));

}