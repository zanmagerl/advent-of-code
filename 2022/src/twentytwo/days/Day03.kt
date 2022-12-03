package twentytwo.days

import twentytwo.utilities.readStrings

private fun calculatePriority(char: Char): Int {
    return if (char.code >= 92) {
        char.code - 96
    } else {
        char.code - 38
    }
}

private fun calculatePriority(backpacks: List<String>): Int {
    return calculatePriority(backpacks.map { it.toCharArray().toSet() }.reduce{acc, backpack -> backpack.intersect(acc) }.first())
}

fun partOne(backpacks: List<String>): Int {
    return backpacks.sumOf { calculatePriority( it.chunked(it.length / 2)) }
}

fun partTwo(backpacks: List<String>): Int {
    return backpacks.windowed(3, step = 3).sumOf { calculatePriority(it) }
}

fun main(){

    val input = readStrings()

    println(partOne(input))
    println(partTwo(input))
}