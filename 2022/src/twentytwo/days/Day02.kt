package twentytwo.days

import twentytwo.utilities.readStrings

class Day02 {

    private fun applyGuessedStrategy(player1: String, player2: String): Int {
        return when (player1) {
            "A" -> when (player2) {
                "X" -> 3 + 1
                "Y" -> 6 + 2
                else -> 0 + 3
            }
            "B" -> when (player2) {
                "X" -> 0 + 1
                "Y" -> 3 + 2
                else -> 6 + 3
            }
            "C" -> when (player2) {
                "X" -> 6 + 1
                "Y" -> 0 + 2
                else -> 3 + 3
            }
            else -> 0
        }
    }

    private fun applyActualStrategy(player1: String, player2: String): Int {
        return when (player1) {
            "A" -> when (player2) {
                "X" -> 3
                "Y" -> 4
                else -> 8
            }
            "B" -> when (player2) {
                "X" -> 1
                "Y" -> 5
                else -> 9
            }
            "C" -> when (player2) {
                "X" -> 2
                "Y" -> 6
                else -> 7
            }
            else -> 0
        }
    }

    fun partOne(guide: List<Pair<String, String>>): Int {
        return guide.sumOf { applyGuessedStrategy(it.first, it.second) }
    }

    fun partTwo(guide: List<Pair<String, String>>): Int {
        return guide.sumOf { applyActualStrategy(it.first, it.second) }
    }
}

fun main(){

    val day02 = Day02()

    val input = readStrings().map { Pair(it.split(" ")[0], it.split(" ")[1]) }

    println(day02.partOne(input))
    println(day02.partTwo(input))
}