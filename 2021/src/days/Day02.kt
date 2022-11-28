package days

import utilities.readStrings

class Day02{

    fun partOne(instructions: List<Pair<String, Int>>): Int {

        var horizontal = 0
        var vertical = 0

        for (instruction in instructions) {
            when (instruction.first) {
                "forward" -> horizontal += instruction.second
                "down" -> vertical += instruction.second
                "up" -> vertical -= instruction.second
            }
        }

        return horizontal * vertical
    }

    fun partTwo(instructions: List<Pair<String, Int>>): Int {
        var horizontal = 0
        var vertical = 0
        var aim = 0

        for (instruction in instructions) {
            when (instruction.first) {
                "down" -> aim += instruction.second
                "up" -> aim -= instruction.second
                "forward" -> {
                    horizontal += instruction.second
                    vertical += aim * instruction.second
                }
            }
        }

        return horizontal * vertical
    }

}

fun main(){

    val dayTwo = Day02()

    val lines = readStrings().map { Pair<String, Int>(it.split(" ")[0], it.split(" ")[1].toInt()) }

    println(dayTwo.partOne(lines))
    println(dayTwo.partTwo(lines))

}
