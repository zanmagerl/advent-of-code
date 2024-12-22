package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
)

func canBeMatched(towelPatterns []string, design string) bool {
	if design == "" {
		return true
	}
	for _, towelPattern := range towelPatterns {
		if strings.HasPrefix(design, towelPattern) {
			if canBeMatched(towelPatterns, strings.TrimPrefix(strings.Clone(design), towelPattern)) {
				return true
			}
		}
	}
	return false
}

var cache = make(map[string]int)

func canBeMatched2(towelPatterns map[string]bool, design string) int {
	if design == "" {
		return 1
	}

	cached, exists := cache[design]
	if exists {
		return cached
	}

	sum := 0
	for i := 1; i <= len(design); i++ {
		prefix := string([]rune(design)[0:i])
		if towelPatterns[prefix] {
			sum += canBeMatched2(towelPatterns, strings.TrimPrefix(strings.Clone(design), prefix))
		}
	}

	cache[design] = sum
	return sum
}

func partOne(towelPatterns []string, designs []string) int {
	sum := 0
	for _, design := range designs {
		if canBeMatched(towelPatterns, design) {
			sum++
		}
	}
	return sum
}

func partTwo(towelPatterns []string, designs []string) int {
	sum := 0

	towelMap := make(map[string]bool)
	for _, towelPattern := range towelPatterns {
		towelMap[towelPattern] = true
	}
	for _, design := range designs {
		sum += canBeMatched2(towelMap, design)
	}

	return sum
}

func parseInput(lines []string) ([]string, []string) {
	towelPatterns := strings.Split(lines[0], ",")
	for i := range towelPatterns {
		towelPatterns[i] = strings.TrimSpace(towelPatterns[i])
	}
	var designs []string
	for _, line := range lines[2:] {
		designs = append(designs, line)
	}

	return towelPatterns, designs
}

func main() {

	scanner := bufio.NewScanner(os.Stdin)

	var lines []string
	for scanner.Scan() {
		line := scanner.Text()
		lines = append(lines, line)
	}

	towelPatterns, designs := parseInput(lines)

	fmt.Println("Part 1:", partOne(towelPatterns, designs))
	fmt.Println("Part 2:", partTwo(towelPatterns, designs))
}
