package main

import "testing"

func TestPartOne(t *testing.T) {
	examples := []string{">", "^>v<", "^v^v^v^v^v"}
	expectedResults := []int{2, 4, 2}
	for index, example := range examples {
		result := partOne(examples[index])
		if expectedResults[index] != result {
			t.Errorf("Wrong result for test %s, got %d, want %d", example, result, expectedResults[index])
		}
	}
}

func TestPartTwo(t *testing.T) {
	examples := []string{"^v", "^>v<", "^v^v^v^v^v"}
	expectedResults := []int{3, 3, 11}
	for index, example := range examples {
		result := partTwo(examples[index])
		if expectedResults[index] != result {
			t.Errorf("Wrong result for test %s, got %d, want %d", example, result, expectedResults[index])
		}
	}
}
