import fileinput
import math 

count = 50
count_2 = 50
part1 = 0
part2 = 0
for line in fileinput.input():
    turn, amount = line[0], line[1:]
    if turn == "L":
        count -= int(amount)
        for i in range(0, int(amount)):
            count_2 -= 1
            count_2 %= 100
            if count_2 == 0:
                part2 += 1
    elif turn == "R":
        count += int(amount)
        for i in range(0, int(amount)):
            count_2 += 1
            count_2 %= 100
            if count_2 == 0:
                part2 += 1
    else:
        print(f"Unknown {turn} and {amount}")
        
    count %= 100

    if count == 0:
        part1 += 1

print(part1)
print(part2)