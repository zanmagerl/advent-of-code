package main

import (
	"bufio"
	"fmt"
	"math"
	"os"
	"strconv"
	"strings"
)

type equation struct {
	result   int
	operands []int
}

func compute(equation equation, index int, result int) bool {
	if index == len(equation.operands) {
		return result == equation.result
	}
	return compute(equation, index+1, result+equation.operands[index]) || compute(equation, index+1, result*equation.operands[index])
}

func compute2(equation equation, index int, result int) bool {
	if index == len(equation.operands) {
		return result == equation.result
	}
	return compute2(equation, index+1, result+equation.operands[index]) ||
		compute2(equation, index+1, result*equation.operands[index]) ||
		compute2(equation, index+1, concatNumbers(result, equation.operands[index]))
}

func concatNumbers(a int, b int) int {

	c := int(math.Pow10(int(math.Log10(float64(b))+1)))*a + b
	return c
}

func partOne(equations []equation) int {
	sum := 0
	for _, equation := range equations {
		if compute(equation, 1, equation.operands[0]) {
			sum += equation.result
		}
	}
	return sum
}

func partTwo(equations []equation) int {
	sum := 0
	for _, equation := range equations {
		if compute2(equation, 1, equation.operands[0]) {
			sum += equation.result
		}
	}
	return sum
}

func parseInput(lines []string) []equation {
	var equations []equation
	for _, line := range lines {
		split := strings.Split(line, ":")
		result, _ := strconv.Atoi(split[0])
		operators := strings.Fields(split[1])
		var numbers []int
		for _, operator := range operators {
			num, _ := strconv.Atoi(operator)
			numbers = append(numbers, num)
		}
		equations = append(equations, equation{result: result, operands: numbers})
	}
	return equations
}

func main() {

	scanner := bufio.NewScanner(os.Stdin)
	var lines []string

	for scanner.Scan() {
		line := scanner.Text()
		lines = append(lines, line)
	}

	equations := parseInput(lines)

	fmt.Println("Part 1:", partOne(equations))
	fmt.Println("Part 2:", partTwo(equations))
}
