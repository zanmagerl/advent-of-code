def part_one(codes, noun, verb):
    codes = list(codes)
    codes[1] = noun
    codes[2] = verb
    i = 0
    while i < len(codes):
        operation = codes[i]
        #chech if result is out of bounds
        if codes[i+1] > len(codes) or codes[i+2] > len(codes) or codes[i+3] > len(codes):
            return 0
        if operation == 99:
            break
        elif operation == 1:
            codes[codes[i+3]] = codes[codes[i+1]] + codes[codes[i+2]]
        elif operation == 2:
            codes[codes[i+3]] = codes[codes[i+1]] * codes[codes[i+2]]
        i += 4
    
    return codes[0]

def part_two(codes):
    for noun in range(0,100):
        for verb in range(0,100): 
            if part_one(codes, noun, verb) == 19690720:
                return (100 * noun + verb)   

# input parsing 
input = open("input02.txt", "r").read().split(",")
codes = []
for code in input:
    codes.append(int(code))


print("Part one: {}".format(part_one(codes, 12, 2)))
print("Part two: {}".format(part_two(codes)))
