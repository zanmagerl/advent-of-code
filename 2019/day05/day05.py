
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

def extract_parameter(codes, i, modes, parameter):
    if modes[parameter-1] == 0:
        return codes[codes[i+parameter]]
    else:
        return codes[i+parameter]

def part_one(codes, input_parameter):
    
    codes = list(codes)
    
    i = 0
    while i < len(codes):
        
        opcode = int(str(codes[i])[-2:])
        modes = get_mode(codes[i])

        if opcode == 99:
            return
        elif opcode == 1:
            codes[codes[i+3]] = extract_parameter(codes, i, modes, 1) + extract_parameter(codes, i, modes, 2)
           
        elif opcode == 2:
            codes[codes[i+3]] = extract_parameter(codes, i, modes, 1) * extract_parameter(codes, i, modes, 2)

        elif opcode == 3:
            codes[codes[i+1]] = input_parameter
        else:
            print(extract_parameter(codes, i, modes, 1))

        if opcode == 1 or opcode == 2:
            i += 4
        else:
            i += 2

def part_two(codes, input_parameter):
    
    codes = list(codes)
    
    i = 0
    while i < len(codes):
        
        opcode = int(str(codes[i])[-2:])
        modes = get_mode(codes[i])

        if opcode == 99:
            return
        elif opcode == 1:
            codes[codes[i+3]] = extract_parameter(codes, i, modes, 1) + extract_parameter(codes, i, modes, 2)
           
        elif opcode == 2:
            codes[codes[i+3]] = extract_parameter(codes, i, modes, 1) * extract_parameter(codes, i, modes, 2)

        elif opcode == 3:
            codes[codes[i+1]] = input_parameter
        elif opcode == 4:
            print(extract_parameter(codes, i, modes, 1))
        elif opcode == 5:
            if extract_parameter(codes, i, modes, 1) != 0:
                i = extract_parameter(codes, i, modes, 2)
                continue
        elif opcode == 6:
            if extract_parameter(codes, i, modes, 1) == 0:
                i = extract_parameter(codes, i, modes, 2)
                continue 
        elif opcode == 7:
            if extract_parameter(codes, i, modes, 1) < extract_parameter(codes, i, modes, 2):
                codes[codes[i+3]] = 1
            else:
                codes[codes[i+3]] = 0
        else:
            if extract_parameter(codes, i, modes, 1) == extract_parameter(codes, i, modes, 2):
                codes[codes[i+3]] = 1
            else:
                codes[codes[i+3]] = 0

        if opcode == 1 or opcode == 2 or opcode == 7 or opcode == 8:
            i += 4
        elif opcode == 3 or opcode == 4:
            i += 2
        else:
            i += 3

# input parsing 
input = open("input05.txt", "r").read().split(",")
codes = []
for code in input:
    codes.append(int(code))

test_condition_unit = 1
system_thermal_controller = 5

part_one(codes, test_condition_unit)
part_two(codes, system_thermal_controller)

'''
We do not have to clone input, because with input_parameter (1 and 5) we kind of choose
which program we want to run. Program 5 is far away, and we never reach those
addresses with program 1.
Moreover, we can understand the beginning of our input like a simple bootloader:
1. step: load number in location 225                                                (255 <- 1 or 5)
2. step: add that loaded number to the location 6                                   (6 <- (255))
3. step: (location 6): 
        if program 1, simply continue with program,
        if program 5, far jump to location 238 and there continue with the program
'''