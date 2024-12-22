package main

import (
	"bufio"
	"fmt"
	"math"
	"os"
	"sort"
	"strconv"
)

type location struct {
	x int
	y int
}

type candidate struct {
	x         int
	y         int
	dx        int
	dy        int
	cost      int
	length    int
	locations []location
}

func isInGrid(grid [][]rune, candidate candidate) bool {
	if candidate.x >= 0 && candidate.y >= 0 && candidate.y < len(grid) && candidate.x < len(grid[candidate.y]) {
		if grid[candidate.y][candidate.x] == '#' {
			return false
		}
		return true
	}
	return false
}

func rotateAndMove(possibleCandidate candidate, dx int, dy int) candidate {
	if possibleCandidate.dx == dx && possibleCandidate.dy == dy {
		newLocation := location{x: possibleCandidate.x, y: possibleCandidate.y}
		var newLocations []location
		for _, loc := range possibleCandidate.locations {
			newLocations = append(newLocations, loc)
		}
		newLocations = append(newLocations, newLocation)
		// Then move
		return candidate{x: possibleCandidate.x + dx, y: possibleCandidate.y + dy, dx: dx, dy: dy, cost: possibleCandidate.cost + 1, length: possibleCandidate.length + 1, locations: newLocations}
	}
	// Otherwise only rotate
	return candidate{x: possibleCandidate.x, y: possibleCandidate.y, dx: dx, dy: dy, cost: possibleCandidate.cost + 1000, length: possibleCandidate.length, locations: possibleCandidate.locations}
}

func calculateDistance(x1 int, y1 int, x2 int, y2 int) int {
	return int(math.Abs(float64(x2-x1))) + int(math.Abs(float64(y2-y1)))
	// xd := x2 - x1
	// yd := y2 - y1
	// if xd < 0 {
	// 	xd = -xd
	// }
	// if yd < 0 {
	// 	yd = -yd
	// }
	// return xd + yd
}

func calculateKey(can candidate) string {
	return strconv.Itoa(can.x) + "," + strconv.Itoa(can.y) + "," + strconv.Itoa(can.dx) + "," + strconv.Itoa(can.dy)
}

func partOne(grid [][]rune, start location, end location) int {
	cache := make(map[string]int)
	var possibleCandidate candidate
	var candidates []candidate
	candidates = append(candidates, candidate{x: start.x, y: start.y, dx: 1, dy: 0, cost: 0, length: 0})
	for len(candidates) > 0 {
		possibleCandidate, candidates = candidates[0], candidates[1:]
		if possibleCandidate.x == end.x && possibleCandidate.y == end.y {
			return possibleCandidate.cost
		}

		if !isInGrid(grid, possibleCandidate) {
			continue
		}
		cachedCosts, exits := cache[calculateKey(possibleCandidate)]
		if exits && cachedCosts <= possibleCandidate.cost {
			continue
		}

		cache[calculateKey(possibleCandidate)] = possibleCandidate.cost
		candidates = append(candidates, rotateAndMove(possibleCandidate, 1, 0))
		candidates = append(candidates, rotateAndMove(possibleCandidate, 0, 1))
		candidates = append(candidates, rotateAndMove(possibleCandidate, -1, 0))
		candidates = append(candidates, rotateAndMove(possibleCandidate, 0, -1))
		// Sort by cost descending
		sort.Slice(candidates, func(i, j int) bool {
			return candidates[i].cost+calculateDistance(candidates[i].x, candidates[i].y, end.x, end.y) < candidates[j].cost+calculateDistance(candidates[j].x, candidates[j].y, end.x, end.y)
		})
	}

	return -1
}

func partTwo(grid [][]rune, start location, end location) int {
	minCost := 10000000
	cache := make(map[string]int)
	var possibleCandidate candidate
	var candidates []candidate
	candidates = append(candidates, candidate{x: start.x, y: start.y, dx: 1, dy: 0, cost: 0, length: 0, locations: []location{location{x: start.x, y: start.y}}})

	var visited [][]bool
	for _, row := range grid {
		var rowGrid []bool
		for i := range row {
			i = i
			rowGrid = append(rowGrid, false)
		}
		visited = append(visited, rowGrid)
	}

	for len(candidates) > 0 {
		possibleCandidate, candidates = candidates[0], candidates[1:]

		if possibleCandidate.cost > minCost {
			continue
		}

		if possibleCandidate.x == end.x && possibleCandidate.y == end.y {
			minCost = possibleCandidate.cost
			for _, location := range possibleCandidate.locations {
				visited[location.y][location.x] = true
			}
			continue
		}

		if !isInGrid(grid, possibleCandidate) {
			continue
		}
		cachedCosts, exits := cache[calculateKey(possibleCandidate)]
		if exits && cachedCosts < possibleCandidate.cost {
			continue
		}

		cache[calculateKey(possibleCandidate)] = possibleCandidate.cost
		candidates = append(candidates, rotateAndMove(possibleCandidate, 1, 0))
		candidates = append(candidates, rotateAndMove(possibleCandidate, 0, 1))
		candidates = append(candidates, rotateAndMove(possibleCandidate, -1, 0))
		candidates = append(candidates, rotateAndMove(possibleCandidate, 0, -1))
		// Sort by cost descending
		sort.Slice(candidates, func(i, j int) bool {
			return candidates[i].cost+calculateDistance(candidates[i].x, candidates[i].y, end.x, end.y) < candidates[j].cost+calculateDistance(candidates[j].x, candidates[j].y, end.x, end.y)
		})
	}

	sum := 0
	for _, visitRow := range visited {
		for _, isVisited := range visitRow {
			if isVisited {
				sum++
			}
		}
	}

	return sum + 1
}
func parseInput(lines []string) ([][]rune, location, location) {
	var grid [][]rune
	var start, end location
	for row, line := range lines {
		var gridRow []rune
		for column, r := range []rune(line) {
			if r == 'S' {
				start = location{x: column, y: row}
			} else if r == 'E' {
				end = location{x: column, y: row}
			}
			gridRow = append(gridRow, r)
		}
		grid = append(grid, gridRow)
	}
	return grid, start, end
}

func main() {

	scanner := bufio.NewScanner(os.Stdin)
	var lines []string

	for scanner.Scan() {
		line := scanner.Text()
		lines = append(lines, line)
	}

	grid, start, end := parseInput(lines)

	fmt.Println("Part 1:", partOne(grid, start, end))
	fmt.Println("Part 2:", partTwo(grid, start, end))
}
