package main

import (
	"bufio"
	"fmt"
	"os"
)

func partOne() int {
	return -1
}

func partTwo() int {
	return -1
}

func parseInput(lines []string) int {
	return -1
}

func main() {

	scanner := bufio.NewScanner(os.Stdin)
	var lines []string

	for scanner.Scan() {
		line := scanner.Text()
		lines = append(lines, line)
	}

	parseInput(lines)

	fmt.Println("Part 1:", partOne())
	fmt.Println("Part 2:", partTwo())
}
