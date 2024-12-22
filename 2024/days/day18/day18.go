package main

import (
	"bufio"
	"fmt"
	"os"
	"sort"
	"strconv"
	"strings"
)

type byte struct {
	x int
	y int
}

type candidate struct {
	x    int
	y    int
	cost int
}

func isInGrid(grid [][]bool, x int, y int) bool {
	return y >= 0 && x >= 0 && y < len(grid) && x < len(grid[y])
}

func computeKey(can candidate) string {
	return strconv.Itoa(can.y) + "," + strconv.Itoa(can.x)
}

func heuristics(can candidate) int {
	return (71 - can.y) + (71 - can.x)
}

func partOne(bytes []byte) int {
	gridSize := 71
	var grid [][]bool
	for i := 0; i < gridSize; i++ {
		var gridRow []bool
		for j := 0; j < gridSize; j++ {
			gridRow = append(gridRow, false)
		}
		grid = append(grid, gridRow)
	}

	for i := 0; i < len(bytes); i++ {
		b := bytes[i]
		grid[b.y][b.x] = true
	}

	var b candidate
	var queue []candidate
	queue = append(queue, candidate{x: 0, y: 0, cost: 0})
	cache := make(map[string]int)
	for len(queue) > 0 {
		b, queue = queue[0], queue[1:]
		cached, exists := cache[computeKey(b)]
		if exists {
			if cached <= b.cost {
				continue
			}
		}
		if !isInGrid(grid, b.x, b.y) || grid[b.y][b.x] == true {
			continue
		}
		if b.x == gridSize-1 && b.y == gridSize-1 {
			return b.cost
		}

		cache[computeKey(b)] = b.cost
		queue = append(queue, candidate{x: b.x, y: b.y - 1, cost: b.cost + 1})
		queue = append(queue, candidate{x: b.x, y: b.y + 1, cost: b.cost + 1})
		queue = append(queue, candidate{x: b.x - 1, y: b.y, cost: b.cost + 1})
		queue = append(queue, candidate{x: b.x + 1, y: b.y, cost: b.cost + 1})

		sort.Slice(queue, func(i, j int) bool {
			return queue[i].cost+heuristics(queue[i]) < queue[j].cost+heuristics(queue[j])
		})
	}
	return -1
}

func partTwo(bytes []byte) string {
	min := 1025
	max := len(bytes)
	previousVal := -1
	for min != max {
		val := (max + min) / 2
		if val == previousVal {
			break
		}
		previousVal = val
		if partOne(bytes[:val]) == -1 {
			max = val
		} else {
			min = val
		}
	}
	return strconv.Itoa(bytes[min].x) + "," + strconv.Itoa(bytes[min].y)
}

func parseInput(lines []string) []byte {
	var bytes []byte
	for _, line := range lines {
		numbers := strings.Split(line, ",")
		x, _ := strconv.Atoi(numbers[0])
		y, _ := strconv.Atoi(numbers[1])
		bytes = append(bytes, byte{x: x, y: y})
	}
	return bytes
}

func main() {

	scanner := bufio.NewScanner(os.Stdin)
	var lines []string

	for scanner.Scan() {
		line := scanner.Text()
		lines = append(lines, line)
	}
	bytes := parseInput(lines)
	fmt.Println("Part 1:", partOne(bytes[:1024]))
	fmt.Println("Part 2:", partTwo(bytes))
}
