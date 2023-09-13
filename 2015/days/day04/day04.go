package main

import (
	"bufio"
	"crypto/md5"
	"encoding/hex"
	"fmt"
	"os"
	"strings"
)

func findKey(secretKey string, prefix string) int {
	number := 0
	signature := secretKey
	for !strings.HasPrefix(signature, prefix) {
		number += 1
		hash := md5.Sum([]byte(fmt.Sprintf("%s%d", secretKey, number)))
		signature = hex.EncodeToString(hash[:])
	}

	return number
}

func partOne(secretKey string) int {
	return findKey(secretKey, "00000")
}

func partTwo(secretKey string) int {
	return findKey(secretKey, "000000")
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	scanner.Scan()
	var input = scanner.Text()

	fmt.Println("Part 1:", partOne(input))
	fmt.Println("Part 2:", partTwo(input))
}
