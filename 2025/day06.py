import fileinput

grid = []
for line in fileinput.input():
    grid.append(line.strip().split())

part1 = 0
for column in range(len(grid[0])):
    problem = 0
    operation = grid[-1][column]
    if operation == '*':
        problem = 1
    for row in range(len(grid)-1):
        if operation == '*':
            problem *= int(grid[row][column])
        else:
            problem += int(grid[row][column])
    part1 += problem
    print(problem)

print(part1)

grid = []
for line in fileinput.input():
    grid.append(list(line)[:-1])

separator_indexes = []
for c in range(len(grid[-1])):
    if grid[-1][c] == "*" or grid[-1][c] == "+":
        separator_indexes.append(c)
print(separator_indexes)

part2 = 0

operator_index = -1

problem = 0
operator = grid[-1][separator_indexes[operator_index]]
if operator == '*':
    problem = 1

column = len(grid[0])-1

while column >= 0:
    number = ""
    for row in range(0, len(grid)-1):
        number += grid[row][column]
    print(number)
    if operator == '*':
        problem *= int(number)
    else:
        problem += int(number)
    
    if column == separator_indexes[operator_index]:
        column -= 1
        print(f"Problem {problem}")
        part2 += problem
        problem = 0
        operator_index -= 1
        print(part2)
        operator = grid[-1][separator_indexes[operator_index]]
        if operator == '*':
            problem = 1

    column -= 1
print(part2)