class Planet():

    def __init__ (self, name, parent):
        self.name = name
        self.parent = parent
    
    
    def list_of_parents(self):
        parents = []
        iter = self.parent
        while iter is not None:
            parents.append(iter)
            iter = iter.parent
        return parents

def count_orbits(planet):
    if planet.parent is not None:
        return 1 + count_orbits(planet.parent)
    return 0

def part_one(planet_system):
    
    orbits = 0
    for planet in planet_system:
        orbits += count_orbits(planet_system[planet])    
    
    return orbits

def part_two(planet_system):

    parents_you = planet_system['YOU'].list_of_parents()
    parents_san = planet_system['SAN'].list_of_parents()
    
    i = 0
    while i < len(parents_you):
        j = 0
        while j < len(parents_san):
            if parents_you[i].name == parents_san[j].name:
                return i + j
            j += 1
        i += 1


def create_planet_system(input):
    planet_system = {}
    for line in input:
        planets = line.split(")")
        #if planet that orbits has not been known yet
        if planets[1] not in planet_system:
            #if planet around which planet orbits has not been known yet
            if planets[0] not in planet_system:
                planet_system[planets[0]] = Planet(planets[0], None)
            planet_system[planets[1]] = Planet(planets[1], planet_system[planets[0]])
        
        else:
            #if planet around which planet orbits has not been known yet
            if planets[0] not in planet_system:
                planet_system[planets[0]] = Planet(planets[0], None)
            planet_system[planets[1]].parent = planet_system[planets[0]]
            
    return planet_system

input = open("input06.txt").read().split("\n")
planet_system = create_planet_system(input)

print("Part one: {}".format(part_one(planet_system)))
print("Part two: {}".format(part_two(planet_system)))