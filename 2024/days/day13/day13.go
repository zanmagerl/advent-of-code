package main

import (
	"bufio"
	"fmt"
	"os"
	"regexp"
	"strconv"
)

type button struct {
	x int
	y int
}

type clawMachine struct {
	buttonA button
	buttonB button
	resultX int
	resultY int
}

func partOne(clawMachines []clawMachine) int {
	sum := 0
	for _, clawMachine := range clawMachines {
		minCost := 401
		for numA := 0; numA <= 100; numA++ {
			for numB := 0; numB <= 100; numB++ {
				if numA*clawMachine.buttonA.x+numB*clawMachine.buttonB.x == clawMachine.resultX && numA*clawMachine.buttonA.y+numB*clawMachine.buttonB.y == clawMachine.resultY {
					cost := numA*3 + numB
					if cost < minCost {
						minCost = cost
					}
				}
			}
		}
		if minCost < 401 {
			sum += minCost
		}
	}
	return sum
}

func partTwo(clawMachines []clawMachine) int {
	sum := 0
	for _, clawMachine := range clawMachines {
		adjustedX := 10000000000000 + clawMachine.resultX
		adjustedY := 10000000000000 + clawMachine.resultY
		bUp := (adjustedY*clawMachine.buttonA.x - clawMachine.buttonA.y*adjustedX)
		bDown := -clawMachine.buttonB.x*clawMachine.buttonA.y + clawMachine.buttonA.x*clawMachine.buttonB.y
		if bUp%bDown == 0 {
			b := bUp / bDown
			aUp := adjustedX - clawMachine.buttonB.x*b
			aDown := clawMachine.buttonA.x
			if aUp%aDown == 0 {
				a := aUp / aDown
				sum += 3*a + b
			}
		}
	}
	return sum
}

func parseInput(lines []string) []clawMachine {
	var clawMachines []clawMachine
	for i := 0; i < len(lines); i += 4 {
		pattern := "(\\d+)"
		regex, _ := regexp.Compile(pattern)
		matchesA := regex.FindAllStringSubmatch(lines[i], -1)
		xa, _ := strconv.Atoi(matchesA[0][0])
		ya, _ := strconv.Atoi(matchesA[1][0])
		buttonA := button{x: xa, y: ya}

		matchesB := regex.FindAllStringSubmatch(lines[i+1], -1)
		xb, _ := strconv.Atoi(matchesB[0][0])
		yb, _ := strconv.Atoi(matchesB[1][0])
		buttonB := button{x: xb, y: yb}

		matchesR := regex.FindAllStringSubmatch(lines[i+2], -1)
		xr, _ := strconv.Atoi(matchesR[0][0])
		yr, _ := strconv.Atoi(matchesR[1][0])

		clawMachines = append(clawMachines, clawMachine{buttonA: buttonA, buttonB: buttonB, resultX: xr, resultY: yr})
	}
	return clawMachines
}

func main() {

	scanner := bufio.NewScanner(os.Stdin)
	var lines []string

	for scanner.Scan() {
		line := scanner.Text()
		lines = append(lines, line)
	}

	clawMachines := parseInput(lines)

	fmt.Println("Part 1:", partOne(clawMachines))
	fmt.Println("Part 2:", partTwo(clawMachines))
}
