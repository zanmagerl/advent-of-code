import fileinput

grid = []
for line in fileinput.input():
    grid.append(list(line.strip()))

def is_neighbour(row: int, column: int, grid) -> int:
    if row >= 0 and column >= 0 and row < len(grid) and column < len(grid[row]):
        if grid[row][column] == "@":
            return 1
    return 0

part1 = 0
for row in range(0, len(grid)):
    for column in range(0, len(grid[row])):
        num_neighbours = 0
        if grid[row][column] == "@":
            num_neighbours += is_neighbour(row-1, column-1, grid)
            num_neighbours += is_neighbour(row-1, column, grid)
            num_neighbours += is_neighbour(row-1, column+1, grid)
            num_neighbours += is_neighbour(row, column-1, grid)
            num_neighbours += is_neighbour(row, column+1, grid)
            num_neighbours += is_neighbour(row+1, column-1, grid)
            num_neighbours += is_neighbour(row+1, column, grid)
            num_neighbours += is_neighbour(row+1, column+1, grid)
            if num_neighbours < 4:
                part1 += 1

print(part1)



def remove_rolls(grid):
    locations = []
    for row in range(0, len(grid)):
        for column in range(0, len(grid[row])):
            num_neighbours = 0
            if grid[row][column] == "@":
                num_neighbours += is_neighbour(row-1, column-1, grid)
                num_neighbours += is_neighbour(row-1, column, grid)
                num_neighbours += is_neighbour(row-1, column+1, grid)
                num_neighbours += is_neighbour(row, column-1, grid)
                num_neighbours += is_neighbour(row, column+1, grid)
                num_neighbours += is_neighbour(row+1, column-1, grid)
                num_neighbours += is_neighbour(row+1, column, grid)
                num_neighbours += is_neighbour(row+1, column+1, grid)
                if num_neighbours < 4:
                    locations.append((row, column))
    return locations

part2 = 0
locations = remove_rolls(grid)
while len(locations) > 0:
    for location in locations:
        grid[location[0]][location[1]] = "."
        part2 += 1
    locations = remove_rolls(grid)
print(part2)