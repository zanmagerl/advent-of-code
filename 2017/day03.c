#include <stdio.h>
#include <stdlib.h>

#define SIZE 1000

int grid[SIZE][SIZE];

typedef struct Point{
    int x;
    int y;
}Point;

int partOne(int input){

    int result = 0;

    int previousSquare = 1;
    int i;
    
    for(i = 3; ; i+=2){
        int value = i*i;
        
        if(value >= input){
            break;
        }else{
            previousSquare = value; 
        }
    }

    int numberOfBigSteps = (i-1) / 2;

    int middlePoint = i/2 + 1;

    int numberOfSmallSteps = 0;

    for(int j = i*i; j > previousSquare; j--){

        numberOfSmallSteps++;

        if(j == input){
            break;
        }
        numberOfSmallSteps %= i-1;
    }   
    result = numberOfBigSteps + (middlePoint - numberOfSmallSteps);

    return result;
}

int sumOfNeighbours(int x, int y){

    int a =  
            grid[y-1][x-1] + grid[y-1][x] + grid[y-1][x+1] + 
            grid[y][x-1] + grid[y][x+1] +
            grid[y+1][x-1] + grid[y+1][x] + grid[y+1][x+1];
    
    return a;
}

int fillArray(Point* p, int xDirection, int yDirection, int dolzinaKraka, int input){

    for(int i = 0; i < dolzinaKraka; i++){
       
        int sum = sumOfNeighbours(p->x + xDirection*i, p->y + yDirection*i);
        grid[p->y + yDirection*i][p->x + xDirection*i] = sum;

        if(sum > input){
            p->x += xDirection*i;
            p->y += yDirection*i;
            return 0;
        } 

    }

    
    p->x += xDirection*(dolzinaKraka-1);
    p->y += yDirection*(dolzinaKraka-1);
    
    return 1;
}

int partTwo(int input){

    int middlePoint = SIZE/2;

    grid[middlePoint][middlePoint] = 1;

    int dolzinaKraka = 2;

    int lastValue = 1;
    
    Point* p = malloc(sizeof(Point*));
    
    p->x = SIZE/2 + 1;
    p->y = SIZE/2;
    
    int notOver = 1; 

    while(notOver){

        notOver = fillArray(p, 0, -1, dolzinaKraka, input);
        if(!notOver){
            return grid[p->y][p->x];
        }
        p->x--;

        notOver = fillArray(p, -1, 0, dolzinaKraka, input);
        if(!notOver){
            return grid[p->y][p->x];
        }
        p->y++;

        notOver = fillArray(p, 0, 1, dolzinaKraka, input);
        if(!notOver){
            return grid[p->y][p->x];
        }
        p->x++;

        notOver = fillArray(p, 1, 0, dolzinaKraka, input);
        if(!notOver){
            return grid[p->y][p->x];
        }
        p->x++;

        dolzinaKraka += 2;
    }

}

int main(){

    int input = 347991;

    printf("Solution for the first part: %d\n", partOne(input));
    printf("Solution for the second part: %d\n", partTwo(input));

    return 0;
}