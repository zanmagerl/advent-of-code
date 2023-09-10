package main

import (
	"bufio"
	"fmt"
	"os"
)

type Location struct {
	x int
	y int
}

func (l Location) String() string {
	return fmt.Sprintf("x: %d, y: %d", l.x, l.y)
}

func moveWithDirection(location Location, direction rune) Location {
	switch direction {
	case '>':
		return move(location, Location{x: 1})
	case '<':
		return move(location, Location{x: -1})
	case '^':
		return move(location, Location{y: -1})
	case 'v':
		return move(location, Location{y: 1})
	default:
		panic("Unknown direction")
	}
}

func move(a Location, b Location) Location {
	return Location{x: a.x + b.x, y: a.y + b.y}
}

func deliverGifts(chart map[Location]bool, path string, offset int, step int) map[Location]bool {
	currentPosition := Location{}
	chart[currentPosition] = true
	for i := offset; i < len(path); i += step {
		currentPosition = moveWithDirection(currentPosition, rune(path[i]))
		chart[currentPosition] = true
	}
	return chart
}

func partOne(path string) int {
	return len(deliverGifts(make(map[Location]bool), path, 0, 1))
}

func partTwo(path string) int {
	santaChart := deliverGifts(make(map[Location]bool), path, 0, 2)
	robotChart := deliverGifts(santaChart, path, 1, 2)
	return len(robotChart)
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	scanner.Scan()
	var input = scanner.Text()

	fmt.Println("Part 1:", partOne(input))
	fmt.Println("Part 2:", partTwo(input))
}
