package main

import (
	"bufio"
	"fmt"
	"os"
)

type location struct {
	x int
	y int
}

var numericKeypad = map[rune]location{
	'7': location{0, 0},
	'8': location{1, 0},
	'9': location{2, 0},
	'4': location{0, 1},
	'5': location{1, 1},
	'6': location{2, 1},
	'1': location{0, 2},
	'2': location{1, 2},
	'3': location{2, 2},
	'0': location{1, 3},
	'A': location{2, 3},
}

var directionalKeypad = map[rune]location{
	'^': location{1, 0},
	'A': location{2, 0},
	'<': location{0, 1},
	'v': location{1, 1},
	'>': location{2, 1},
}

func findPaths(forbiddenLocation location, start location, end location) [][]rune {
	if start.x == end.x && start.y == end.y {
		return [][]rune{[]rune{'A'}}
	}
	dx := end.x - start.x
	dy := end.y - start.y
	var resultPaths [][]rune
	if dx != 0 {
		if dx > 0 {
			newLocation := location{x: start.x + 1, y: start.y}
			if newLocation.x != forbiddenLocation.x || newLocation.y != forbiddenLocation.y {
				computedPaths := findPaths(forbiddenLocation, newLocation, end)
				for _, path := range computedPaths {
					resultPaths = append(resultPaths, append([]rune{'>'}, path...))
				}
			}
		} else {
			newLocation := location{x: start.x - 1, y: start.y}
			if newLocation.x != forbiddenLocation.x || newLocation.y != forbiddenLocation.y {
				computedPaths := findPaths(forbiddenLocation, newLocation, end)
				for _, path := range computedPaths {
					resultPaths = append(resultPaths, append([]rune{'<'}, path...))
				}
			}
		}
	}
	if dy != 0 {
		if dy > 0 {
			newLocation := location{x: start.x, y: start.y + 1}
			if newLocation.x != forbiddenLocation.x || newLocation.y != forbiddenLocation.y {
				computedPaths := findPaths(forbiddenLocation, newLocation, end)
				for _, path := range computedPaths {
					resultPaths = append(resultPaths, append([]rune{'v'}, path...))
				}
			}
		} else {
			newLocation := location{x: start.x, y: start.y - 1}
			if newLocation.x != forbiddenLocation.x || newLocation.y != forbiddenLocation.y {
				computedPaths := findPaths(forbiddenLocation, newLocation, end)
				for _, path := range computedPaths {
					resultPaths = append(resultPaths, append([]rune{'^'}, path...))
				}
			}
		}
	}
	return resultPaths
}

func findAllPairs(keypad map[rune]location, forbiddenLocation location) map[rune]map[rune][][]rune {
	result := make(map[rune]map[rune][][]rune)
	for k1 := range keypad {
		for k2 := range keypad {
			_, exits := result[k1]
			if !exits {
				result[k1] = make(map[rune][][]rune)
			}
			if k1 == k2 {
				result[k1][k2] = [][]rune{}
			} else {
				a := findPaths(forbiddenLocation, keypad[k1], keypad[k2])
				result[k1][k2] = a
			}

		}
	}
	return result
}

func findPossiblePaths(allPairs map[rune]map[rune][][]rune, sequence []rune, start rune) [][]rune {
	currentLocation := start
	var possiblePaths [][]rune

	for _, nextLocation := range sequence {
		pathCandidates := allPairs[currentLocation][nextLocation]
		if len(pathCandidates) == 0 {
			pathCandidates = append(pathCandidates, []rune{'A'})
		}
		var newPaths [][]rune
		for _, path := range pathCandidates {
			if len(possiblePaths) == 0 {
				newPaths = append(newPaths, path)
			} else {
				for _, possiblePath := range possiblePaths {
					var joinedPath []rune
					joinedPath = append(joinedPath, possiblePath...)
					joinedPath = append(joinedPath, path...)
					newPaths = append(newPaths, joinedPath)
				}
			}

		}
		possiblePaths = newPaths
		currentLocation = nextLocation
	}
	return possiblePaths
}

var cache = make(map[string]int)

func computeKey(start rune, end rune, robot int) string {
	return string(start) + "," + string(end) + "," + string(robot)
}

func findPossiblePaths2(allPairs map[rune]map[rune][][]rune, sequence []rune, start rune, robotNumber int) int {
	if robotNumber == 0 {
		return 1
	}
	currentLocation := start
	finalPaths := 0
	for _, nextLocation := range sequence {

		cached, exits := cache[computeKey(currentLocation, nextLocation, robotNumber)]
		if exits {
			finalPaths += cached
			currentLocation = nextLocation
			continue
		}

		pathCandidates := allPairs[currentLocation][nextLocation]
		if len(pathCandidates) == 0 {
			pathCandidates = append(pathCandidates, []rune{'A'})
		}

		pathL := 1000000000000000
		for _, pathCandidate := range pathCandidates {
			le := findPossiblePaths2(allPairs, pathCandidate, 'A', robotNumber-1)
			if le < pathL {
				pathL = le
			}
		}
		cache[computeKey(currentLocation, nextLocation, robotNumber)] = pathL
		currentLocation = nextLocation
		finalPaths += pathL
	}
	return finalPaths
}

func findShortesCode(code []rune) int {
	allNumericPairs := findAllPairs(numericKeypad, location{0, 3})

	possiblePaths := findPossiblePaths(allNumericPairs, code, 'A')

	allDirectionPairs := findAllPairs(directionalKeypad, location{0, 0})
	numRobots := 2
	for i := 0; i < numRobots; i++ {
		var secondPaths [][]rune
		for _, possiblePath := range possiblePaths {
			secondPaths = append(secondPaths, findPossiblePaths(allDirectionPairs, possiblePath, 'A')...)
		}
		possiblePaths = secondPaths
	}

	minLength := 1000000
	for _, possiblePath := range possiblePaths {
		if len(possiblePath) < minLength {
			minLength = len(possiblePath)
		}
	}

	return minLength
}

func findShortesCode2(code []rune) int {
	allNumericPairs := findAllPairs(numericKeypad, location{0, 3})

	possiblePaths := findPossiblePaths(allNumericPairs, code, 'A')

	allDirectionPairs := findAllPairs(directionalKeypad, location{0, 0})
	numRobots := 26
	minSum := 100000000000000000
	for _, possiblePath := range possiblePaths {
		s := findPossiblePaths2(allDirectionPairs, possiblePath, 'A', numRobots)
		if s < minSum {
			minSum = s
		}
	}
	return minSum
}

func getNumericPart(code []rune) int {
	sum := 0
	for _, r := range code {
		if r >= '0' && r <= '9' {
			sum *= 10
			sum += int(r - '0')
		}
	}
	return sum
}

func partOne(codes [][]rune) int {
	sum := 0
	for _, code := range codes {
		sum += findShortesCode(code) * getNumericPart(code)
	}
	return sum
}

func partTwo(codes [][]rune) int {
	sum := 0
	for _, code := range codes {
		sum += findShortesCode2(code) * getNumericPart(code)
	}
	return sum
}

func parseInput(lines []string) [][]rune {
	var codes [][]rune
	for _, line := range lines {
		codes = append(codes, []rune(line))
	}
	return codes
}

func main() {

	scanner := bufio.NewScanner(os.Stdin)

	var lines []string
	for scanner.Scan() {
		line := scanner.Text()
		lines = append(lines, line)
	}

	codes := parseInput(lines)

	fmt.Println("Part 1:", partOne(codes))
	fmt.Println("Part 2:", partTwo(codes))
}
