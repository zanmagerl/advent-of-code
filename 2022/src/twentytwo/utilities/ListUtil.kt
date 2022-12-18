package twentytwo.utilities

fun <T> List<T>.toPair(): Pair<T, T> {
    if (this.size != 2) {
        throw IllegalArgumentException("Intended list '$this' has ambiguous transformation to Pair")
    }
    return this[0] to this[1]
}

fun <T,R> List<T>.use(transform: (T) -> R): List<T> {
    return this.map {
        transform(it)
        it
    }
}