package main

import (
	"bufio"
	"fmt"
	"math"
	"os"
	"strconv"
)

type location struct {
	x int
	y int
}

type candidate struct {
	loc  location
	path int
}

func isInGrid(grid [][]bool, x int, y int) bool {
	return x >= 0 && y >= 0 && y < len(grid) && x < len(grid[y])
}

func computeCacheKey(loc location) string {
	return strconv.Itoa(loc.x) + "," + strconv.Itoa(loc.y)
}

func getVisitedMap(grid [][]bool, start location, end location) map[string]int {
	visited := make(map[string]int)
	currentCandidate := candidate{loc: location{x: start.x, y: start.y}, path: 0}
	var candidates []candidate
	candidates = append(candidates, currentCandidate)
	finalPath := -1
	for true {
		currentCandidate, candidates = candidates[0], candidates[1:]
		if currentCandidate.loc.x == end.x && currentCandidate.loc.y == end.y {
			finalPath = currentCandidate.path
			visited[computeCacheKey(currentCandidate.loc)] = currentCandidate.path + 1
			break
		}

		cached, exists := visited[computeCacheKey(currentCandidate.loc)]
		if exists && cached < currentCandidate.path {
			continue
		}

		if isInGrid(grid, currentCandidate.loc.x+1, currentCandidate.loc.y) && grid[currentCandidate.loc.y][currentCandidate.loc.x+1] {
			candidates = append(candidates, candidate{loc: location{x: currentCandidate.loc.x + 1, y: currentCandidate.loc.y}, path: currentCandidate.path + 1})
		}
		if isInGrid(grid, currentCandidate.loc.x, currentCandidate.loc.y+1) && grid[currentCandidate.loc.y+1][currentCandidate.loc.x] {
			candidates = append(candidates, candidate{loc: location{x: currentCandidate.loc.x, y: currentCandidate.loc.y + 1}, path: currentCandidate.path + 1})
		}
		if isInGrid(grid, currentCandidate.loc.x-1, currentCandidate.loc.y) && grid[currentCandidate.loc.y][currentCandidate.loc.x-1] {
			candidates = append(candidates, candidate{loc: location{x: currentCandidate.loc.x - 1, y: currentCandidate.loc.y}, path: currentCandidate.path + 1})
		}
		if isInGrid(grid, currentCandidate.loc.x, currentCandidate.loc.y-1) && grid[currentCandidate.loc.y-1][currentCandidate.loc.x] {
			candidates = append(candidates, candidate{loc: location{x: currentCandidate.loc.x, y: currentCandidate.loc.y - 1}, path: currentCandidate.path + 1})
		}
		visited[computeCacheKey(currentCandidate.loc)] = currentCandidate.path + 1
	}

	for candidate := range visited {
		visited[candidate] = finalPath - visited[candidate] - 1
	}

	return visited
}

func isOnPath(grid [][]bool, x int, y int) bool {
	return isInGrid(grid, x, y) && grid[y][x]
}

func partOne(grid [][]bool, start location, end location) int {
	visited := getVisitedMap(grid, start, end)
	noCheatPathLength := visited[computeCacheKey(start)]
	currentCandidate := candidate{loc: location{x: start.x, y: start.y}, path: 0}
	var candidates []candidate
	candidates = append(candidates, currentCandidate)
	var cheats []int
	for true {
		currentCandidate, candidates = candidates[0], candidates[1:]

		if currentCandidate.loc.x == end.x && currentCandidate.loc.y == end.y {
			break
		}

		cached, exists := visited[computeCacheKey(currentCandidate.loc)]
		if exists && (noCheatPathLength-cached) < currentCandidate.path {
			continue
		}

		if isOnPath(grid, currentCandidate.loc.x+1, currentCandidate.loc.y) {
			candidates = append(candidates, candidate{loc: location{x: currentCandidate.loc.x + 1, y: currentCandidate.loc.y}, path: currentCandidate.path + 1})
		} else if isOnPath(grid, currentCandidate.loc.x+2, currentCandidate.loc.y) {
			cheats = append(cheats, visited[computeCacheKey(currentCandidate.loc)]-visited[computeCacheKey(location{x: currentCandidate.loc.x + 2, y: currentCandidate.loc.y})]-2)
		}

		if isOnPath(grid, currentCandidate.loc.x-1, currentCandidate.loc.y) {
			candidates = append(candidates, candidate{loc: location{x: currentCandidate.loc.x - 1, y: currentCandidate.loc.y}, path: currentCandidate.path + 1})
		} else if isOnPath(grid, currentCandidate.loc.x-2, currentCandidate.loc.y) {
			cheats = append(cheats, visited[computeCacheKey(currentCandidate.loc)]-visited[computeCacheKey(location{x: currentCandidate.loc.x - 2, y: currentCandidate.loc.y})]-2)
		}

		if isOnPath(grid, currentCandidate.loc.x, currentCandidate.loc.y+1) {
			candidates = append(candidates, candidate{loc: location{x: currentCandidate.loc.x, y: currentCandidate.loc.y + 1}, path: currentCandidate.path + 1})
		} else if isOnPath(grid, currentCandidate.loc.x, currentCandidate.loc.y+2) {
			cheats = append(cheats, visited[computeCacheKey(currentCandidate.loc)]-visited[computeCacheKey(location{x: currentCandidate.loc.x, y: currentCandidate.loc.y + 2})]-2)
		}

		if isOnPath(grid, currentCandidate.loc.x, currentCandidate.loc.y-1) {
			candidates = append(candidates, candidate{loc: location{x: currentCandidate.loc.x, y: currentCandidate.loc.y - 1}, path: currentCandidate.path + 1})
		} else if isOnPath(grid, currentCandidate.loc.x, currentCandidate.loc.y-2) {
			cheats = append(cheats, visited[computeCacheKey(currentCandidate.loc)]-visited[computeCacheKey(location{x: currentCandidate.loc.x, y: currentCandidate.loc.y - 2})]-2)
		}
	}
	sum := 0
	for _, cheat := range cheats {
		if cheat >= 100 {
			sum++
		}
	}
	return sum
}

func calculateCheatKey(x1 int, y1 int, x2 int, y2 int) string {
	return strconv.Itoa(x1) + "," + strconv.Itoa(y1) + "," + strconv.Itoa(x2) + "," + strconv.Itoa(y2)
}

var cheatCache = make(map[string]int)

func findCheats2(grid [][]bool, visited map[string]int, xStart int, yStart int, originalEstimate int) []int {
	var possibleCheats []int
	for i := -20; i <= 20; i++ {
		for j := -20; j <= 20; j++ {
			pathLength := int(math.Abs(float64(i))) + int(math.Abs(float64(j)))
			if pathLength <= 20 {
				_, exits := cheatCache[calculateCheatKey(xStart, yStart, xStart+i, yStart+j)]
				if exits {
					continue
				}
				if isOnPath(grid, xStart+i, yStart+j) {
					cheat := originalEstimate - visited[computeCacheKey(location{x: xStart + i, y: yStart + j})] - pathLength
					cheatCache[calculateCheatKey(xStart, yStart, xStart+i, yStart+j)] = 1
					if cheat > 0 {
						possibleCheats = append(possibleCheats, cheat)
					}
				}
			}

		}
	}
	return possibleCheats
}

func partTwo(grid [][]bool, start location, end location) int {
	visited := getVisitedMap(grid, start, end)

	noCheatPathLength := visited[computeCacheKey(start)]
	currentCandidate := candidate{loc: location{x: start.x, y: start.y}, path: 0}
	var candidates []candidate
	candidates = append(candidates, currentCandidate)
	var cheats []int

	for true {
		currentCandidate, candidates = candidates[0], candidates[1:]
		if currentCandidate.loc.x == end.x && currentCandidate.loc.y == end.y {
			break
		}

		cached, exists := visited[computeCacheKey(currentCandidate.loc)]
		if exists && (noCheatPathLength-cached) < currentCandidate.path {
			continue
		}

		if isOnPath(grid, currentCandidate.loc.x+1, currentCandidate.loc.y) {
			candidates = append(candidates, candidate{loc: location{x: currentCandidate.loc.x + 1, y: currentCandidate.loc.y}, path: currentCandidate.path + 1})
		}

		if isOnPath(grid, currentCandidate.loc.x-1, currentCandidate.loc.y) {
			candidates = append(candidates, candidate{loc: location{x: currentCandidate.loc.x - 1, y: currentCandidate.loc.y}, path: currentCandidate.path + 1})
		}

		if isOnPath(grid, currentCandidate.loc.x, currentCandidate.loc.y+1) {
			candidates = append(candidates, candidate{loc: location{x: currentCandidate.loc.x, y: currentCandidate.loc.y + 1}, path: currentCandidate.path + 1})
		}

		if isOnPath(grid, currentCandidate.loc.x, currentCandidate.loc.y-1) {
			candidates = append(candidates, candidate{loc: location{x: currentCandidate.loc.x, y: currentCandidate.loc.y - 1}, path: currentCandidate.path + 1})
		}

		computedCheats := findCheats2(grid, visited, currentCandidate.loc.x, currentCandidate.loc.y, visited[computeCacheKey(currentCandidate.loc)])
		for _, cheat := range computedCheats {
			cheats = append(cheats, cheat)
		}

	}
	sum := 0

	for _, cheat := range cheats {
		if cheat >= 100 {
			sum++
		}
	}
	return sum
}

func parseInput(lines []string) ([][]bool, location, location) {
	var start location
	var end location
	var grid [][]bool

	for row, line := range lines {
		var gridRow []bool
		for column, element := range []rune(line) {
			if element == '#' {
				gridRow = append(gridRow, false)
			} else {
				if element == 'S' {
					start = location{x: column, y: row}
				}
				if element == 'E' {
					end = location{x: column, y: row}
				}
				gridRow = append(gridRow, true)
			}
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
