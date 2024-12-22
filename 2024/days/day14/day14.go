package main

import (
	"bufio"
	"fmt"
	"os"
	"regexp"
	"strconv"
)

type robot struct {
	x  int
	y  int
	dx int
	dy int
}

func calculateSafetyFactor(grid [][]int) int {
	safetyFactor := 1
	for i := 0; i < 2; i++ {
		for j := 0; j < 2; j++ {
			numOfRobots := 0
			for row := i * (len(grid)/2 + 1); row < (i+1)*len(grid)/2; row++ {
				for column := j * (len(grid[row]) + 1) / 2; column < (j+1)*len(grid[row])/2; column++ {
					numOfRobots += grid[row][column]
				}
			}
			safetyFactor *= numOfRobots
		}
	}
	return safetyFactor
}

func partOne(robots []robot) int {
	gridWidth := 101
	gridHeight := 103

	for second := 0; second < 100; second++ {
		for i, robot := range robots {
			robot.x += robot.dx
			robot.y += robot.dy
			if robot.x < 0 {
				robot.x += gridWidth
			}
			if robot.y < 0 {
				robot.y += gridHeight
			}
			robot.x %= gridWidth
			robot.y %= gridHeight
			robots[i] = robot
		}
	}

	var grid [][]int
	for row := 0; row < gridHeight; row++ {
		var gridRow []int
		for column := 0; column < gridWidth; column++ {
			gridRow = append(gridRow, 0)
		}
		grid = append(grid, gridRow)
	}

	for _, robot := range robots {
		grid[robot.y][robot.x] += 1
	}
	return calculateSafetyFactor(grid)
}

func printGrid(grid [][]int) {
	for row := 0; row < len(grid); row++ {
		for column := 0; column < len(grid[row]); column++ {
			if grid[row][column] > 0 {
				fmt.Print("x")
			} else {
				fmt.Print(" ")
			}
		}
		fmt.Println()
	}
}

func partTwo(robots []robot) int {
	gridWidth := 101
	gridHeight := 103

	for second := 1; second < 30000; second++ {
		for i, robot := range robots {
			robot.x += robot.dx
			robot.y += robot.dy
			if robot.x < 0 {
				robot.x += gridWidth
			}
			if robot.y < 0 {
				robot.y += gridHeight
			}
			robot.x %= gridWidth
			robot.y %= gridHeight
			robots[i] = robot
		}

		var grid [][]int
		for row := 0; row < gridHeight; row++ {
			var gridRow []int
			for column := 0; column < gridWidth; column++ {
				gridRow = append(gridRow, 0)
			}
			grid = append(grid, gridRow)
		}
		for _, robot := range robots {
			grid[robot.y][robot.x] += 1
		}

		// Parameters defined by trial and error
		quadrantSize := 65

		for row := 0; row < gridHeight-quadrantSize; row++ {
			for column := 0; column < gridWidth-quadrantSize; column++ {
				sum := 0
				for i := 0; i < quadrantSize; i++ {
					for j := 0; j < quadrantSize; j++ {
						sum += grid[row+i][column+j]
					}
				}
				if sum > 400 {
					printGrid(grid)
					return second
				}
			}
		}
	}
	return -1
}

func parseInput(lines []string) []robot {
	var robots []robot
	for _, line := range lines {
		pattern := "(-?\\d+)"
		regex, _ := regexp.Compile(pattern)
		matches := regex.FindAllStringSubmatch(line, -1)
		x, _ := strconv.Atoi(matches[0][0])
		y, _ := strconv.Atoi(matches[1][0])
		dx, _ := strconv.Atoi(matches[2][0])
		dy, _ := strconv.Atoi(matches[3][0])
		robots = append(robots, robot{x: x, y: y, dx: dx, dy: dy})
	}
	return robots
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
