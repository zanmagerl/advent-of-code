def part_one(modules):
    total_fuel = 0
    for module in modules:
        total_fuel += (module // 3) - 2
    return total_fuel

def part_two(modules):
    total_fuel = 0
    for module in modules:
        fuel = (module // 3) - 2
        total_fuel += fuel
        while fuel > 0:
            fuel = max(0,(fuel // 3) - 2)
            total_fuel += fuel
    return total_fuel    

# input parsing 
input = open("input01.txt", "r")
modules = []
for line in input:
    modules.append(int(line))

print("Part one: {}".format(part_one(modules)))
print("Part two: {}".format(part_two(modules)))
