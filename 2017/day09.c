#include <stdio.h>
#include <stdlib.h>

int solutionForPartTwo = 0;

int partOne(FILE* file, int depth, int trash, int sum){

    char c = ' ';
    while((c = fgetc(file)) != EOF){
        if(trash == 1){
            if(c == '!'){
                fgetc(file);
                continue;
            }else if(c == '>'){
                trash = 0;
                return 0;
            }else{
                solutionForPartTwo++;
                continue;
            }
        }else{
            if(c == '{'){
                sum = partOne(file, depth+1, trash, sum);
            }else if(c == '}'){
                return depth + sum;
            }else if(c == '<'){
                partOne(file, depth, 1, sum);
            }
            else{
                continue;
            }
        }
    }
    return sum;
}

int main(){

    FILE* file = fopen("input09.txt", "r");

    printf("%d\n", partOne(file, 0, 0, 0));
    printf("%d\n", solutionForPartTwo);
    return 0;
}