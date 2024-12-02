package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

func Abs(a int) int {
	if a < 0 {
		return -a
	}
	return a
}

func partOne(reports [][]int) int {
	safeReports := 0

	for _, report := range reports {
		if isSafeReport(report) {
			safeReports += 1
		}
	}

	return safeReports
}

func partTwo(reports [][]int) int {
	safeReports := 0

	for _, report := range reports {

		// Brute-force as input is small enough: remove one level from list and check if report is valid
		for count := 0; count < len(report); count += 1 {
			var newReport []int
			for in, el := range report {
				if in != count {
					newReport = append(newReport, el)
				}
			}
			if isSafeReport(newReport) {
				safeReports += 1
				break
			}
		}

	}

	return safeReports
}

func isSafeReport(level []int) bool {
	safeReport := true
	increasing := true
	for index, _ := range level {

		if index >= 1 {
			diff := level[index] - level[index-1]

			if index == 1 {
				if diff < 0 {
					increasing = false
				}
			}
			if increasing {
				if diff < 1 || diff > 3 {
					safeReport = false
				}
			} else {
				if diff > -1 || diff < -3 {
					safeReport = false
				}
			}
		}
	}
	return safeReport
}

func parseInput(lines []string) [][]int {
	var reports [][]int

	for _, line := range lines {
		var report []int
		var numbers = strings.Fields(line)
		for _, number := range numbers {
			num, _ := strconv.Atoi(number)
			report = append(report, num)
		}
		reports = append(reports, report)
	}
	return reports
}

func main() {

	scanner := bufio.NewScanner(os.Stdin)
	var lines []string

	for scanner.Scan() {
		line := scanner.Text()
		lines = append(lines, line)
	}

	reports := parseInput(lines)

	fmt.Println("Part 1:", partOne(reports))
	fmt.Println("Part 2:", partTwo(reports))
}
