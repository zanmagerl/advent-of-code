package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
)

/*
*
-1 0 ->  0 1

	0 1 -> 1 0

	1 0 ->  0 -1

	0 -1 -> -1 0
*/
func partOne(grid [][]bool, x int, y int) int {
	var visited [][]bool
	for i := 0; i < len(grid); i++ {
		var ro []bool
		for j := 0; j < len(grid[i]); j++ {
			ro = append(ro, false)
		}
		visited = append(visited, ro)
	}
	row, column := y, x
	dRow, dColumn := -1, 0
	for row+dRow >= 0 && column+dColumn >= 0 && row+dRow < len(grid) && column+dColumn < len(grid[0]) {
		visited[row][column] = true

		if grid[row+dRow][column+dColumn] {
			// 0 1
			if dRow == -1 && dColumn == 0 {
				dColumn = 1
				dRow = 0
			} else if dColumn == 1 && dRow == 0 {
				dRow = 1
				dColumn = 0
			} else if dRow == 1 && dColumn == 0 {
				dRow = 0
				dColumn = -1
			} else if dRow == 0 && dColumn == -1 {
				dRow = -1
				dColumn = 0
			} else {
				panic("Wrong turn!")
			}
		}

		row += dRow
		column += dColumn
	}
	// We did not go inside last iteration due to stop condition
	visited[row][column] = true
	sum := 0
	for i := 0; i < len(visited); i++ {
		for j := 0; j < len(visited[i]); j++ {
			if visited[i][j] {
				sum++
			}
		}
	}
	// + 1 as we do not go to the last turn
	return sum
}

func executeSimulation(grid [][]bool, x int, y int) int {

	var visited [][]int
	for i := 0; i < len(grid); i++ {
		var ro []int
		for j := 0; j < len(grid[i]); j++ {
			ro = append(ro, -1000)
		}
		visited = append(visited, ro)
	}

	row, column := y, x
	dRow, dColumn := -1, 0
	for row+dRow >= 0 && column+dColumn >= 0 && row+dRow < len(grid) && column+dColumn < len(grid[0]) {
		// Tricky - when you add new obstacles you can get corner
		for grid[row+dRow][column+dColumn] {
			// 0 1
			if dRow == -1 && dColumn == 0 {
				dColumn = 1
				dRow = 0
			} else if dColumn == 1 && dRow == 0 {
				dRow = 1
				dColumn = 0
			} else if dRow == 1 && dColumn == 0 {
				dRow = 0
				dColumn = -1
			} else if dRow == 0 && dColumn == -1 {
				dRow = -1
				dColumn = 0
			} else {
				panic("Wrong turn!")
			}
		}

		identifier := dRow*10 + dColumn
		if visited[row][column] == identifier {
			return 1
		}
		visited[row][column] = identifier

		row += dRow
		column += dColumn
	}
	return 0
}

func partTwo(grid [][]bool, x int, y int) int {
	sum := 0
	for i := 0; i < len(grid); i++ {
		for j := 0; j < len(grid[i]); j++ {
			if i == y && j == x {
				continue
			}
			if !grid[i][j] {
				grid[i][j] = true
				sum += executeSimulation(grid, x, y)
				grid[i][j] = false
			}
		}
	}
	return sum
}

func parseInput(lines []string) ([][]bool, int, int) {
	obstacles := [][]bool{}
	obstaclesRow := make(map[int][]int)
	obstaclesColumn := make(map[int][]int)
	var x int
	var y int
	for row, line := range lines {
		splitLine := strings.Split(line, "")
		obst := []bool{}
		for column, char := range splitLine {
			if char == "#" {
				obst = append(obst, true)
				list, exists := obstaclesColumn[column]
				if !exists {
					l := make([]int, 0)
					obstaclesColumn[column] = append(l, row)
				} else {
					obstaclesColumn[column] = append(list, row)
				}
				list2, exists := obstaclesRow[row]
				if !exists {
					l := make([]int, 0)
					obstaclesRow[row] = append(l, column)
				} else {
					obstaclesRow[row] = append(list2, column)
				}
			} else if char == "^" {
				obst = append(obst, false)
				x = column
				y = row
			} else {
				obst = append(obst, false)
			}
		}
		obstacles = append(obstacles, obst)
	}
	return obstacles, x, y
}

func main() {

	scanner := bufio.NewScanner(os.Stdin)
	var lines []string

	for scanner.Scan() {
		line := scanner.Text()
		lines = append(lines, line)
	}
	grid, x, y := parseInput(lines)
	fmt.Println("Part 1:", partOne(grid, x, y))
	fmt.Println("Part 2:", partTwo(grid, x, y))
}
