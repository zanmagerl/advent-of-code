import math 

class Asteroid():

    def __init__(self, y, x):
        self.y = y
        self.x = x
        self.angle = 0

    def __str__(self):
        return "x: {} y: {} angle: {}".format(self.x, self.y, self.angle)

    def intersect(self, asteroid, asteroids):
        
        if self == asteroid:
            return True

        delta_x = asteroid.x - self.x
        delta_y = asteroid.y - self.y

        rho = math.atan2(delta_y, delta_x)

        for a in asteroids:

            if a == asteroid or a == self:
                continue
            
            dx = a.x - self.x
            dy = a.y - self.y
            i_rho = math.atan2(dy, dx)

            if rho == i_rho and a.x >= min(asteroid.x, self.x) and a.x <= max(asteroid.x, self.x) and a.y >= min(asteroid.y, self.y) and a.y <= max(asteroid.y, self.y):
                return True 

        return False

    def calucalate_angle(self, asteroid):
        dx = asteroid.x - self.x
        dy = asteroid.y - self.y
        rho = math.atan2(dy, dx)

        self.angle = rho

    def manhattan_distance(self, x, y):
        return abs(self.x - x) + abs(self.y - y) 

def part_one(asteroids, grid):
    
    max_detexted = 0
    best_asteroid = None

    for asteroid in asteroids:
        detected = 0
        for a in asteroids:
            if not asteroid.intersect(a, asteroids):
                detected += 1
        if detected > max_detexted:
            max_detexted = detected
            best_asteroid = asteroid
    return (max_detexted, best_asteroid)

def part_two(laser_position, asteroids):

    asteroids.remove(laser_position)
    
    for asteroid in asteroids:
        asteroid.calucalate_angle(laser_position)

    asteroids = sorted(asteroids, key = lambda asteroid: asteroid.angle)

    iterator = 0
    while iterator < len(asteroids):
        min_iterator = iterator
        while iterator < len(asteroids) and asteroids[min_iterator].angle == asteroids[iterator].angle:
            iterator += 1
        asteroids[min_iterator:iterator] = sorted(asteroids[min_iterator:iterator] , key = lambda asteroid: asteroid.manhattan_distance(laser_position.x, laser_position.y))
    
    counter = 0
    shoot_index = 0

    while asteroids[shoot_index].angle < math.pi/2:
        shoot_index += 1

    while len(asteroids) > 0:
        
        shooted_asteroid = asteroids[shoot_index]
        asteroids.remove(asteroids[shoot_index])

        if shoot_index == len(asteroids):
            shoot_index = 0
        
        while asteroids[shoot_index].angle == shooted_asteroid.angle:
            shoot_index += 1
            if shoot_index == len(asteroids):
                shoot_index = 0

        counter += 1
        if counter == 200:
            return shooted_asteroid.x * 100 + shooted_asteroid.y
    
    for asteroid in asteroids:
        print(asteroid)

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

result_1 = part_one(asteroids, grid)
print("Part one: {}".format(result_1[0]))
print("Part two: {}".format(part_two(result_1[1], asteroids)))