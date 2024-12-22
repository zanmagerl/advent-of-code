package main

import (
	"bufio"
	"fmt"
	"os"
	"sort"
)

func mapInstruction(instruction rune) (int, int) {
	if instruction == '<' {
		return -1, 0
	}
	if instruction == 'v' {
		return 0, 1
	}
	if instruction == '>' {
		return 1, 0
	}
	if instruction == '^' {
		return 0, -1
	}
	panic("Unknown instruction")
}

func partOne(grid [][]rune, instructions []rune, x int, y int) int {
	for _, instruction := range instructions {
		dx, dy := mapInstruction(instruction)
		if grid[y+dy][x+dx] == '.' {
			x += dx
			y += dy
		} else if grid[y+dy][x+dx] == '#' {
			continue
		} else if grid[y+dy][x+dx] == 'O' {
			// Let's try to push crates around
			numberOfPushes := -1

			for i := 1; grid[y+i*dy][x+i*dx] != '#'; i++ {
				if grid[y+i*dy][x+i*dx] == '.' {
					numberOfPushes = i
					break
				}
			}
			if numberOfPushes > -1 {
				grid[y+dy][x+dx] = '.'
				grid[y+numberOfPushes*dy][x+numberOfPushes*dx] = 'O'
				x += dx
				y += dy
			}
		}
	}
	sum := 0
	for i, row := range grid {
		for j, c := range row {
			if c == 'O' {
				sum += i*100 + j
			}
		}
	}

	return sum
}

type box struct {
	x int
	y int
}

func affectedBoxes(grid [][]rune, x int, y int, dx int, dy int) (bool, []box) {
	var b box
	if grid[y+dy][x+dx] == '[' {
		b = box{x: x + dx, y: y + dy}
		available := true
		available1, boxes1 := affectedBoxes(grid, x+dx, y+dy, dx, dy)
		var result []box
		if dx != -1 {
			available2, boxes2 := affectedBoxes(grid, x+1+dx, y+dy, dx, dy)
			for _, bo := range boxes2 {
				result = append(result, bo)
			}
			available = available && available2
		}
		for _, bo := range boxes1 {
			result = append(result, bo)
		}

		result = append(result, b)
		return (available1 && available), result
	} else if grid[y+dy][x+dx] == ']' {
		b = box{x: x - 1 + dx, y: y + dy}
		var result []box
		available := true
		if dx != 1 {
			available1, boxes1 := affectedBoxes(grid, x-1+dx, y+dy, dx, dy)
			for _, bo := range boxes1 {
				result = append(result, bo)
			}
			available = available && available1
		}
		available2, boxes2 := affectedBoxes(grid, x+dx, y+dy, dx, dy)

		for _, bo := range boxes2 {
			result = append(result, bo)
		}
		result = append(result, b)
		return (available && available2), result
	} else if grid[y+dy][x+dx] == '#' {
		return false, []box{}
	} else if grid[y+dy][x+dx] == '.' {
		return true, []box{}
	}
	panic("Invalid grid")
}

func partTwo(grid [][]rune, instructions []rune, x int, y int) int {
	for _, instruction := range instructions {
		dx, dy := mapInstruction(instruction)
		if grid[y+dy][x+dx] == '.' {
			x += dx
			y += dy
		} else if grid[y+dy][x+dx] == '#' {
			continue
		} else if grid[y+dy][x+dx] == '[' || grid[y+dy][x+dx] == ']' {
			// Let's try to push crates around
			canBePushed, affectedBoxes := affectedBoxes(grid, x, y, dx, dy)
			if canBePushed {
				// We need to sort boxes so that they do not get applied in wrong order
				sort.Slice(affectedBoxes, func(i, j int) bool {
					if dx == 1 {
						return affectedBoxes[i].x >= affectedBoxes[j].x
					} else if dx == -1 {
						return affectedBoxes[i].x <= affectedBoxes[j].x
					}
					if dy == 1 {
						return affectedBoxes[i].y >= affectedBoxes[j].y
					} else if dy == -1 {
						return affectedBoxes[i].y <= affectedBoxes[j].y
					}
					return true
				})
				for _, box := range affectedBoxes {
					grid[box.y][box.x] = '.'
					grid[box.y][box.x+1] = '.'
					grid[box.y+dy][box.x+dx] = '['
					grid[box.y+dy][box.x+1+dx] = ']'
				}
				x += dx
				y += dy
			}
		}
	}
	sum := 0
	for i, row := range grid {
		for j, c := range row {
			if c == '[' {
				sum += i*100 + j
			}
		}
	}

	return sum
}

func parseInput(lines []string) ([][]rune, []rune, int, int) {
	isParsingGrid := true
	var grid [][]rune
	var instructions []rune
	var x, y int
	for i, line := range lines {
		if len(line) == 0 {
			isParsingGrid = false
			continue
		}
		if isParsingGrid {
			var gridRow []rune
			for j, r := range []rune(line) {
				if r == '@' {
					x, y = j, i
					r = '.'
				}
				gridRow = append(gridRow, r)
			}
			grid = append(grid, gridRow)
		} else {
			for _, r := range []rune(line) {
				instructions = append(instructions, r)
			}
		}
	}
	return grid, instructions, x, y
}

func parseInput2(lines []string) ([][]rune, []rune, int, int) {
	isParsingGrid := true
	var grid [][]rune
	var instructions []rune
	var x, y int
	for i, line := range lines {
		if len(line) == 0 {
			isParsingGrid = false
			continue
		}
		if isParsingGrid {
			var gridRow []rune
			for j, r := range []rune(line) {
				if r == '@' {
					x, y = 2*j, i
					gridRow = append(gridRow, '.')
					gridRow = append(gridRow, '.')
				} else if r == '#' || r == '.' {
					gridRow = append(gridRow, r)
					gridRow = append(gridRow, r)
				} else if r == 'O' {
					gridRow = append(gridRow, '[')
					gridRow = append(gridRow, ']')
				}
			}
			grid = append(grid, gridRow)
		} else {
			for _, r := range []rune(line) {
				instructions = append(instructions, r)
			}
		}
	}
	return grid, instructions, x, y
}

func main() {

	scanner := bufio.NewScanner(os.Stdin)
	var lines []string

	for scanner.Scan() {
		line := scanner.Text()
		lines = append(lines, line)
	}
	fmt.Println("Part 1:", partOne(parseInput(lines)))
	fmt.Println("Part 2:", partTwo(parseInput2(lines)))
}
