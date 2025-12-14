import fileinput
from typing import Tuple

coordinates = []
for line in fileinput.input():
    split = line.strip().split(",")
    coordinates.append((int(split[0]), int(split[1])))

def calculate_area(cornerA: Tuple[int, int], cornerB: Tuple[int, int]) -> int:
    return (abs(cornerA[0]-cornerB[0])+1) * (abs(cornerA[1]-cornerB[1])+1)

def calculate_distance(verticeA: Tuple[int, int], verticeB: Tuple[int, int]) -> int:
    return abs(verticeA[0]-verticeB[0]) + abs(verticeA[1]-verticeB[1])

max_area = -1
for i in range(0, len(coordinates)):
    for j in range(i+1, len(coordinates)):
        area = calculate_area(coordinates[i], coordinates[j])
        if area > max_area:
            max_area = area

def sort_edge(edge: Tuple[Tuple[int, int], Tuple[int, int]]) -> Tuple[Tuple[int, int], Tuple[int, int]]:
    score_f = edge[0][0]*edge[0][1]
    score_s = edge[1][0]*edge[1][1]

    if score_s < score_f:
        return (edge[1], edge[0])
    return edge

print(max_area)

edges = []
distance_matrix = []
for i in range(len(coordinates)):
    distance_matrix.append([0]*len(coordinates))

for i in range(len(coordinates)):
    for j in range(len(coordinates)):
        if i == j or (coordinates[i][0] != coordinates[j][0] and coordinates[i][1] != coordinates[j][1]):
            distance_matrix[i][j] = 1000000000000000
        else:    
            distance_matrix[i][j] = calculate_distance(coordinates[i], coordinates[j])

for i in range(0, len(coordinates)):
    min_1 = 0
    for j in range(len(distance_matrix[i])):
        if distance_matrix[i][j] < distance_matrix[i][min_1]:
            min_1 = j
    edges.append(sort_edge(((coordinates[i][0], coordinates[i][1]), (coordinates[min_1][0], coordinates[min_1][1]))))
    min_2 = 0
    for j in range(len(distance_matrix[i])):
        if min_1 == min_2:
            min_2 = j
        if distance_matrix[i][j] < distance_matrix[i][min_2] and j != min_1:
            min_2 = j
    edges.append(sort_edge(((coordinates[i][0], coordinates[i][1]), (coordinates[min_2][0], coordinates[min_2][1]))))

edges = set(edges)

def is_inside_edges(edges, point) -> bool:
    is_inside = True
    # Check north
    line = ((point[0], point[1]-1000000000), (point[0], point[1]))
    #print(line)
    num_intersect = 0
    hit_edge = []
    for edge in edges:
        if edge[0][0] == edge[1][0]:
            continue
        if edge[0][0] < line[0][0] and edge[1][0] >= line[0][0] and edge[0][1] >= line[0][1] and edge[1][1] <= line[1][1]:
            #print(edge)
            num_intersect += 1
            #hit_edge.append(edge)
    
        if point[0] >= edge[0][0] and point[0] <= edge[1][0] and point[1] == edge[0][1]:
            return True

    #print(is_inside)
    #print()
    num_same_point = 0
    #print(hit_edge)
 
    return num_intersect % 2 == 1

max_area = -1
for i in range(0, len(coordinates)):
    for j in range(i+1, len(coordinates)):
        #print(coordinates[i], coordinates[j])
        point_A = (coordinates[i][0], coordinates[j][1])
        point_B = (coordinates[j][0], coordinates[i][1])
        
        if is_inside_edges(edges, point_A) and is_inside_edges(edges, point_B):
            area = calculate_area(coordinates[i], coordinates[j])
            
            if area > max_area:
                print(coordinates[i], coordinates[j], area)
                max_area = area
        #print()
print(max_area)
