package main

import (
	"bufio"
	"fmt"
	"math"
	"os"
	"strconv"
	"strings"
)

type Operation string

const (
	TurnOn  Operation = "turn on"
	TurnOff Operation = "turn off"
	Toggle  Operation = "toggle"
)

type Location struct {
	x int
	y int
}

type Instruction struct {
	startLocation Location
	endLocation   Location
	operation     Operation
}

func (instruction Instruction) toString() string {
	return fmt.Sprintf("%s: %d,%d -> %d,%d", instruction.operation, instruction.startLocation.x, instruction.startLocation.y, instruction.endLocation.x, instruction.endLocation.y)
}

func generateGrid(startLocation Location, endLocation Location) []Location {
	grid := []Location{}
	for x := startLocation.x; x <= endLocation.x; x++ {
		for y := startLocation.y; y <= endLocation.y; y++ {
			grid = append(grid, Location{x, y})
		}
	}
	return grid
}

func processInstruction(instruction Instruction, lights map[Location]int) map[Location]int {
	grid := generateGrid(instruction.startLocation, instruction.endLocation)
	for _, location := range grid {
		currentLight := lights[location]
		switch instruction.operation {
		case TurnOn:
			lights[location] = 1
		case TurnOff:
			lights[location] = 0
		case Toggle:
			lights[location] = int(math.Abs(float64(currentLight - 1)))
		}
	}
	return lights
}

func processInstructionV2(instruction Instruction, lights map[Location]int) map[Location]int {
	grid := generateGrid(instruction.startLocation, instruction.endLocation)
	for _, location := range grid {
		switch instruction.operation {
		case TurnOn:
			lights[location] += 1
		case TurnOff:
			lights[location] = int(math.Max(0, float64(lights[location]-1)))
		case Toggle:
			lights[location] += 2
		}
	}
	return lights
}

func partOne(instructions []Instruction) int {
	lights := make(map[Location]int)
	for _, instruction := range instructions {
		lights = processInstruction(instruction, lights)
	}

	numberOfTurnedOnLights := 0
	for _, value := range lights {
		numberOfTurnedOnLights += value
	}
	return numberOfTurnedOnLights
}

func partTwo(instructions []Instruction) int {
	lights := make(map[Location]int)
	for _, instruction := range instructions {
		lights = processInstructionV2(instruction, lights)
	}

	numberOfTurnedOnLights := 0
	for _, value := range lights {
		numberOfTurnedOnLights += value
	}
	return numberOfTurnedOnLights
}

func parseLocation(str string) Location {
	location := strings.Split(str, ",")[0:2]
	x, _ := strconv.Atoi(location[0])
	y, _ := strconv.Atoi(location[1])
	return Location{x, y}
}

func parseInput(line string) Instruction {
	splitLine := strings.Split(line, " ")
	if strings.HasPrefix(line, string(TurnOn)) || strings.HasPrefix(line, string(TurnOff)) {
		return Instruction{operation: Operation(splitLine[0] + " " + splitLine[1]), startLocation: parseLocation(splitLine[2]), endLocation: parseLocation(splitLine[4])}
	} else if strings.HasPrefix(line, string(Toggle)) {
		return Instruction{operation: Toggle, startLocation: parseLocation(splitLine[1]), endLocation: parseLocation(splitLine[3])}
	} else {
		panic("Unknown start of line: " + line)
	}
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)

	var lines []Instruction

	for scanner.Scan() {
		lines = append(lines, parseInput(scanner.Text()))
	}

	fmt.Println("Part 1:", partOne(lines))
	fmt.Println("Part 2:", partTwo(lines))
}
