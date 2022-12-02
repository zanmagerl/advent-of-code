package twentytwo.utilities

fun String.splitToPair(delimiter: String): Pair<String, String> {
    val splitString = this.split(delimiter)
    if (splitString.size != 2) {
        throw IllegalArgumentException("Intended string '$this' has ambiguous transformation to Pair with given delimiter '$delimiter'")
    }
    return splitString[0] to splitString[1]
}