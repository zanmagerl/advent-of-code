package main

import (
	"bufio"
	"fmt"
	"os"
	"slices"
	"strconv"
)

type plot struct {
	id rune
	x  int
	y  int
}

func isInGarden(garden [][]rune, x int, y int) bool {
	return x >= 0 && y >= 0 && y < len(garden) && x < len(garden[y])
}

func findRegion(garden [][]rune, id rune, x int, y int) []plot {
	var queue []plot
	queue = append(queue, plot{id: id, x: x, y: y})
	visited := make(map[string]bool)
	var regions []plot
	var candidate plot
	for len(queue) > 0 {
		candidate, queue = queue[0], queue[1:]
		if !isInGarden(garden, candidate.x, candidate.y) || garden[candidate.y][candidate.x] != id {
			continue
		}
		if visited[strconv.Itoa(candidate.y)+","+strconv.Itoa(candidate.x)] {
			continue
		}
		regions = append(regions, plot{id: garden[candidate.y][candidate.x], x: candidate.x, y: candidate.y})
		queue = append(queue, plot{x: candidate.x, y: candidate.y - 1})
		queue = append(queue, plot{x: candidate.x + 1, y: candidate.y})
		queue = append(queue, plot{x: candidate.x, y: candidate.y + 1})
		queue = append(queue, plot{x: candidate.x - 1, y: candidate.y})
		visited[strconv.Itoa(candidate.y)+","+strconv.Itoa(candidate.x)] = true
	}

	return regions
}

func existsInRegion(regions [][]plot, x int, y int) bool {
	for _, region := range regions {
		for _, plot := range region {
			if plot.x == x && plot.y == y {
				return true
			}
		}
	}
	return false
}

func doesNeedFence(id rune, x int, y int, garden [][]rune) int {
	if !isInGarden(garden, x, y) {
		return 1
	}
	if garden[y][x] == id {
		return 0
	}
	return 1
}

func calculateRegionPerimeter(region []plot, garden [][]rune) int {
	perimeter := 0
	for _, plot := range region {
		perimeter += doesNeedFence(plot.id, plot.x, plot.y-1, garden)
		perimeter += doesNeedFence(plot.id, plot.x+1, plot.y, garden)
		perimeter += doesNeedFence(plot.id, plot.x, plot.y+1, garden)
		perimeter += doesNeedFence(plot.id, plot.x-1, plot.y, garden)
	}
	return perimeter
}

func countNumberOfComponents(components map[int][]int) int {
	numOfComponents := 0
	for k := range components {
		slices.Sort(components[k])
		for i := 0; i < len(components[k]); i++ {
			if i == 0 {
				numOfComponents++
			} else if components[k][i]-components[k][i-1] > 1 {
				numOfComponents++
			}
		}
	}
	return numOfComponents
}

func calculateRegionPerimeterPart2(region []plot, garden [][]rune) int {
	up := make(map[int][]int)
	down := make(map[int][]int)
	left := make(map[int][]int)
	right := make(map[int][]int)
	for _, plot := range region {
		if doesNeedFence(plot.id, plot.x, plot.y-1, garden) == 1 {
			xs, exist := up[plot.y]
			if !exist {
				xs = []int{}
			}
			up[plot.y] = append(xs, plot.x)
		}
		if doesNeedFence(plot.id, plot.x+1, plot.y, garden) == 1 {
			ys, exist := right[plot.x]
			if !exist {
				ys = []int{}
			}
			right[plot.x] = append(ys, plot.y)
		}
		if doesNeedFence(plot.id, plot.x, plot.y+1, garden) == 1 {
			xs, exist := down[plot.y]
			if !exist {
				xs = []int{}
			}
			down[plot.y] = append(xs, plot.x)
		}
		if doesNeedFence(plot.id, plot.x-1, plot.y, garden) == 1 {
			ys, exist := left[plot.x]
			if !exist {
				ys = []int{}
			}
			left[plot.x] = append(ys, plot.y)
		}
	}

	return countNumberOfComponents(up) + countNumberOfComponents(down) + countNumberOfComponents(left) + countNumberOfComponents(right)
}

func partOne(garden [][]rune) int {
	var regions [][]plot
	for row, gardenRow := range garden {
		for column, _ := range gardenRow {
			if !existsInRegion(regions, column, row) {
				regions = append(regions, findRegion(garden, garden[row][column], column, row))
			}
		}
	}
	sum := 0
	for _, region := range regions {
		regionArea := len(region)
		regionPerimeter := calculateRegionPerimeter(region, garden)
		sum += regionArea * regionPerimeter
	}
	return sum
}

func partTwo(garden [][]rune) int {
	var regions [][]plot
	for row, gardenRow := range garden {
		for column, _ := range gardenRow {
			if !existsInRegion(regions, column, row) {
				regions = append(regions, findRegion(garden, garden[row][column], column, row))
			}
		}
	}
	sum := 0
	for _, region := range regions {
		regionArea := len(region)
		regionPerimeter := calculateRegionPerimeterPart2(region, garden)
		sum += regionArea * regionPerimeter
	}
	return sum
}

func parseInput(lines []string) [][]rune {
	var runes [][]rune
	for _, line := range lines {
		runes = append(runes, []rune(line))
	}
	return runes
}

func main() {

	scanner := bufio.NewScanner(os.Stdin)
	var lines []string

	for scanner.Scan() {
		line := scanner.Text()
		lines = append(lines, line)
	}

	garden := parseInput(lines)

	fmt.Println("Part 1:", partOne(garden))
	fmt.Println("Part 2:", partTwo(garden))
}
