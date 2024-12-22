package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
)

func mix(a int, b int) int {
	return a ^ b
}

func prune(a int) int {
	return a % 16777216
}

func partOne(initialSecretNumbers []int) int {
	sum := 0
	for _, initialSecretNumber := range initialSecretNumbers {
		iterNumber := initialSecretNumber
		for i := 0; i < 2000; i++ {
			iterNumber = prune(mix(iterNumber*64, iterNumber))
			iterNumber = prune(mix(iterNumber/32, iterNumber))
			iterNumber = prune(mix(iterNumber*2048, iterNumber))
		}
		sum += iterNumber
	}

	return sum
}

func getBananas(a int) int {
	return a % 10
}

func computeKey(a int, b int, c int, d int) string {
	return strconv.Itoa(a) + "," + strconv.Itoa(b) + "," + strconv.Itoa(c) + "," + strconv.Itoa(d)
}

func partTwo(initialSecretNumbers []int) int {
	sum := 0
	sequences := make(map[string]int)
	for _, initialSecretNumber := range initialSecretNumbers {
		iterNumber := initialSecretNumber
		var lastChanges []int
		soldSequences := make(map[string]bool)
		for i := 0; i < 2000; i++ {
			previousNumber := iterNumber
			iterNumber = prune(mix(iterNumber*64, iterNumber))
			iterNumber = prune(mix(iterNumber/32, iterNumber))
			iterNumber = prune(mix(iterNumber*2048, iterNumber))
			lastChanges = append(lastChanges, getBananas(iterNumber)-getBananas(previousNumber))
			if len(lastChanges) >= 4 {
				key := computeKey(lastChanges[i-3], lastChanges[i-2], lastChanges[i-1], lastChanges[i])
				_, exists := soldSequences[key]
				if !exists {
					sequences[key] += getBananas(iterNumber)
					soldSequences[key] = true
				}
			}
		}
		sum += iterNumber
	}

	maxBananas := -1
	for k := range sequences {
		if sequences[k] > maxBananas {
			maxBananas = sequences[k]
		}
	}

	return maxBananas
}

func parseInput(lines []string) []int {
	var initialSecretNumbers []int
	for _, line := range lines {
		num, _ := strconv.Atoi(line)
		initialSecretNumbers = append(initialSecretNumbers, num)
	}
	return initialSecretNumbers
}

func main() {

	scanner := bufio.NewScanner(os.Stdin)
	var lines []string

	for scanner.Scan() {
		line := scanner.Text()
		lines = append(lines, line)
	}

	initialSecretNumbers := parseInput(lines)

	fmt.Println("Part 1:", partOne(initialSecretNumbers))
	fmt.Println("Part 2:", partTwo(initialSecretNumbers))
}
