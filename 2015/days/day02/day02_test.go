package main

import "testing"

func TestPartOne(t *testing.T) {
	examples := []Present{convertToPresent("2x3x4"), convertToPresent("1x1x10")}
	expectedResults := []int{58, 43}
	for index, example := range examples {
		result := partOne([]Present{example})
		if expectedResults[index] != result {
			t.Errorf("Wrong result for test %v, got %d, want %d", example, result, expectedResults[index])
		}
	}
}

func TestPartTwo(t *testing.T) {
	examples := []Present{convertToPresent("2x3x4"), convertToPresent("1x1x10")}
	expectedResults := []int{34, 14}
	for index, example := range examples {
		result := partTwo([]Present{example})
		if expectedResults[index] != result {
			t.Errorf("Wrong result for test %v, got %d, want %d", example, result, expectedResults[index])
		}
	}
}
