package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
)

var vowels = []rune{'a', 'e', 'i', 'o', 'u'}
var forbiddenStrings = []string{"ab", "cd", "pq", "xy"}

func countVowels(candidate string) int {
	letters := make(map[rune]int)
	for _, letter := range candidate {
		letters[letter] += 1
	}
	numberOfVowels := 0
	for _, vowel := range vowels {
		numberOfVowels += letters[vowel]
	}

	return numberOfVowels
}

func countDoubledLetters(candidate string) int {
	doubles := 0
	for i := range candidate {
		if i == 0 {
			continue
		} else if candidate[i] == candidate[i-1] {
			doubles++
		}
	}
	return doubles
}

func countForbiddenStrings(candidate string) int {
	numberOfForbiddenString := 0
	for _, forbiddenString := range forbiddenStrings {
		numberOfForbiddenString += strings.Count(candidate, forbiddenString)
	}
	return numberOfForbiddenString
}

func findDoubledDouble(candidate string) bool {
	doubles := make(map[string]int)
	for i := range candidate {
		if i == 1 {
			doubles[candidate[i-1:i+1]] = i
		} else if i >= 2 {
			val, exists := doubles[candidate[i-1:i+1]]
			if exists {
				// Is at least one letter between them aaxaa
				if val < i-1 {
					return true
				}
			} else {
				doubles[candidate[i-1:i+1]] = i
			}
		}
	}
	return false
}

func countNumberOfThreesomes(candidate string) int {
	numberOfThreesomes := 0
	for i := range candidate {
		if i >= 2 {
			if candidate[i] == candidate[i-2] {
				numberOfThreesomes++
			}
		}
	}
	return numberOfThreesomes
}

func isNiceStringV1(candidate string) bool {
	return countVowels(candidate) >= 3 && countDoubledLetters(candidate) >= 1 && countForbiddenStrings(candidate) == 0
}

func isNiceStringV2(candidate string) bool {
	return findDoubledDouble(candidate) && countNumberOfThreesomes(candidate) >= 1
}

func partOne(strings []string) int {
	numberOfNiceStrings := 0
	for _, candidate := range strings {
		if isNiceStringV1(candidate) {
			numberOfNiceStrings++
		}
	}
	return numberOfNiceStrings
}

func partTwo(strings []string) int {
	numberOfNiceStrings := 0
	for _, candidate := range strings {
		if isNiceStringV2(candidate) {
			numberOfNiceStrings++
		}
	}
	return numberOfNiceStrings
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)

	var lines []string

	for scanner.Scan() {
		lines = append(lines, scanner.Text())
	}

	fmt.Println("Part 1:", partOne(lines))
	fmt.Println("Part 2:", partTwo(lines))
}
