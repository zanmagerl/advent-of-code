package main

import "testing"

func TestPartOne(t *testing.T) {
	examples := [][]Game{{
		Game{1, 4, 2, 6},
		Game{2, 1, 3, 1},
		Game{3, 20, 13, 6},
		Game{4, 14, 3, 15},
		Game{5, 6, 3, 2},
	}}
	expectedResults := []int{8}
	for index, example := range examples {
		result := partOne(examples[index])
		if expectedResults[index] != result {
			t.Errorf("Wrong result for test %v, got %d, want %d", example, result, expectedResults[index])
		}
	}
}

func TestPartTwo(t *testing.T) {
	examples := [][]Game{{
		Game{1, 4, 2, 6},
		Game{2, 1, 3, 4},
		Game{3, 20, 13, 6},
		Game{4, 14, 3, 15},
		Game{5, 6, 3, 2},
	}}
	expectedResults := []int{2286}
	for index, example := range examples {
		result := partTwo(examples[index])
		if expectedResults[index] != result {
			t.Errorf("Wrong result for test %v, got %d, want %d", example, result, expectedResults[index])
		}
	}
}
