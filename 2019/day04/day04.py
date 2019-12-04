def is_not_decreasing(a):
    digits = [int(digit) for digit in str(a)] 
    i = 1
    while i < len(digits):
        if digits[i] < digits[i-1]:
           return False
        i += 1 
    return True

def is_repeated(a):
    i = 1
    a = str(a)
    while i < len(a):
        if a[i] == a[i-1]:
            return True
        i += 1
    return False

def is_repeated_exactly_twice(a):
    i = 1
    a = str(a)
    while i < len(a):
        if a[i] == a[i-1]:
            number_of_occurences = 2

            number = a[i]
            i = i+1
            while i < len(a) and a[i] == number:
                number_of_occurences += 1
                i += 1
            
            if number_of_occurences == 2:
                return True
        i += 1
    return False

def part_one(start, end):
    valid_passwords = 0
    for i in range(start, end+1):
        if is_not_decreasing(i) and is_repeated(i):
            valid_passwords += 1
    return valid_passwords

def part_two(start, end):
    valid_passwords = 0
    for i in range(start, end+1):
        if is_not_decreasing(i) and is_repeated_exactly_twice(i):
            valid_passwords += 1
    return valid_passwords    

input = open("input04.txt").read().split("-")
a = int(input[0])
b = int(input[1])

print("Part one: {}".format(part_one(a, b)))
print("Part two: {}".format(part_two(a, b)))