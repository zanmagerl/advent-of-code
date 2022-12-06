package twentytwo.days

private fun findMarker(buffer: String, numDistinctCharacters: Int): Int {
    return buffer.windowed(numDistinctCharacters)
            .indexOfFirst { it.toSet().size == numDistinctCharacters}
            .plus(numDistinctCharacters)
}

private fun partOne(buffer: String): Int {
    return findMarker(buffer, 4)
}

private fun partTwo(buffer: String): Int {
    return findMarker(buffer, 14)
}

fun main(){

    val input = readln()

    println(partOne(input))
    println(partTwo(input))
}