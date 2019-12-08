def count_zeros(layer):
    number_of_zeros = 0
    for pixel in layer:
        if pixel == 0:
            number_of_zeros += 1

    return number_of_zeros

def part_one(layers):
    min_zeros = 1000000
    the_layer = layers[0]
    for layer in layers:
        
        zeros = count_zeros(layer)

        if zeros < min_zeros:
            min_zeros = zeros
            the_layer = layer
    
    ones = 0
    twos = 0
    for pixel in the_layer:
        if pixel == 1:
            ones += 1
        elif pixel == 2:
            twos += 1

    return ones * twos

def part_two(layers, width, height):

    visible_layer = layers[0]

    i = 1
    while i < len(layers):
        layer = layers[i]
        j = 0
        while j < len(layer):
            # if pixel in the visible layer is transparent, we see pixels beneath it
            if visible_layer[j] == 2:
                visible_layer[j] = layer[j]
            j += 1
        i += 1
    
    output = open("output.txt", "w")
    for y in range(0, height):
        for x in range(0, width):
            color = visible_layer[y*width + x]
            if color == 0:
                output.write(" ")
            else:
                output.write("X")
        output.write("\n")
    output.close()
    return open("output.txt", "r").read()

# input parsing 
input = [int(number) for number in list(open("input08.txt", "r").read())]

width = 25
height = 6
picture_size = width * height
number_of_layers = len(input) // picture_size

layers = []
last_i = 0
for j in range(0, number_of_layers):
    pixels = []
    for i in range(0,picture_size):
        pixels.append(input[i+last_i])
    layers.append(pixels)
    last_i += picture_size

print("Part one: {}".format(part_one(layers)))
print("Part two:\n{}".format(part_two(layers, width, height)))
