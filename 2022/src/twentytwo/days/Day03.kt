package twentytwo.days

import twentytwo.utilities.readStrings

private fun calculatePriority(backpacks: List<String>): Int {
    return backpacks.map { it.toSet() }.reduce{acc, backpack -> backpack.intersect(acc) }.first().run {
        if (this.isLowerCase()) {
            this.code - 96
        } else {
            this.code - 38
        }
    }
}

private fun partOne(backpacks: List<String>): Int {
    return backpacks.sumOf { calculatePriority( it.chunked(it.length / 2)) }
}

private fun partTwo(backpacks: List<String>): Int {
    return backpacks.chunked(3).sumOf { calculatePriority(it) }
}

fun main(){

    val input = readStrings()

    println(partOne(input))
    println(partTwo(input))
}