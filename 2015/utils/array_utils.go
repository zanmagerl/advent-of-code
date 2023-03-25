package utils

// Applies given transform function to every element of given element of array
func Transform[T any, U any](elements []T, transform func(T) U) []U {
	transformedElements := make([]U, len(elements))
	for i, element := range elements {
		transformedElements[i] = transform(element)
	}
	return transformedElements
}

// Sums given list of integers
func Sum(elements []int) int {
	result := 0
	for _, element := range elements {
		result += element
	}
	return result
}
