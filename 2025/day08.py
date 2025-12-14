import fileinput
from typing import Tuple

junction_boxes = []

for line in fileinput.input():
    box = line.strip().split(",")
    junction_boxes.append((int(box[0]), int(box[1]), int(box[2])))

circuits = []
for box in junction_boxes:
    circuits.append([box])

def calc_distance(box1: Tuple[int, int, int], box2: Tuple[int, int, int]) -> int:
    return abs(box1[0]-box2[0])**2 + abs(box1[1]-box2[1])**2 + abs(box1[2]-box2[2])**2

def not_in_circuit(junction_boxes, i, j, circuits) -> bool:
    box_i = junction_boxes[i]
    box_j = junction_boxes[j]

    circuit_i, circuit_j = -1, -1
    for c in range(len(circuits)):
        for box in circuits[c]:
            if box == box_i:
                circuit_i = c
            if box == box_j:
                circuit_j = c
    return circuit_i != circuit_j

visited_paths = set()

precomputed = []
for i in range(0, len(junction_boxes)-1):
    for j in range(i+1, len(junction_boxes)):
        precomputed.append((i, j, calc_distance(junction_boxes[i], junction_boxes[j])))

precomputed.sort(key=lambda x: x[2])

for iter in range(1000):
    smallest_distance = 10000000000
    i_box1, i_box2 = -1,-1
    i_box1, i_box2 = precomputed[iter][0], precomputed[iter][1]


    circuit_1, circuit_2 = -1, -1
    for i in range(len(circuits)):
        for box in circuits[i]:
            if box == junction_boxes[i_box1]:
                circuit_1 = i
            if box == junction_boxes[i_box2]:
                circuit_2 = i

    if circuit_1 != circuit_2:
        circuits[circuit_1] = circuits[circuit_1] + circuits[circuit_2]
        circuits.remove(circuits[circuit_2])
    
    
circuits.sort(key=lambda x: -len(x))
part1 = 1
for c in circuits[0:3]:
    part1 *= len(c)

print(part1)
iter = 0
while len(circuits) > 1:
    smallest_distance = 10000000000
    i_box1, i_box2 = -1,-1

    i_box1, i_box2 = precomputed[iter][0], precomputed[iter][1]


    circuit_1, circuit_2 = -1, -1
    for i in range(len(circuits)):
        for box in circuits[i]:
            if box == junction_boxes[i_box1]:
                circuit_1 = i
            if box == junction_boxes[i_box2]:
                circuit_2 = i

    if circuit_1 != circuit_2:
        circuits[circuit_1] = circuits[circuit_1] + circuits[circuit_2]
        circuits.remove(circuits[circuit_2])
    
    iter += 1
    
print(junction_boxes[i_box1][0] * junction_boxes[i_box2][0])