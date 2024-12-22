package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

func partOne(disk []int) int64 {
	var sum int64 = 0
	index := 0
	fragmetedIndex := len(disk) - 1
	currentFragmentedBlockIndex := disk[fragmetedIndex]
	for blockIndex, blockLength := range disk {
		// Actual file
		if blockIndex%2 == 0 {
			size := blockLength
			if blockIndex == fragmetedIndex {
				size = currentFragmentedBlockIndex
			}
			for i := index; i < index+size; i++ {
				sum += int64(i * (blockIndex / 2))
			}
		} else {
			for i := index; i < index+blockLength; i++ {
				sum += int64(i * (fragmetedIndex / 2))
				currentFragmentedBlockIndex--
				for currentFragmentedBlockIndex == 0 {
					fragmetedIndex -= 2
					currentFragmentedBlockIndex = disk[fragmetedIndex]
				}
				if blockIndex/2 == fragmetedIndex/2 {
					break
				}
			}
		}
		index += blockLength
		if blockIndex >= fragmetedIndex {
			break
		}
	}

	return sum
}

func partTwo(disk []int) int {
	occupiedSpac := make(map[int]int)
	sum := 0
	for blockIndex := len(disk) - 1; blockIndex >= 0; blockIndex -= 2 {
		blockLength := disk[blockIndex]
		index := 0
		wasMoved := false
		for spaceIndex := 0; spaceIndex < blockIndex; spaceIndex++ {
			if spaceIndex%2 == 1 {
				occupiedSpace, exists := occupiedSpac[spaceIndex]
				if !exists {
					occupiedSpace = 0
				}
				if blockLength <= disk[spaceIndex] && blockLength <= (disk[spaceIndex]-occupiedSpace) {
					for i := 0; i < blockLength; i++ {
						sum += (index + occupiedSpace + i) * (blockIndex / 2)
					}
					occupiedSpace += blockLength
					occupiedSpac[spaceIndex] = occupiedSpace
					wasMoved = true
					break
				}
			}
			index += disk[spaceIndex]
		}
		if !wasMoved {
			for i := 0; i < blockLength; i++ {
				sum += (index + i) * (blockIndex / 2)
			}
		}
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

	var disk []int
	for _, el := range strings.Split(lines[0], "") {
		num, _ := strconv.Atoi(el)
		disk = append(disk, num)
	}

	fmt.Println("Part 1:", partOne(disk))
	fmt.Println("Part 2:", partTwo(disk))
}
