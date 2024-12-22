package main

import (
	"bufio"
	"fmt"
	"math"
	"os"
	"strconv"
	"strings"
)

func partOne(stones []int) int {
	numOfBlinks := 25
	iterStones := stones
	for iteration := 0; iteration < numOfBlinks; iteration++ {
		var newStones []int
		for _, stone := range iterStones {
			if stone == 0 {
				newStones = append(newStones, 1)
			} else if (int(math.Log10(float64(stone)))+1)%2 == 0 {
				stringifiedStone := strconv.Itoa(stone)
				numOfDigits := int(math.Log10(float64(stone))) + 1
				num1, _ := strconv.Atoi(stringifiedStone[0 : numOfDigits/2])
				num2, _ := strconv.Atoi(stringifiedStone[numOfDigits/2 : numOfDigits])
				newStones = append(newStones, num1, num2)
			} else {
				newStones = append(newStones, stone*2024)
			}
		}
		iterStones = newStones
	}
	return len(iterStones)
}

func calculateKey(stone int, iterationsLeft int) string {
	return strconv.Itoa(stone) + "," + strconv.Itoa(iterationsLeft)
}

var cache = make(map[string]int)

func calculateStone(stone int, depth int) int {
	finalDepth := 75
	if depth == finalDepth {
		return 1
	}
	res, exists := cache[calculateKey(stone, finalDepth-depth)]
	if exists {
		return res
	} else {
		var newStones []int
		if stone == 0 {
			newStones = append(newStones, 1)
		} else if (int(math.Log10(float64(stone)))+1)%2 == 0 {
			stringifiedStone := strconv.Itoa(stone)
			numOfDigits := int(math.Log10(float64(stone))) + 1
			num1, _ := strconv.Atoi(stringifiedStone[0 : numOfDigits/2])
			num2, _ := strconv.Atoi(stringifiedStone[numOfDigits/2 : numOfDigits])
			newStones = append(newStones, num1, num2)
		} else {
			newStones = append(newStones, stone*2024)
		}
		sum := 0
		for _, newStone := range newStones {
			sum += calculateStone(newStone, depth+1)
		}
		cache[calculateKey(stone, finalDepth-depth)] = sum
		return sum
	}
}

func partTwo(stones []int) int {
	sum := 0
	for _, stone := range stones {
		sum += (calculateStone(stone, 0))
	}
	return sum
}

func main() {

	scanner := bufio.NewScanner(os.Stdin)
	var lines []string

	for scanner.Scan() {
		line := scanner.Text()
		lines = append(lines, line)
	}

	splitString := strings.Fields(lines[0])
	var stones []int
	for _, split := range splitString {
		stone, _ := strconv.Atoi(split)
		stones = append(stones, stone)
	}

	fmt.Println("Part 1:", partOne(stones))
	fmt.Println("Part 2:", partTwo(stones))
}
