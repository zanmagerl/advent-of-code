package days

import utilities.readStrings
import java.util.*
import kotlin.collections.ArrayList

class Day10 {

    private val pairMappings = mapOf(
        "(" to ")",
        "[" to "]",
        "{" to "}",
        "<" to ">"
    )

    private fun checkLine(line: String): Int {

        val scoreMapping = mapOf(
            ")" to 3,
            "]" to 57,
            "}" to 1197,
            ">" to 25137
        )

        val symbols = Stack<String>()

        for (char in line.toCharArray().map { a -> a.toString() }) {
            if (char in listOf("(", "[", "{", "<")) {
                symbols.push(char)
            } else {
                if (pairMappings[symbols.pop()] != char){
                    return scoreMapping[char]!!
                }
            }
        }

        return 0
    }

    private fun evaluateLine(line: String): Long {

        val scoreMapping = mapOf(
            "(" to 1,
            "[" to 2,
            "{" to 3,
            "<" to 4
        )

        val symbols = Stack<String>()

        for (char in line.toCharArray().map { a -> a.toString() }) {
            if (char in listOf("(", "[", "{", "<")) {
                symbols.push(char)
            } else {
                symbols.pop()
            }
        }
        var score = 0L

        var char: String
        while (!symbols.empty()) {
            char = symbols.pop()
            score *= 5
            score += scoreMapping[char]!!
        }

        return score
    }


    fun partOne(lines: List<String>): Int {

        var score = 0

        for (line in lines) {
            score += checkLine(line)
        }

        return score
    }


    fun partTwo(lines: List<String>): Long {

        val scores = mutableListOf<Long>()

        for (line in lines) {
            if (checkLine(line) == 0) {
                scores.add(evaluateLine(line))
            }
        }
        scores.sort()
        return scores[scores.size / 2]
    }

}

fun main(){

    val dayTen = Day10()

    val lines = readStrings()

    println(dayTen.partOne(lines))
    println(dayTen.partTwo(lines))

}
