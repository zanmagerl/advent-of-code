package main

import (
	"bufio"
	"fmt"
	"os"
	"regexp"
	"strconv"
	"strings"
)

type Game struct {
	id    int
	red   int
	green int
	blue  int
}

func partOne(games []Game) int {
	possibleGamesSum := 0
	for _, game := range games {
		if game.red <= 12 && game.green <= 13 && game.blue <= 14 {
			possibleGamesSum += game.id
		}
	}
	return possibleGamesSum
}

func partTwo(games []Game) int {
	possibleGamesSum := 0
	for _, game := range games {
		possibleGamesSum += game.blue * game.green * game.red
	}
	return possibleGamesSum
}

// Match looks like `<number> <red|green|blue>`
func findMatches(regex *regexp.Regexp, line string) int {
	matches := regex.FindAllString(line, -1)
	max := -1
	for _, match := range matches {
		number, _ := strconv.Atoi(strings.Split(match, " ")[0])
		if number > max {
			max = number
		}
	}
	return max
}

func parseInput(lines []string) []Game {
	var games []Game

	patternRed := "(\\d+)\\s+red"
	regexRed, _ := regexp.Compile(patternRed)

	patternGreen := "(\\d+)\\s+green"
	regexGreen, _ := regexp.Compile(patternGreen)

	patternBlue := "(\\d+)\\s+blue"
	regexBlue, _ := regexp.Compile(patternBlue)

	for i, line := range lines {
		games = append(games, Game{
			id:    i + 1,
			red:   findMatches(regexRed, line),
			green: findMatches(regexGreen, line),
			blue:  findMatches(regexBlue, line),
		})
	}
	return games
}

func main() {

	scanner := bufio.NewScanner(os.Stdin)
	var lines []string

	for scanner.Scan() {
		line := scanner.Text()
		lines = append(lines, line)
	}

	games := parseInput(lines)

	fmt.Println("Part 1:", partOne(games))
	fmt.Println("Part 2:", partTwo(games))
}
