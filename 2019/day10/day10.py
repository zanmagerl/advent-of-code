class Asteroid():

    def __init__(self, y, x):
        self.y = y
        self.x = x

    def intersect(self, asteroid, grid):
        if self.x == asteroid.x and self.y == asteroid.y:
            return True
        difference_x = self.x - asteroid.x
        difference_y = self.y - asteroid.y
        if difference_x != 0 and difference_y != 0:
            k = difference_y/difference_x
            n = self.y - k * self.x
            #print(asteroid.x, asteroid.y, k, n)
            for x in range(0, len(grid[0])):
                y = k*x + n
                if y.is_integer() and y >= 0 and y < len(grid) and grid[int(y)][x] == '#' and (y != self.y or x != self.x) and (y != asteroid.y or x != asteroid.x) and (x in range(min(self.x, asteroid.x), max(self.x, asteroid.x)) and y in range(min(self.y, asteroid.y), max(self.y, asteroid.y))):
                    #print("Koordinate: ", x, y, asteroid.x, asteroid.y)
                    return True
        elif difference_y == 0:
            for x in range(min(self.x, asteroid.x) + 1, max(self.x, asteroid.x)):
                if grid[self.y][x] == '#':
                    return True
            return False
        elif difference_x == 0:
            for y in range(min(self.y, asteroid.y) + 1, max(self.y, asteroid.y)):
                if grid[y][self.x] == '#':
                    return True
            return False
        return False

    def manhattan_distance(self, x, y):
        return abs(self.x - x) + abs(self.y - y) 

def part_one(asteroids, grid):
    max_detexted = 0
    for asteroid in asteroids:
        detected = 0
        for a in asteroids:
            if not asteroid.intersect(a, grid):
                detected += 1
    
        if detected > max_detexted:
            print("Zaznano:" , asteroid.x, asteroid.y, detected)
            max_detexted = detected
    return max_detexted

def part_two():
    pass

lines = list(open("input10.txt").read().split("\n"))
grid = []
asteroids = []
i = 0
while i < len(lines):
    j = 0
    grid_level = []
    while j < len(lines[i]):
        if lines[i][j] == '#':
            asteroids.append(Asteroid(i, j))
        grid_level.append(lines[i][j])
        j += 1
    i += 1
    grid.append(grid_level)

"""
asteroid = asteroids[20]
print(asteroid.x, asteroid.y)
for a in asteroids:
    if not asteroid.intersect(a, grid):
        print(a.y, a.x)
"""
#print(grid)
print("Part one: {}".format(part_one(asteroids, grid)))
#print("Part two: {}".format(part_two(planet_system)))