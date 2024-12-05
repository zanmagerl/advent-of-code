package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
)

func isInGrid(grid [][]string, row int, column int, letter string) bool {
	if row >= 0 && column >= 0 && row < len(grid) && column < len(grid[row]) {
		if grid[row][column] == letter {
			return true
		}
	}
	return false
}

func findXmas(grid [][]string, row int, column int) int {
	count := 0
	if isInGrid(grid, row-1, column, "M") && isInGrid(grid, row-2, column, "A") && isInGrid(grid, row-3, column, "S") {
		count++
	}
	if isInGrid(grid, row-1, column+1, "M") && isInGrid(grid, row-2, column+2, "A") && isInGrid(grid, row-3, column+3, "S") {
		count++
	}
	if isInGrid(grid, row, column+1, "M") && isInGrid(grid, row, column+2, "A") && isInGrid(grid, row, column+3, "S") {
		count++
	}
	if isInGrid(grid, row+1, column+1, "M") && isInGrid(grid, row+2, column+2, "A") && isInGrid(grid, row+3, column+3, "S") {
		count++
	}
	if isInGrid(grid, row+1, column, "M") && isInGrid(grid, row+2, column, "A") && isInGrid(grid, row+3, column, "S") {
		count++
	}
	if isInGrid(grid, row+1, column-1, "M") && isInGrid(grid, row+2, column-2, "A") && isInGrid(grid, row+3, column-3, "S") {
		count++
	}
	if isInGrid(grid, row, column-1, "M") && isInGrid(grid, row, column-2, "A") && isInGrid(grid, row, column-3, "S") {
		count++
	}
	if isInGrid(grid, row-1, column-1, "M") && isInGrid(grid, row-2, column-2, "A") && isInGrid(grid, row-3, column-3, "S") {
		count++
	}
	return count
}

func find2MAS(grid [][]string, row int, column int) int {
	count := 0
	if isInGrid(grid, row-1, column-1, "M") && isInGrid(grid, row+1, column+1, "S") {
		if isInGrid(grid, row-1, column+1, "M") && isInGrid(grid, row+1, column-1, "S") {
			count++
		}
		if isInGrid(grid, row-1, column+1, "S") && isInGrid(grid, row+1, column-1, "M") {
			count++
		}
	}
	if isInGrid(grid, row-1, column-1, "S") && isInGrid(grid, row+1, column+1, "M") {
		if isInGrid(grid, row-1, column+1, "M") && isInGrid(grid, row+1, column-1, "S") {
			count++
		}
		if isInGrid(grid, row-1, column+1, "S") && isInGrid(grid, row+1, column-1, "M") {
			count++
		}
	}
	return count
}

func partOne(lines []string) int {
	var grid [][]string
	for _, line := range lines {
		grid = append(grid, strings.Split(line, ""))
	}
	sum := 0
	for row, _ := range grid {
		for column, _ := range grid[row] {
			if grid[row][column] == "X" {
				count := findXmas(grid, row, column)
				sum += count
			}
		}
	}
	return sum
}

func partTwo(lines []string) int {
	var grid [][]string
	for _, line := range lines {
		grid = append(grid, strings.Split(line, ""))
	}
	sum := 0
	for row, _ := range grid {
		for column, _ := range grid[row] {
			if grid[row][column] == "A" {
				count := find2MAS(grid, row, column)
				sum += count
			}
		}
	}
	return sum
}

func main() {

	scanner := bufio.NewScanner(os.Stdin)
	var lines []string

	for scanner.Scan() {
		line := scanner.Text()
		lines = append(lines, line)
	}

	fmt.Println("Part 1:", partOne(lines))
	fmt.Println("Part 2:", partTwo(lines))
}
