package main

import (
	"bufio"
	"fmt"
	"os"
	"slices"
	"sort"
	"strconv"
	"strings"
)

func solve(pagesLess map[int][]int, updates [][]int) (int, int) {
	part1 := 0
	part2 := 0
	for _, update := range updates {
		correctOrder := true
		for i, _ := range update {
			for j := 0; j < i; j++ {
				if !slices.Contains(pagesLess[update[i]], update[j]) {
					correctOrder = false
					break
				}
			}
		}
		if correctOrder {
			part1 += update[len(update)/2]
		} else {
			sort.Slice(update, func(i, j int) bool {
				for _, num := range pagesLess[update[i]] {
					if update[j] == num {
						return true
					}
				}
				return false
			})
			part2 += update[len(update)/2]
		}
	}

	return part1, part2
}

func parseInput(lines []string) (map[int][]int, [][]int) {
	pagesLess := make(map[int][]int)
	var updates [][]int
	for _, line := range lines {
		if strings.Contains(line, "|") {
			matches := strings.Split(line, "|")
			left, _ := strconv.Atoi(matches[0])
			right, _ := strconv.Atoi(matches[1])

			l, exits := pagesLess[right]
			if !exits {
				l = make([]int, 0)
				pagesLess[right] = append(l, left)
			} else {
				pagesLess[right] = append(pagesLess[right], left)
			}
		}

		if strings.Contains(line, ",") {
			matches := strings.Split(line, ",")
			var update []int
			for _, match := range matches {
				num, _ := strconv.Atoi(match)
				update = append(update, num)
			}
			updates = append(updates, update)
		}
	}
	return pagesLess, updates
}

func main() {

	scanner := bufio.NewScanner(os.Stdin)

	var lines []string
	for scanner.Scan() {
		line := scanner.Text()
		lines = append(lines, line)
	}

	pagesLess, updates := parseInput(lines)
	part1, part2 := solve(pagesLess, updates)

	fmt.Println("Part 1:", part1)
	fmt.Println("Part 2:", part2)
}
