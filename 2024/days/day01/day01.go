package main

import (
	"bufio"
	"fmt"
	"os"
	"sort"
	"strconv"
	"strings"
)

func Abs(a int) int {
	if a < 0 {
		return -a
	}
	return a
}

func partOne(firstList []int, secondList []int) int {
	sort.Slice(firstList, func(i, j int) bool {
		return firstList[i] < firstList[j]
	})
	sort.Slice(secondList, func(i, j int) bool {
		return secondList[i] < secondList[j]
	})

	wholeDistance := 0
	for index, _ := range firstList {
		wholeDistance += Abs(firstList[index] - secondList[index])
	}

	return wholeDistance
}

func partTwo(firstList []int, secondList []int) int {
	counts := make(map[int]int)
	for _, element := range secondList {
		counts[element] = counts[element] + 1
	}
	sum := 0
	for _, element := range firstList {
		sum += element * counts[element]
	}
	return sum
}

func parseInput(lines []string) ([]int, []int) {
	var firstList []int
	var secondList []int

	for _, line := range lines {
		var numbers = strings.Fields(line)
		num1, _ := strconv.Atoi(numbers[0])
		num2, _ := strconv.Atoi(numbers[1])
		firstList = append(firstList, num1)
		secondList = append(secondList, num2)
	}
	return firstList, secondList
}

func main() {

	scanner := bufio.NewScanner(os.Stdin)
	var lines []string

	for scanner.Scan() {
		line := scanner.Text()
		lines = append(lines, line)
	}

	firstList, secondList := parseInput(lines)

	fmt.Println("Part 1:", partOne(firstList, secondList))
	fmt.Println("Part 2:", partTwo(firstList, secondList))
}
