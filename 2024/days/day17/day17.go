package main

import (
	"bufio"
	"fmt"
	"math"
	"os"
	"sort"
	"strconv"
	"strings"
)

func mapOperand(registerA int, registerB int, registerC int, operand int) int {
	if operand >= 0 && operand <= 3 {
		return operand
	}
	if operand == 4 {
		return registerA
	}
	if operand == 5 {
		return registerB
	}
	if operand == 6 {
		return registerC
	}
	panic("Invalid operand")
}

func executeInstruction(registerA int, registerB int, registerC int, instruction int, operand int, intructionPointer int) (int, int, int, int, string) {
	comboOperand := mapOperand(registerA, registerB, registerC, operand)
	output := ""
	if instruction == 0 {
		registerA = registerA / int(math.Pow(2, float64(comboOperand)))
	} else if instruction == 1 {
		registerB = registerB ^ operand
	} else if instruction == 2 {
		registerB = comboOperand % 8
	} else if instruction == 3 {
		if registerA == 0 {
			return registerA, registerB, registerC, intructionPointer + 2, output
		}
		return registerA, registerB, registerC, operand, output
	} else if instruction == 4 {
		registerB = registerB ^ registerC
	} else if instruction == 5 {
		output = strconv.Itoa(comboOperand % 8)
	} else if instruction == 6 {
		registerB = registerA / int(math.Pow(2, float64(comboOperand)))
	} else if instruction == 7 {
		registerC = registerA / int(math.Pow(2, float64(comboOperand)))
	}

	return registerA, registerB, registerC, intructionPointer + 2, output
}

func partOne(registerA int, registerB int, registerC int, instructions []int) string {

	var output string
	finalOutput := ""
	instructionPointer := 0

	for instructionPointer < len(instructions) {
		registerA, registerB, registerC, instructionPointer, output = executeInstruction(registerA, registerB, registerC, instructions[instructionPointer], instructions[instructionPointer+1], instructionPointer)
		if output != "" {
			if finalOutput != "" {
				finalOutput += ","
			}
			finalOutput += output
		}
	}

	return finalOutput
}

func partTwo(registerA int, registerB int, registerC int, instructions []int) int {
	var possibleAs []int
	possibleAs = append(possibleAs, 0)
	for i := len(instructions) - 1; i >= 0; i-- {
		var iterAs []int
		for _, aCandidate := range possibleAs {
			for ai := 0; ai < 8; ai++ {
				a := aCandidate*8 + ai
				b := ai
				b = b ^ 5
				c := a / int(math.Pow(2, float64(b)))
				b = b ^ 6
				b = b ^ c
				if b%8 == instructions[i] {
					iterAs = append(iterAs, a)
				}
			}
		}

		possibleAs = iterAs
	}
	sort.Slice(possibleAs, func(i, j int) bool {
		return possibleAs[i] < possibleAs[j]
	})
	return possibleAs[0]
}

func parseInput(lines []string) (int, int, int, []int) {
	a, _ := strconv.Atoi(strings.Split(lines[0], " ")[2])
	b, _ := strconv.Atoi(strings.Split(lines[1], " ")[2])
	c, _ := strconv.Atoi(strings.Split(lines[2], " ")[2])

	var instructions []int

	split := strings.Split(strings.Split(lines[4], " ")[1], ",")
	for _, s := range split {
		ins, _ := strconv.Atoi(s)
		instructions = append(instructions, ins)
	}

	return a, b, c, instructions
}

func main() {

	scanner := bufio.NewScanner(os.Stdin)
	var lines []string

	for scanner.Scan() {
		line := scanner.Text()
		lines = append(lines, line)
	}

	a, b, c, instructions := parseInput(lines)

	fmt.Println("Part 1:", partOne(a, b, c, instructions))
	fmt.Println("Part 2:", partTwo(a, b, c, instructions))
}
