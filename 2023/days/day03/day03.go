package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
)

type Part struct {
	x1    int
	y1    int
	x2    int
	y2    int
	value int
}

type Symbol struct {
	x int
	y int
	c rune
}

func (p Part) isNeighbourOfSymbol(s Symbol) bool {
	return s.x >= p.x1-1 && s.x <= p.x2+1 && s.y >= p.y1-1 && s.y <= p.y2+1
}

func (p Part) isNeighbourOfAnySymbol(symbols []Symbol) bool {
	for _, s := range symbols {
		if p.isNeighbourOfSymbol(s) {
			return true
		}
	}
	return false
}

func (s Symbol) isNeighbourOfTwoParts(parts []Part) (bool, int) {
	numberOfNeightbours := 0
	gearRatio := 1
	for _, part := range parts {
		if part.isNeighbourOfSymbol(s) {
			numberOfNeightbours++
			gearRatio *= part.value
		}
	}

	return numberOfNeightbours == 2, gearRatio
}

func partOne(parts []Part, symbols []Symbol) int {
	sum := 0
	for _, part := range parts {
		if part.isNeighbourOfAnySymbol(symbols) {
			sum += part.value
		}
	}
	return sum
}

func partTwo(parts []Part, symbols []Symbol) int {
	sum := 0
	for _, symbol := range symbols {
		if symbol.c == '*' {
			isGear, gearRatio := symbol.isNeighbourOfTwoParts(parts)
			if isGear {
				sum += gearRatio
			}
		}
	}
	return sum
}

func parseInput(lines []string) ([]Part, []Symbol) {
	var parts []Part
	var symbols []Symbol

	for y, line := range lines {
		chars := []rune(line)
		currentNumber := ""
		for x, char := range chars {
			if char >= '0' && char <= '9' {
				currentNumber += string(char)
			} else if char == '.' {
				curNum, _ := strconv.Atoi(currentNumber)
				if currentNumber != "" {
					parts = append(parts, Part{x - len(currentNumber), y, x - 1, y, curNum})
					currentNumber = ""
				}
			} else {
				if currentNumber != "" {
					curNum, _ := strconv.Atoi(currentNumber)
					parts = append(parts, Part{x - len(currentNumber), y, x - 1, y, curNum})
					currentNumber = ""
				}
				symbols = append(symbols, Symbol{x, y, char})
			}
		}
		if currentNumber != "" {
			curNum, _ := strconv.Atoi(currentNumber)
			parts = append(parts, Part{len(line) - len(currentNumber), y, len(line) - 1, y, curNum})
		}
	}
	for _, part := range parts {
		fmt.Println(part)
	}
	return parts, symbols
}

func main() {

	scanner := bufio.NewScanner(os.Stdin)
	var lines []string

	for scanner.Scan() {
		line := scanner.Text()
		lines = append(lines, line)
	}

	parts, symbols := parseInput(lines)

	fmt.Println("Part 1:", partOne(parts, symbols))
	fmt.Println("Part 2:", partTwo(parts, symbols))
}
