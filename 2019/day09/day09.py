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

def part_one(codes, input_parameter):

    relative_base = 0

    i = 0
    while i < len(codes):

        opcode = int(str(codes[i])[-2:])
        modes = get_mode(codes[i])

        if opcode == 99:
            return
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
            print(extract_parameter(codes, i, modes, 1, relative_base))
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
input = open("input09.txt", "r").read().split(",")
codes = []
for code in input:
    codes.append(int(code))

for i in range(0,100000):
    codes.append(0)

test_mode_part_one = 1
sensors_boost_mode = 2

print("Part one: ", end ="")
part_one(codes.copy(), test_mode_part_one)
print("Part two: ", end ="")
part_two(codes.copy(), sensors_boost_mode)
