package main

import "testing"

func TestPartOne(t *testing.T) {
	examples := [][]string{{"1abc2",
		"pqr3stu8vwx",
		"a1b2c3d4e5f",
		"treb7uchet"}}
	expectedResults := []int{142}
	for index, example := range examples {
		result := partOne(examples[index])
		if expectedResults[index] != result {
			t.Errorf("Wrong result for test %s, got %d, want %d", example, result, expectedResults[index])
		}
	}
}

func TestPartTwo(t *testing.T) {
	examples := [][]string{{
		"rhqrpdxsqhgxzknr2foursnrcfthree",
	}}
	expectedResults := []int{23}
	for index, example := range examples {
		result := partTwo(examples[index])
		if expectedResults[index] != result {
			t.Errorf("Wrong result for test %s, got %d, want %d", example, result, expectedResults[index])
		}
	}
}
