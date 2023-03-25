package main

import (
	"bufio"
	"fmt"
	"magerl.si/advent-of-code/2015/utils"
	"os"
)

func convertParenthesis(char rune) int {
	if char == '(' {
		return 1
	} else if char == ')' {
		return -1
	}
	return 0
}

func partOne(input string) int {
	var convertedValues = utils.Transform([]rune(input), convertParenthesis)
	return utils.Sum(convertedValues)
}

func partTwo(input string) int {
	var convertedValues = utils.Transform([]rune(input), convertParenthesis)
	var sum = 0
	for index, element := range convertedValues {
		sum += element
		if sum == -1 {
			return index + 1
		}
	}
	return -1
}

func main() {

	scanner := bufio.NewScanner(os.Stdin)
	scanner.Scan()
	var input = scanner.Text()

	fmt.Println("Part 1:", partOne(input))
	fmt.Println("Part 2:", partTwo(input))
}
