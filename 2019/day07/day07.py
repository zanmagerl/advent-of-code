import itertools

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

def amplifier(codes, phase_setting, output_from_previous):
    
    number_of_inputs = 0

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
            value = phase_setting if number_of_inputs == 0 else output_from_previous
            codes[codes[i+1]] = value
            number_of_inputs += 1
        elif opcode == 4:
            return extract_parameter(codes, i, modes, 1)
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

def interuptable_amplifier(codes, phase_setting, output_from_previous, program_counter):
    
    number_of_inputs = 0

    i = program_counter
    while i < len(codes):
        
        opcode = int(str(codes[i])[-2:])
        modes = get_mode(codes[i])

        if opcode == 99:
            return None
        elif opcode == 1:
            codes[codes[i+3]] = extract_parameter(codes, i, modes, 1) + extract_parameter(codes, i, modes, 2)
        elif opcode == 2:
            codes[codes[i+3]] = extract_parameter(codes, i, modes, 1) * extract_parameter(codes, i, modes, 2)
        elif opcode == 3:
            # phase setting is used exactly once as the input to the amplifier
            value = phase_setting if program_counter == 0 and number_of_inputs == 0 else output_from_previous
            codes[codes[i+1]] = value
            number_of_inputs += 1
        elif opcode == 4:
            return (extract_parameter(codes, i, modes, 1), codes, i+2)
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

# Function checks if any amplifier halted
def over(states):
    for state in states:
        if state is None:
            return True
    return False

def part_one(codes, configiration):
    
    max_signal = 0
    
    permutations = list(itertools.permutations(configiration))

    for permutation in permutations:
        
        previous_output = 0
        for i in range(0,5):
            codes_copy = codes.copy()
            previous_output = amplifier(codes_copy, permutation[i], previous_output)

        if previous_output > max_signal:
            max_signal = previous_output
            
    return max_signal 

def part_two(codes, configuration):
    
    max_signal = 0
    
    permutations = list(itertools.permutations(configuration))

    for permutation in permutations:

        # "Data structure" to keep track of states of amplifiers
        previous_output = [
            (0, codes.copy(), 0),
            (0, codes.copy(), 0),
            (0, codes.copy(), 0),
            (0, codes.copy(), 0),
            (0, codes.copy(), 0),
            ]
        
        while True and not over(previous_output):
            
            for i in range(0,5):

                if(over(previous_output)):
                    break

                # arguments: code of amplifier, phase setting, output of the previous amplifier, program counter
                previous_output[i] = interuptable_amplifier(
                    previous_output[i][1],                                             
                    permutation[i],                                                     
                    (previous_output[i-1][0] if i > 0 else previous_output[4][0]), 
                    previous_output[i][2]
                    )
                
            if previous_output[4][2] >= len(codes):
                break
        
        if previous_output[4][0] > max_signal:
            max_signal = previous_output[4][0]

    return max_signal   
  
# input parsing 
input = open("input07.txt", "r").read().split(",")
codes = []
for code in input:
    codes.append(int(code))

part_one_configuration = [0,1,2,3,4]
part_two_configuration = [5,6,7,8,9]

print(part_one(codes, part_one_configuration))
print(part_two(codes, part_two_configuration))
