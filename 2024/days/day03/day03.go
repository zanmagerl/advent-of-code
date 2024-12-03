package main

import (
	"bufio"
	"fmt"
	"os"
	"regexp"
	"strconv"
)

func partOne(line string) int {
	pattern := `mul\((\d+),(\d+)\)`
	re := regexp.MustCompile(pattern)
	sum := 0

	matches := re.FindAllStringSubmatch(line, -1)
	for _, match := range matches {
		num1, _ := strconv.Atoi(match[1])
		num2, _ := strconv.Atoi(match[2])
		sum += num1 * num2
	}

	return sum
}

func partTwo(line string) int {

	enabledRegex := regexp.MustCompile(`do\(\)`)
	disabledRegex := regexp.MustCompile(`don't\(\)`)

	var enabledIndexes []int
	for _, match := range enabledRegex.FindAllStringSubmatchIndex(line, -1) {
		enabledIndexes = append(enabledIndexes, match[0])
	}
	var disabledIndexes []int
	for _, match := range disabledRegex.FindAllStringSubmatchIndex(line, -1) {
		disabledIndexes = append(disabledIndexes, match[0])
	}

	mulRegex := regexp.MustCompile(`mul\((\d+),(\d+)\)`)
	mulIndexes := mulRegex.FindAllStringSubmatchIndex(line, -1)
	mulMatches := mulRegex.FindAllStringSubmatch(line, -1)

	sum := 0
	for index, match := range mulIndexes {
		if isInstructionEnabled(match[0], enabledIndexes, disabledIndexes) {
			num1, _ := strconv.Atoi(mulMatches[index][1])
			num2, _ := strconv.Atoi(mulMatches[index][2])
			sum += num1 * num2
		}
	}

	return sum
}

func isInstructionEnabled(index int, enabled_indexes []int, disabled_indexes []int) bool {
	lastEnabled := -1
	lastDisabled := -1
	for _, in := range enabled_indexes {
		if in < index {
			lastEnabled = in
		} else {
			break
		}
	}
	for _, in := range disabled_indexes {
		if in < index {
			lastDisabled = in
		} else {
			break
		}
	}
	// It has to be >= instead of > for cases before first do()
	return lastEnabled >= lastDisabled
}

func main() {

	scanner := bufio.NewScanner(os.Stdin)
	var lines []string

	for scanner.Scan() {
		line := scanner.Text()
		lines = append(lines, line)
	}

	fmt.Println("Part 1:", partOne(lines[0]))
	fmt.Println("Part 2:", partTwo(lines[0]))
}
