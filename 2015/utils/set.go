package utils

type Set[T comparable] struct {
	elements map[T]struct{}
}

func New[T comparable]() Set[T] {
	return Set[T]{
		elements: make(map[T]struct{}),
	}
}

func Of[T comparable](values ...T) Set[T] {
	s := New[T]()
	for _, val := range values {
		s.Add(val)
	}
	return s
}

func (set Set[T]) Add(value T) {
	set.elements[value] = struct{}{}
}

func (set Set[T]) Contains(value T) bool {
	_, exists := set.elements[value]
	return exists
}

func (set Set[T]) Remove(value T) {
	delete(set.elements, value)
}

func (set Set[T]) Elements() []T {
	var ls []T
	for el := range set.elements {
		ls = append(ls, el)
	}
	return ls
}
