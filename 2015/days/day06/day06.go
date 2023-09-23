package main

import (
	"bufio"
	"fmt"
	"magerl.si/advent-of-code/2015/utils"
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
	id            int
	startLocation Location
	endLocation   Location
	operation     Operation
}

func (instruction Instruction) Equal(a Instruction) bool {
	return instruction.id == a.id
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
			lights[location] = utils.Max(0, lights[location]-1)
		case Toggle:
			lights[location] += 2
		}
	}
	return lights
}

func partOneBasic(instructions []Instruction) int {
	lights := make(map[Location]int)
	for _, instruction := range instructions {
		//fmt.Println(instruction.id)
		lights = processInstruction(instruction, lights)
	}

	numberOfTurnedOnLights := 0
	for _, value := range lights {
		numberOfTurnedOnLights += value
	}
	return numberOfTurnedOnLights
}

func partTwoBasic(instructions []Instruction) int {
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

func parseInput(id int, line string) Instruction {
	splitLine := strings.Split(line, " ")
	if strings.HasPrefix(line, string(TurnOn)) || strings.HasPrefix(line, string(TurnOff)) {
		return Instruction{id: id, operation: Operation(splitLine[0] + " " + splitLine[1]), startLocation: parseLocation(splitLine[2]), endLocation: parseLocation(splitLine[4])}
	} else if strings.HasPrefix(line, string(Toggle)) {
		return Instruction{id: id, operation: Toggle, startLocation: parseLocation(splitLine[1]), endLocation: parseLocation(splitLine[3])}
	} else {
		panic("Unknown start of line: " + line)
	}
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)

	var lines []Instruction
	for i := 0; scanner.Scan(); i++ {
		lines = append(lines, parseInput(i, scanner.Text()))
	}
	println("Basic algorithm:")
	fmt.Println("Part 1:", partOneBasic(lines))
	fmt.Println("Part 2:", partTwoBasic(lines))

	for i := 0; i < len(lines); i++ {
		// Algorithm is written for exclusive coordinates
		lines[i].endLocation.x++
		lines[i].endLocation.y++
	}
	fmt.Println("\nSweeping algorithm:")
	fmt.Println("Part 1:", partOneSweep(lines))
	fmt.Println("Part 2:", partTwoSweep(lines))
}
