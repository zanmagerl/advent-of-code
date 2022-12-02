package twentytwo.days

import twentytwo.utilities.readStrings
import twentytwo.utilities.splitToPair

class Day02 {

    fun partOne(guide: List<Pair<String, String>>): Int {
        return guide.sumOf {
            when (it) {
                "A" to "X" -> 3 + 1
                "A" to "Y" -> 6 + 2
                "A" to "Z" -> 0 + 3
                "B" to "X" -> 0 + 1
                "B" to "Y" -> 3 + 2
                "B" to "Z" -> 6 + 3
                "C" to "X" -> 6 + 1
                "C" to "Y" -> 0 + 2
                "C" to "Z" -> 3 + 3
                else -> throw IllegalArgumentException("Undefined sequence")
            }.toInt()
        }
    }

    fun partTwo(guide: List<Pair<String, String>>): Int {
        return guide.sumOf {
            when (it) {
                "A" to "X" -> 0 + 3
                "A" to "Y" -> 3 + 1
                "A" to "Z" -> 6 + 2
                "B" to "X" -> 0 + 1
                "B" to "Y" -> 3 + 2
                "B" to "Z" -> 6 + 3
                "C" to "X" -> 0 + 2
                "C" to "Y" -> 3 + 3
                "C" to "Z" -> 6 + 1
                else -> throw IllegalArgumentException("Undefined sequence")
            }.toInt()
        }
    }
}

fun main(){

    val day02 = Day02()

    val input = readStrings().map { it.splitToPair(" ") }

    println(day02.partOne(input))
    println(day02.partTwo(input))
}