import fileinput

grid = []

for line in fileinput.input():
    grid.append(list(line.strip()))

num_iterations = len(grid)-2

start_index = grid[0].index('S')

tachyon_beams = set()
tachyon_beams.add((0, start_index))
num_splits = 0
iteration = 0
while iteration < num_iterations:
    new_beams = set()
    for beam in tachyon_beams:
        new_beam = (beam[0]+1, beam[1])
        if grid[new_beam[0]][new_beam[1]] == '^':
            num_splits += 1
            new_beams.add((new_beam[0], new_beam[1]-1))
            new_beams.add((new_beam[0], new_beam[1]+1))
        else:
            new_beams.add(new_beam)
    tachyon_beams = new_beams
    iteration += 1
print(num_splits)

tachyon_beams = []
tachyon_beams.append((0, start_index, 1))
num_splits = 0
iteration = 0
while iteration < num_iterations:
    new_beams = [0]*len(grid[0])
    for beam in tachyon_beams:
        new_beam = (beam[0]+1, beam[1], beam[2])
        if grid[new_beam[0]][new_beam[1]] == '^':
            num_splits += 1
            new_beams[new_beam[1]-1] += new_beam[2]
            new_beams[new_beam[1]+1] += new_beam[2]
        else:
            new_beams[new_beam[1]] += new_beam[2]
    bms = []
    for i in range(0, len(new_beams)):
        if new_beams[i] != 0:
            bms.append((iteration+1, i, new_beams[i]))
     

    tachyon_beams = bms
    iteration += 1


part2 = 0
for beam in tachyon_beams:
    part2 += beam[2]

print(part2)