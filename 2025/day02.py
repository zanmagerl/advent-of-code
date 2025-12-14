import fileinput

line = fileinput.input().readline()

ranges = []

for r in line.split(","):
    f, s = r.split("-")
    ranges.append((int(f), int(s)))

# part1
sum_part_1 = 0
invalid_ids = set()
for i in range(1, 1000000):
    for j in range(2, 16):
        duplicate = int(str(i) * j)
        for r in ranges:
            if duplicate >= r[0] and duplicate <= r[1]:
                invalid_ids.add(duplicate)
print(sum_part_1)

sum_part_2 = 0
for i in invalid_ids:
    sum_part_2 += i
print(su)