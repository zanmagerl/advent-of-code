import fileinput


batteries = []
for line in fileinput.input():
    batteries.append(list(line.strip()))

part1 = 0
for battery in batteries:
    max_number = 0
    index_max = battery.index(max(battery[:-1]))
    max_2 = max(battery[index_max+1:])
    part1 += int(battery[index_max]) * 10 + int(max_2)

print(part1)


part2 = 0
for battery in batteries:
    index_max = -1
    s = 0
    for i in range(0, 12):
        if i != 12:
            maks = max(battery[0:-(12-i)])
            index_max = battery.index(max(battery[0:-(12-i)]))
        else:
            maks = max(battery[0:])
            index_max = battery.index(max(battery[0:]))
        battery = battery[index_max+1:]
        s *= 10
        s += int(maks)
    part2 += s

print(part2)