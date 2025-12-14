import fileinput

should_read_ranges = True
valid_ranges = []
ingredients = []
for line in fileinput.input():
    if len(line) < 3 and should_read_ranges:
        should_read_ranges = False
        continue
    
    if should_read_ranges:
        f, s = line.split("-")
        valid_ranges.append((int(f), int(s)))
    else:
        ingredients.append(int(line))

fresh_ingredients = set()
for ingredient in ingredients:
    for valid_range in valid_ranges:
        if ingredient >= valid_range[0] and ingredient <= valid_range[1]:
            fresh_ingredients.add(ingredient)

print(len(fresh_ingredients))

#
#  xxxxx
#    yyyyyy
#  xxyyyyyy
#

num_changes = 1
while num_changes > 0:
    num_changes = 0
    new_ranges = []
    for original_range in valid_ranges:
        did_change = False
        for valid_range in valid_ranges:
            if valid_range == original_range:
                continue
            if original_range[0] >= valid_range[0] and original_range[0] <= valid_range[1]:
                new_ranges.append((min(original_range[0], valid_range[0]), max(original_range[1], valid_range[1])))
                did_change = True
                num_changes+=1
        if not did_change:
            new_ranges.append(original_range)
    valid_ranges = set(new_ranges)
    print(valid_ranges)

part2 = 0
for r in set(valid_ranges):
    part2 += (r[1]-r[0]+1)

print(part2)