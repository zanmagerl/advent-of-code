package main

import (
	"fmt"
	"magerl.si/advent-of-code/2015/utils"
	"math"
	"sort"
	"strconv"
	"strings"
)

type ProcessedInstruction struct {
	name          string
	startLocation Location
	endLocation   Location
	instructions  []Instruction
}

func (instruction ProcessedInstruction) toString() string {
	overlaps := ""
	for i, instruction := range instruction.instructions {
		if i != 0 {
			overlaps += ","
		}
		overlaps = strings.Join([]string{overlaps, strconv.Itoa(instruction.id)}, "")
	}

	return fmt.Sprintf("(%d,%d) -> (%d,%d) [%s]", instruction.startLocation.x, instruction.startLocation.y, instruction.endLocation.x, instruction.endLocation.y, overlaps)
}

func generateAllYs(instructions []Instruction) []int {
	keys := utils.New[int]()
	for _, instruction := range instructions {
		keys.Add(instruction.startLocation.y)
		keys.Add(instruction.endLocation.y)
	}
	elements := keys.Elements()
	sort.Slice(elements, func(i, j int) bool {
		return elements[i] < elements[j]
	})
	return elements
}

func generateAllXs(instructions []Instruction) []int {
	keys := utils.New[int]()
	for _, instruction := range instructions {
		keys.Add(instruction.startLocation.x)
		keys.Add(instruction.endLocation.x)
	}
	elements := keys.Elements()
	sort.Slice(elements, func(i, j int) bool {
		return elements[i] < elements[j]
	})
	return elements
}

func sweepAlgorithm(instructions []Instruction) []ProcessedInstruction {

	// Sorting instructions by x1 coordinate
	sort.Slice(instructions, func(i, j int) bool {
		return instructions[i].startLocation.x < instructions[j].startLocation.x
	})

	var processedInstructions []ProcessedInstruction

	sweepingLineX := instructions[0].startLocation.x

	var currentXActiveInstructions []Instruction

	for _, instructionWithX := range instructions {
		if instructionWithX.startLocation.x == instructions[0].startLocation.x {
			currentXActiveInstructions = append(currentXActiveInstructions, instructionWithX)
		} else {
			break
		}
	}

	allXs := generateAllXs(instructions)
	for i := 1; i < len(allXs) && len(currentXActiveInstructions) > 0; i++ {
		nextLineX := allXs[i]

		// Sort current instructions by y1 coordinate
		sort.Slice(currentXActiveInstructions, func(i, j int) bool {
			return currentXActiveInstructions[i].startLocation.y < currentXActiveInstructions[j].startLocation.y
		})

		sweepingLineY := currentXActiveInstructions[0].startLocation.y

		var currentYActiveInstructions []Instruction
		for _, instructionWithY := range currentXActiveInstructions {
			if instructionWithY.startLocation.y == currentXActiveInstructions[0].startLocation.y {
				currentYActiveInstructions = append(currentYActiveInstructions, instructionWithY)
			} else {
				break
			}
		}

		allYs := generateAllYs(currentXActiveInstructions)
		for j := 1; j < len(allYs); j++ {
			nextLineY := allYs[j]

			// Add new processed instruction
			processedInstructions = append(processedInstructions, ProcessedInstruction{
				name:          strconv.Itoa(len(processedInstructions)),
				startLocation: Location{sweepingLineX, sweepingLineY},
				endLocation:   Location{nextLineX, nextLineY},
				instructions:  currentYActiveInstructions[:],
			})

			// Remove instructions
			var newCurrentActiveInstructionsY []Instruction
			for _, currentActiveInstructionY := range currentYActiveInstructions {
				if nextLineY < currentActiveInstructionY.endLocation.y {
					newCurrentActiveInstructionsY = append(newCurrentActiveInstructionsY, currentActiveInstructionY)
				}
			}
			currentYActiveInstructions = newCurrentActiveInstructionsY[:]

			setY := utils.New[Instruction]()
			for _, el := range currentYActiveInstructions {
				setY.Add(el)
			}

			for _, currentXActiveInstruction := range currentXActiveInstructions {
				if currentXActiveInstruction.startLocation.y <= nextLineY && currentXActiveInstruction.endLocation.y > nextLineY && !setY.Contains(currentXActiveInstruction) {
					setY.Add(currentXActiveInstruction)
				}
			}

			currentYActiveInstructions = setY.Elements()
			sweepingLineY = nextLineY
		}

		// Update active instructions
		var newCurrentActiveInstructionsX []Instruction
		for _, currentActiveInstruction := range currentXActiveInstructions {
			if nextLineX < currentActiveInstruction.endLocation.x {
				newCurrentActiveInstructionsX = append(newCurrentActiveInstructionsX, currentActiveInstruction)
			}
		}
		currentXActiveInstructions = newCurrentActiveInstructionsX[:]

		setX := utils.New[Instruction]()
		for _, el := range currentXActiveInstructions {
			setX.Add(el)
		}

		for _, instruction := range instructions {
			if instruction.startLocation.x <= nextLineX && instruction.endLocation.x > nextLineX && !setX.Contains(instruction) {
				setX.Add(instruction)
			}
		}

		currentXActiveInstructions = setX.Elements()
		sweepingLineX = nextLineX
	}

	for _, processedInstruction := range processedInstructions {
		// Sort instructions per id to retain input order
		sort.Slice(processedInstruction.instructions, func(i, j int) bool {
			return processedInstruction.instructions[i].id < processedInstruction.instructions[j].id
		})
	}

	return processedInstructions
}

func partOneSweep(instructions []Instruction) int {

	processedInstructions := sweepAlgorithm(instructions)

	sumOfLights := 0

	for _, processedInstruction := range processedInstructions {
		state := 0
		for _, instruction := range processedInstruction.instructions {
			switch instruction.operation {
			case TurnOn:
				state = 1
			case TurnOff:
				state = 0
			case Toggle:
				state = int(math.Abs(float64(state - 1)))
			}
		}
		sumOfLights += state * (processedInstruction.endLocation.y - processedInstruction.startLocation.y) * (processedInstruction.endLocation.x - processedInstruction.startLocation.x)
	}

	return sumOfLights
}

func partTwoSweep(instructions []Instruction) int {

	processedInstructions := sweepAlgorithm(instructions)

	sumOfLights := 0

	for _, processedInstruction := range processedInstructions {
		state := 0
		for _, instruction := range processedInstruction.instructions {
			switch instruction.operation {
			case TurnOn:
				state += 1
			case TurnOff:
				state = utils.Max(0, state-1)
			case Toggle:
				state += 2
			}
		}
		sumOfLights += state * (processedInstruction.endLocation.y - processedInstruction.startLocation.y) * (processedInstruction.endLocation.x - processedInstruction.startLocation.x)
	}

	return sumOfLights
}
