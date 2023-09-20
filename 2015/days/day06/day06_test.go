package main

import "testing"

func TestPartOne(t *testing.T) {
	examples := [][]Instruction{
		{{operation: TurnOn, startLocation: Location{0, 0}, endLocation: Location{999, 999}}},
		{{operation: Toggle, startLocation: Location{0, 0}, endLocation: Location{999, 0}}},
		{{operation: TurnOff, startLocation: Location{0, 0}, endLocation: Location{500, 0}}},
	}
	expectedResults := []int{1000000, 1000, 0}
	for index, example := range examples {
		result := partOne(examples[index])
		if expectedResults[index] != result {
			t.Errorf("Wrong result for test %v, got %d, want %d", example, result, expectedResults[index])
		}
	}
}
