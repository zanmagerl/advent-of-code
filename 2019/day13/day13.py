data = list(open("input13.txt").read().split(","))

number_of_block = 0

print(len(data))
i = 0
while i < len(data):
    x = data[i]
    y = data[i+1]
    tile = data[i+2]

    if tile == 2:
        number_of_block += 1

    i += 3

print(number_of_block)