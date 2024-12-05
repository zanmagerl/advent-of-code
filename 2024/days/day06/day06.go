package main

import (
	"bufio"
	"fmt"
	"os"
)

func partOne(lines []string) int {
	return -1
}

func partTwo(lines []string) int {
	return -1
}

func main() {

	scanner := bufio.NewScanner(os.Stdin)
	var lines []string

	for scanner.Scan() {
		line := scanner.Text()
		lines = append(lines, line)
	}

	fmt.Println("Part 1:", partOne(lines))
	//fmt.Println("Part 2:", partTwo(lines))
}
