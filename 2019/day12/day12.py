import re

class Moon():

    def __init__(self, x, y, z):
        self.x = x
        self.y = y
        self.z = z

        self.dx = 0
        self.dy = 0
        self.dz = 0

    def applyGravity(self, moons):
        for moon in moons:
            if moon == self:
                continue

            if self.x > moon.x:
                self.dx -= 1
            elif self.x < moon.x:
                self.dx += 1

            if self.y > moon.y:
                self.dy -= 1
            elif self.y < moon.y:
                self.dy += 1

            if self.z > moon.z:
                self.dz -= 1
            elif self.z < moon.z:
                self.dz += 1

    def move(self):
        self.x += self.dx
        self.y += self.dy
        self.z += self.dz

    def __str__(self):
        return "x: {} y: {} z: {} dx: {} dy: {} dz: {}".format(self.x, self.y, self.z, self.dx, self.dy, self.dz)

    def energy(self):
        potential_energy = abs(self.x) + abs(self.y) + abs(self.z)
        kinetic_energy = abs(self.dx) + abs(self.dy) + abs(self.dz)

        return potential_energy * kinetic_energy

def checkMoons(old_moons, current_moons, dimension):
    for i in range(0, len(old_moons)):
        if dimension == "x":
            if old_moons[i].x != current_moons[i].x or current_moons[i].dx != 0:
                return False
        elif dimension == "y":
            if old_moons[i].y != current_moons[i].y or current_moons[i].dy != 0:
                return False
        elif dimension == "z":
            if old_moons[i].z != current_moons[i].z or current_moons[i].dz != 0:
                return False
        else:
            return False
    return True

def gcd(a, b):
    if b == 0:
        return a
    return gcd(b, a % b)

def lcm2(a, b):
    return a * b // gcd(a, b)

def lcm3(a, b, c):
    return lcm2(a, lcm2(b, c))


def part_one(moons):
    
    number_of_steps = 1000

    for i in range(0, number_of_steps):
        for moon in moons:
            moon.applyGravity(moons)
        for moon in moons:
            moon.move()
    
    total_energy = 0
    for moon in moons:
        total_energy += moon.energy()
        
    return total_energy

def part_two(moons):

    number_of_steps = 1000

    periods = [-1, -1, -1]

    old_moons = []
    for moon in moons:
        old_moons.append(Moon(moon.x, moon.y, moon.z))

    step = 1

    while True:
        
        for moon in moons:
            moon.applyGravity(moons)
        for moon in moons:
            moon.move()
        
        if checkMoons(old_moons, moons, "x") and periods[0] == -1:
            periods[0] = step
        if checkMoons(old_moons, moons, "y") and periods[1] == -1:
            periods[1] = step
        if checkMoons(old_moons, moons, "z") and periods[2] == -1:
            periods[2] = step

        if periods[0] != -1 and periods[1] != -1 and periods[2] != -1:
            break

        step += 1

    return lcm3(periods[0], periods[1], periods[2])

lines = list(open("input12.txt").read().split("\n"))

i = 0
moons = []
while i < len(lines):
    position = re.findall("-?[0-9]+", lines[i])
    moons.append(Moon(int(position[0]), int(position[1]), int(position[2])))
    i += 1

old_moons = []
for moon in moons:
    old_moons.append(Moon(moon.x, moon.y, moon.z))

print("Part one: {}".format(part_one(moons)))
print("Part two: {}".format(part_two(old_moons)))