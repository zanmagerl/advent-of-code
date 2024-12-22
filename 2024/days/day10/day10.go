package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

func isInGrid(topologicalMap [][]int, row int, column int) bool {
	return row >= 0 && column >= 0 && row < len(topologicalMap) && column < len(topologicalMap[row])
}

func findTrails(topologicalMap [][]int, row int, column int, previousValue int) int {
	if !isInGrid(topologicalMap, row, column) {
		return 0
	}
	position := topologicalMap[row][column]
	if position-previousValue == 1 {
		if position == 9 {
			return 1
		}
		return findTrails(topologicalMap, row-1, column, position) + findTrails(topologicalMap, row, column+1, position) + findTrails(topologicalMap, row+1, column, position) + findTrails(topologicalMap, row, column-1, position)
	}
	return 0
}

func findPeaks(topologicalMap [][]int, row int, column int, previousValue int) []string {
	if !isInGrid(topologicalMap, row, column) {
		return []string{}
	}
	position := topologicalMap[row][column]
	if position-previousValue == 1 {
		if position == 9 {
			return []string{strconv.Itoa(row) + "," + strconv.Itoa(column)}
		}
		up := findPeaks(topologicalMap, row-1, column, position)
		left := findPeaks(topologicalMap, row, column+1, position)
		down := findPeaks(topologicalMap, row+1, column, position)
		right := findPeaks(topologicalMap, row, column-1, position)

		var result []string
		for _, el := range up {
			result = append(result, el)
		}
		for _, el := range left {
			result = append(result, el)
		}
		for _, el := range down {
			result = append(result, el)
		}
		for _, el := range right {
			result = append(result, el)
		}
		return result
	}
	return []string{}
}

func partOne(topologicalMap [][]int) int {
	sum := 0
	for row, _ := range topologicalMap {
		for column, _ := range topologicalMap[row] {
			position := topologicalMap[row][column]
			// Potential start
			if position == 0 {
				peaks := findPeaks(topologicalMap, row, column, -1)

				peakSet := map[string]bool{}
				for _, peak := range peaks {
					peakSet[peak] = true
				}

				sum += len(peakSet)
			}
		}
	}
	return sum
}

func partTwo(topologicalMap [][]int) int {
	sum := 0
	for row, _ := range topologicalMap {
		for column, _ := range topologicalMap[row] {
			position := topologicalMap[row][column]
			// Potential start
			if position == 0 {
				sum += findTrails(topologicalMap, row, column, -1)
			}
		}
	}
	return sum
}

func parseInput(lines []string) [][]int {
	var numbers [][]int
	for _, line := range lines {
		var numList []int
		nums := strings.Split(line, "")
		for _, number := range nums {
			num, _ := strconv.Atoi(number)
			numList = append(numList, num)
		}
		numbers = append(numbers, numList)
	}
	return numbers
}

func main() {

	scanner := bufio.NewScanner(os.Stdin)
	var lines []string

	for scanner.Scan() {
		line := scanner.Text()
		lines = append(lines, line)
	}

	fmt.Println("Part 1:", partOne(parseInput(lines)))
	fmt.Println("Part 2:", partTwo(parseInput(lines)))
}
