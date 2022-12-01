package twentytwo.days

import twentytwo.utilities.readStrings

class Day01 {

    fun partOne(input: List<String>): Int {
        var mostSum = 0
        var currentSum = 0
        for (line in input) {
            if (line.isBlank()) {
                if (currentSum > mostSum) {
                    mostSum = currentSum
                }
                currentSum = 0
            } else {
                currentSum += line.toInt()
            }
        }
        return mostSum
    }

    fun partTwo(input: List<String>): Int {
        val mostSum = mutableListOf(0,0,0)
        var currentSum = 0
        for (line in input) {
            if (line.isBlank()) {
                val minElement = mostSum.minOrNull()!!
                if (currentSum > minElement) {
                    mostSum[mostSum.indexOf(minElement)] = currentSum
                }
                currentSum = 0
            } else {
                currentSum += line.toInt()
            }
        }
        return mostSum.sum()
    }
}

fun main(){

    val dayOne = Day01()

    val input = readStrings()

    println(dayOne.partOne(input))
    println(dayOne.partTwo(input))

}