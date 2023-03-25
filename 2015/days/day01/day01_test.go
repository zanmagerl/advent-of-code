package main

import "testing"

func TestPartOne(t *testing.T) {
	examples := []string{"(())", "(())", "(((", "(()(()(", "))(((((", "())", "))(", ")))", ")())())"}
	expectedResults := []int{0, 0, 3, 3, 3, -1, -1, -3, -3}
	for index, example := range examples {
		result := partOne(examples[index])
		if expectedResults[index] != result {
			t.Errorf("Wrong result for test %s, got %d, want %d", example, result, expectedResults[index])
		}
	}
}

func TestPartTwo(t *testing.T) {
	examples := []string{")", "()())"}
	expectedResults := []int{1, 5}
	for index, example := range examples {
		result := partTwo(examples[index])
		if expectedResults[index] != result {
			t.Errorf("Wrong result for test %s, got %d, want %d", example, result, expectedResults[index])
		}
	}
}
