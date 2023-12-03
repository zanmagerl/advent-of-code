package utils

func Max(a, b int) int {
	if b > a {
		return b
	}
	return a
}

func Min(a, b int) int {
	if b < a {
		return b
	}
	return a
}

func Abs(a int) int {
	if a < 0 {
		return -a
	}
	return a
}