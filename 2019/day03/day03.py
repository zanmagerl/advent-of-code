class Section:

    def __init__(self, x1, y1, x2, y2, direction):
        self.x1 = x1
        self.y1 = y1
        self.x2 = x2
        self.y2 = y2
        self.direction = direction


    def intersection(self, line):
        if self.direction == '|' and line.direction == '-':
            if self.x1 >= min(line.x1, line.x2) and self.x1 <= max(line.x1, line.x2) and line.y1 >= min(self.y1, self.y2) and line.y2 <= max(self.y1, self.y2):
                return (self.x1, line.y1) 
        elif line.direction == '|' and self.direction == '-':
            if line.x1 >= min(self.x1, self.x2) and line.x1 <= max(self.x1, self.x2) and self.y1 >= min(line.y1, line.y2) and self.y2 <= max(line.y1, line.y2):
                return (line.x1, self.y1)  
        return None

    def point_on_line(self, x, y):
        if x <= max(self.x1, self.x2) and x >= min(self.x1, self.x2) and y <= max(self.y1, self.y2) and y >= min(self.y1, self.y2):
            return True
        return False 

    def distance_line_intersection(self, x, y):
        return max(abs(self.x2 - x), abs(self.y2 - y))

    def line_length(self):
        return abs(self.x2-self.x1) + abs(self.y2-self.y1)

def part_one(sections):
    
    intersections = []
    
    for section_one in sections[0]:
        for section_two in sections[1]:
            intersection = section_one.intersection(section_two)
            if intersection is not None and intersection != (0,0):
                intersections.append(intersection)

    min_distance = 100000000
    for intersection in intersections:
        distance = abs(intersection[0]) + abs(intersection[1])
        if distance < min_distance:
            min_distance = distance

    return (min_distance,intersections)

def part_two(sections):
    
    intersections = part_one(sections)[1]
    distances = []
    
    for intersection in intersections:
      
        distance = 0

        for section in sections[0]:
            distance += section.line_length()
            if section.point_on_line(intersection[0], intersection[1]):
                distance -= section.distance_line_intersection(intersection[0], intersection[1])
                break
          
        for section in sections[1]:
            distance += section.line_length()
            if section.point_on_line(intersection[0], intersection[1]):
                distance -= section.distance_line_intersection(intersection[0], intersection[1])
                break
        
        distances.append(distance)

    return min(distances)

def parse(paths):
    sections_wires = []
    for wire in paths:
        sections = []
        location = [0, 0]
        for section in wire:
            new_location = ()
            direction = ''
            if section[0] == 'R':
                new_location = (location[0] + int(section[1:]), location[1])
                direction = '-'
            elif section[0] == 'L':
                new_location = (location[0] - int(section[1:]), location[1])
                direction = '-'
            elif section[0] == 'U':
                new_location = (location[0], location[1] + int(section[1:]))
                direction = '|'
            else:
                new_location = (location[0], location[1] - int(section[1:]))
                direction = '|'
            sections.append(Section(location[0], location[1], new_location[0], new_location[1], direction))
            location[0] = new_location[0]
            location[1] = new_location[1]

        sections_wires.append(sections)

    return sections_wires

# input parsing 
wires = open("input03.txt", "r").read().split("\n")
paths = []
for wire in wires:
    paths.append(wire.split(","))

sections = parse(paths)

print("Part one: {}".format(part_one(sections)[0]))
print("Part two: {}".format(part_two(sections)))
