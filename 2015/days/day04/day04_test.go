package main

import "testing"

func TestPartOne(t *testing.T) {
	examples := []string{"abcdef", "pqrstuv"}
	expectedResults := []int{609043, 1048970}
	for index, example := range examples {
		result := partOne(examples[index])
		if expectedResults[index] != result {
			t.Errorf("Wrong result for test %s, got %d, want %d", example, result, expectedResults[index])
		}
	}
}
