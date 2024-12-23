package main

import (
	"bufio"
	"fmt"
	"os"
	"sort"
	"strconv"
	"strings"
)

type computer struct {
	id   int
	name string
}

func computeKey(a int, b int, c int) string {
	keys := []int{a, b, c}
	sort.Slice(keys, func(i, j int) bool { return keys[i] < keys[j] })
	return strconv.Itoa(keys[0]) + "," + strconv.Itoa(keys[1]) + "," + strconv.Itoa(keys[2])
}

func partOne(computers map[string]computer, adjancencyMatrix [][]bool) int {
	sum := 0
	visited := make(map[string]bool)
	for computerName := range computers {
		if strings.HasPrefix(computerName, "t") {
			computerId := computers[computerName].id
			for i := 0; i < len(adjancencyMatrix); i++ {
				if i == computerId {
					continue
				}
				if adjancencyMatrix[computerId][i] {
					for j := i + 1; j < len(adjancencyMatrix); j++ {
						if j == computerId {
							continue
						}
						if adjancencyMatrix[computerId][j] && adjancencyMatrix[i][j] {
							key := computeKey(computerId, i, j)
							_, wasVisited := visited[key]
							if !wasVisited {
								visited[key] = true
								sum++
							}
						}
					}
				}
			}
		}
	}
	return sum
}

func add(m map[int]computer, c computer) map[int]computer {
	nextM := make(map[int]computer)
	for _, rComputer := range m {
		nextM[rComputer.id] = rComputer
	}
	nextM[c.id] = c
	return nextM
}

func intersect(a map[int]computer, b map[int]computer) map[int]computer {
	nextA := make(map[int]computer)
	for key := range b {
		_, exists := a[key]
		if exists {
			nextA[key] = a[key]
		}
	}
	return nextA
}

var largestCliqueSize = -1
var largestClique = make(map[int]map[int]computer)

// https://en.wikipedia.org/wiki/Bron%E2%80%93Kerbosch_algorithm
func findMaxClique(adjacencyMatrix [][]bool, idToComputerMap map[int]computer, r map[int]computer, p map[int]computer, x map[int]computer) {
	if len(p) == 0 && len(x) == 0 {
		largestClique[len(r)] = r
		largestCliqueSize = len(r)
		return
	}
	for _, comp := range p {
		var neighbours = make(map[int]computer)
		for i := 0; i < len(adjacencyMatrix); i++ {
			if adjacencyMatrix[comp.id][i] && i != comp.id {
				neighbours[i] = idToComputerMap[i]
			}
		}
		findMaxClique(adjacencyMatrix, idToComputerMap, add(r, comp), intersect(p, neighbours), intersect(x, neighbours))
		delete(p, comp.id)
		x[comp.id] = comp
	}
}

func generatePassword(maxClique map[int]computer) string {
	var computers []string
	for k := range maxClique {
		computers = append(computers, maxClique[k].name)
	}
	sort.Slice(computers, func(i, j int) bool { return computers[i] < computers[j] })
	return strings.Join(computers, ",")
}

func partTwo(computers map[string]computer, adjancencyMatrix [][]bool) string {
	idToComputerMap := make(map[int]computer)
	for computerName := range computers {
		computer := computers[computerName]
		idToComputerMap[computer.id] = computer
	}
	findMaxClique(adjancencyMatrix, idToComputerMap, make(map[int]computer), idToComputerMap, make(map[int]computer))

	maxSize := -1
	for size := range largestClique {
		if size > maxSize {
			maxSize = size
		}
	}

	return generatePassword(largestClique[maxSize])
}

func parseInput(lines []string) (map[string]computer, [][]bool) {
	computers := make(map[string]computer)
	count := 0
	for _, line := range lines {
		split := strings.Split(line, "-")
		_, exists := computers[split[0]]
		if !exists {
			computers[split[0]] = computer{id: count, name: split[0]}
			count++
		}
		_, exists = computers[split[1]]
		if !exists {
			computers[split[1]] = computer{id: count, name: split[1]}
			count++
		}
	}

	var adjecencyMatrix [][]bool
	for i := 0; i < len(computers); i++ {
		var adjacencyRow []bool
		for j := 0; j < count; j++ {
			adjacencyRow = append(adjacencyRow, false)
		}
		adjecencyMatrix = append(adjecencyMatrix, adjacencyRow)
	}

	for _, line := range lines {
		split := strings.Split(line, "-")
		id1 := computers[split[0]].id
		id2 := computers[split[1]].id
		adjecencyMatrix[id1][id2] = true
		adjecencyMatrix[id2][id1] = true
	}

	return computers, adjecencyMatrix
}

func main() {

	scanner := bufio.NewScanner(os.Stdin)
	var lines []string

	for scanner.Scan() {
		line := scanner.Text()
		lines = append(lines, line)
	}

	computers, adjancencyMatrix := parseInput(lines)

	fmt.Println("Part 1:", partOne(computers, adjancencyMatrix))
	fmt.Println("Part 2:", partTwo(computers, adjancencyMatrix))
}
