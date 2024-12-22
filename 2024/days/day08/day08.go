package main

import (
	"bufio"
	"fmt"
	"os"
)

type location struct {
	x int
	y int
}

func isInGrid(y int, x int, grid [][]bool) bool {
	return y >= 0 && x >= 0 && y < len(grid) && x < len(grid[y])
}

func partOne(antinodes [][]bool, antennas map[rune][]location) int {
	for key := range antennas {
		for i := 0; i < len(antennas[key]); i++ {
			for j := i + 1; j < len(antennas[key]); j++ {
				antenna1 := antennas[key][i]
				antenna2 := antennas[key][j]
				dx := antenna1.x - antenna2.x
				dy := antenna1.y - antenna2.y
				if isInGrid(antenna1.y+dy, antenna1.x+dx, antinodes) {
					antinodes[antenna1.y+dy][antenna1.x+dx] = true
				}
				if isInGrid(antenna2.y-dy, antenna2.x-dx, antinodes) {
					antinodes[antenna2.y-dy][antenna2.x-dx] = true
				}
			}
		}
	}
	sum := 0
	for _, row := range antinodes {
		for _, antiantinode := range row {
			if antiantinode {
				sum++
			}
		}
	}
	return sum
}

func partTwo(antinodes [][]bool, antennas map[rune][]location) int {
	for key := range antennas {
		for i := 0; i < len(antennas[key]); i++ {
			for j := i + 1; j < len(antennas[key]); j++ {
				antenna1 := antennas[key][i]
				antenna2 := antennas[key][j]
				dx := antenna1.x - antenna2.x
				dy := antenna1.y - antenna2.y
				for n := 0; isInGrid(antenna1.y+n*dy, antenna1.x+n*dx, antinodes); n++ {
					antinodes[antenna1.y+n*dy][antenna1.x+n*dx] = true
				}
				for n := 0; isInGrid(antenna2.y-n*dy, antenna2.x-n*dx, antinodes); n++ {
					antinodes[antenna2.y-n*dy][antenna2.x-n*dx] = true
				}
			}
		}
	}
	sum := 0
	for _, row := range antinodes {
		for _, antiantinode := range row {
			if antiantinode {
				sum++
			}
		}
	}
	return sum
}

func parseInput(lines []string) (map[rune][]location, [][]bool) {
	antennas := make(map[rune][]location)
	var grid [][]bool
	for y, line := range lines {
		var gridRow []bool
		runes := []rune(line)
		for x, r := range runes {
			gridRow = append(gridRow, false)
			if (r >= 'A' && r < 'Z') || (r >= 'a' && r <= 'z') || (r >= '0' && r <= '9') {
				currentAntennas, exists := antennas[r]
				if !exists {
					currentAntennas = []location{}
				}
				antennas[r] = append(currentAntennas, location{x: x, y: y})
			}
		}
		grid = append(grid, gridRow)
	}
	return antennas, grid
}

func main() {

	scanner := bufio.NewScanner(os.Stdin)
	var lines []string

	for scanner.Scan() {
		line := scanner.Text()
		lines = append(lines, line)
	}

	antennas, grid := parseInput(lines)

	fmt.Println("Part 1:", partOne(grid, antennas))
	fmt.Println("Part 2:", partTwo(grid, antennas))
}
