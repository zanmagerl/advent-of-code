package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
	"unicode"
)

func partOne(lines []string) int {
	calibrationValue := 0
	for _, line := range lines {
		chars := []rune(line)
		var numbers []int
		for _, char := range chars {
			if unicode.IsDigit(char) {
				numbers = append(numbers, int(char-'0'))
			}
		}
		calibrationValue += numbers[0]*10 + numbers[len(numbers)-1]
	}
	return calibrationValue
}

func partTwo(lines []string) int {
	var mappings = map[string]int{
		"one":   1,
		"two":   2,
		"three": 3,
		"four":  4,
		"five":  5,
		"six":   6,
		"seven": 7,
		"eight": 8,
		"nine":  9,
	}
	calibrationValue := 0
	for _, line := range lines {
		chars := []rune(line)
		var numbers []int
		currentSubstring := ""
		for _, char := range chars {
			if unicode.IsDigit(char) {
				numbers = append(numbers, int(char-'0'))
				currentSubstring = ""
				continue
			}

			// Some weird symbols get inserted in the middle of the lines
			if char >= 97 && char <= 130 {
				currentSubstring += string(char)
			} else {
				continue
			}

			for key, value := range mappings {
				if strings.Contains(currentSubstring, key) {
					numbers = append(numbers, value)
					currentSubstring = currentSubstring[len(currentSubstring)-1:]
				}
			}

		}
		calibrationValue += numbers[0]*10 + numbers[len(numbers)-1]
	}
	return calibrationValue
}

func main() {

	scanner := bufio.NewScanner(os.Stdin)
	var lines []string

	for scanner.Scan() {
		line := scanner.Text()
		lines = append(lines, line)
	}

	fmt.Println("Part 1:", partOne(lines))
	fmt.Println("Part 2:", partTwo(lines))
}
