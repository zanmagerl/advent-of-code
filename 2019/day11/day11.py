class Panel:

    def __init__(self, x, y, color):
        self.x = x
        self.y = y
        self.color = color


def get_mode(opcode):
    a = 0
    b = 0
    c = 0
    if len(str(opcode)) == 5:
        a = int(str(opcode)[-5])
    if len(str(opcode)) >= 4:
        b = int(str(opcode)[-4])
    if len(str(opcode)) >= 3:
        c = int(str(opcode)[-3])

    return (c, b, a)

def extract_parameter(codes, i, modes, parameter, relative_base):
    if modes[parameter-1] == 0:
        return codes[codes[i+parameter]]
    elif modes[parameter-1] == 1:
        return codes[i+parameter]
    else:
        return codes[codes[i+parameter]+relative_base]

panels = {}

def computer(codes, input_parameter):
    # 1 is up, 2 is <-, 3 is down, 4 is ->
    direction = 1
    positon_x = 0
    positon_y =0

    output_color = 0
    output_turn = 0
    output_index = 0

    number_of_new_panels_colored = 0

    relative_base = 0

    i = 0
    while i < len(codes):

        opcode = int(str(codes[i])[-2:])
        modes = get_mode(codes[i])

        if opcode == 99:
            return number_of_new_panels_colored
        elif opcode == 1:
            if modes[2] == 2:
                codes[codes[i+3]+relative_base] = extract_parameter(codes, i, modes, 1, relative_base) + extract_parameter(codes, i, modes, 2, relative_base)
            else: 
                codes[codes[i+3]] = extract_parameter(codes, i, modes, 1, relative_base) + extract_parameter(codes, i, modes, 2, relative_base)
        elif opcode == 2:
            if modes[2] == 2:
                codes[codes[i+3]+relative_base] = extract_parameter(codes, i, modes, 1, relative_base) * extract_parameter(codes, i, modes, 2, relative_base)
            else:
                codes[codes[i+3]] = extract_parameter(codes, i, modes, 1, relative_base) * extract_parameter(codes, i, modes, 2, relative_base)
        elif opcode == 3:
            if modes[0] == 2:
                codes[codes[i+1]+relative_base] = input_parameter
            else:
                codes[codes[i+1]] = input_parameter
        elif opcode == 4:
            ##print(extract_parameter(codes, i, modes, 1, relative_base))
            if output_index % 2 == 0:
                output_color = extract_parameter(codes, i, modes, 1, relative_base)
                output_index += 1
            else:
                # 0 left, 1 right
                output_turn = extract_parameter(codes, i, modes, 1, relative_base)
                if (100000*positon_x + positon_y) not in panels:
                    panels[100000*positon_x + positon_y] = Panel(positon_x, positon_y, output_color)
                    number_of_new_panels_colored += 1
                else:
                    panels[100000*positon_x + positon_y].color = output_color
                # 1 is up, 2 is <-, 3 is down, 4 is ->
                if direction == 1:
                    if output_turn == 0:
                        direction = 2
                        positon_x -= 1
                    else:
                        direction = 4
                        positon_x += 1
                elif direction == 2:
                    if output_turn == 0:
                        direction = 3
                        positon_y -= 1
                    else:
                        direction = 1
                        positon_y +=1
                elif direction == 3:
                    if output_turn == 0:
                        direction = 4
                        positon_x += 1
                    else:
                        direction = 2
                        positon_x -= 1
                elif direction == 4:
                    if output_turn == 0:
                        direction = 1
                        positon_y += 1
                    else:
                        direction = 3
                        positon_y -= 1
                if (100000*positon_x + positon_y) not in panels:
                    input_parameter = 0
                else:
                    input_parameter = panels[100000*positon_x + positon_y].color
                output_index += 1
                #print(panels)
        elif opcode == 5:
            if extract_parameter(codes, i, modes, 1, relative_base) != 0:
                i = extract_parameter(codes, i, modes, 2, relative_base)
                continue
        elif opcode == 6:
            if extract_parameter(codes, i, modes, 1, relative_base) == 0:
                i = extract_parameter(codes, i, modes, 2, relative_base)
                continue 
        elif opcode == 7:
            if extract_parameter(codes, i, modes, 1, relative_base) < extract_parameter(codes, i, modes, 2, relative_base):
                if modes[2] == 2:
                    codes[codes[i+3] + relative_base] = 1
                else:
                    codes[codes[i+3]] = 1
            else:
                if modes[2] == 2:
                    codes[codes[i+3] + relative_base] = 0
                else:
                    codes[codes[i+3]] = 0
        elif  opcode ==8:
            if extract_parameter(codes, i, modes, 1, relative_base) == extract_parameter(codes, i, modes, 2, relative_base):
                if modes[2] == 2:
                    codes[codes[i+3] + relative_base] = 1
                else:
                    codes[codes[i+3]] = 1
            else:
                if modes[2] == 2:
                    codes[codes[i+3] + relative_base] = 0
                else:
                    codes[codes[i+3]] = 0
        elif opcode == 9:
            
            relative_base += extract_parameter(codes, i, modes, 1, relative_base)

        if opcode == 1 or opcode == 2 or opcode == 7 or opcode == 8:
            i += 4
        elif opcode == 3 or opcode == 4 or opcode == 9:
            i += 2
        else:
            i += 3

def part_two(codes, input_parameter):
    return part_one(codes, input_parameter)

# input parsing 
input = open("input11.txt", "r").read().split(",")
codes = []
for code in input:
    codes.append(int(code))
for i in range(0, 1000000):
    codes.append(0)

#print("Part one: ", computer(codes.copy(), 0))
#print(len(panels))
print("Part two: ", computer(codes.copy(), 1))
#file = open("output.txt", "w")
min_x = 100000
min_y = 100000
max_x = -100000
max_y = -100000
for panel in panels:
    if panels[panel].x < min_x:
        min_x = panels[panel].x
    if panels[panel].y < min_y:
        min_y = panels[panel].y
    if panels[panel].x > max_x:
        max_x = panels[panel].x
    if panels[panel].y > max_y:
        max_y = panels[panel].y
print(min_x, min_y, max_x, max_y)
grid = []
for i in range(0, abs(min_y)+abs(max_y)+20):   
    grid_line = []
    for j in range(0, abs(min_x) + abs(max_x)+20):
        grid_line.append(".")
    grid.append(grid_line)

for panel in panels:
    grid[panels[panel].y + abs(min_y)][panels[panel].x + abs(min_x)] = "X" if str(panels[panel].color) == "1" else " "

file = open("output.txt", "w")
for i in range(0, abs(min_y)+abs(max_y)+20):   
    for j in range(0, abs(min_x) + abs(max_x)+20):
        file.write(str(grid[i][j]))
    
    file.write("\n")

file.close()