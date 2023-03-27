package main

import (
	"bufio"
	"fmt"
	"magerl.si/advent-of-code/2015/utils"
	"math"
	"os"
	"strconv"
	"strings"
)

type Present struct {
	length int
	width  int
	height int
}

func computeWrappingPaperAmount(p Present) int {
	return 2*p.length*p.height + 2*p.length*p.width + 2*p.width*p.height + int(math.Min(float64(p.length*p.height), math.Min(float64(p.length*p.width), float64(p.width*p.height))))
}

func computeRibbonAmount(p Present) int {
	smallestPerimeter := int(math.Min(float64(2*(p.length+p.height)), math.Min(float64(2*(p.length+p.width)), float64(2*(p.width+p.height)))))
	volume := p.length * p.width * p.height
	return smallestPerimeter + volume
}

func partOne(presents []Present) int {
	return utils.Sum(utils.Transform(presents, computeWrappingPaperAmount))
}

func partTwo(presents []Present) int {
	return utils.Sum(utils.Transform(presents, computeRibbonAmount))
}

func convertToPresent(line string) Present {
	splitLine := utils.Transform(strings.Split(line, "x"), func(size string) int {
		value, _ := strconv.Atoi(size)
		return value
	})
	return Present{
		length: splitLine[0],
		width:  splitLine[1],
		height: splitLine[2],
	}
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)

	var presents []Present

	for scanner.Scan() {
		line := scanner.Text()
		presents = append(presents, convertToPresent(line))
	}
	fmt.Println("Part 1:", partOne(presents))
	fmt.Println("Part 2:", partTwo(presents))
}
