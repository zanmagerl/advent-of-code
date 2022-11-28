package days

import utilities.readInts

class Day01 {

    fun partOne(input: List<Int>): Int {

        var result = 0

        for (i in 1 until input.size){
            if (input[i] > input[i-1]){
                result++
            }
        }

        return result
    }

    fun partTwo(input: List<Int>): Int {
        return input.windowed(4).count { it.last() > it.first() }
    }
}

fun main(){

    val dayOne = Day01()

    val input = readInts()

    println(dayOne.partOne(input))
    println(dayOne.partTwo(input))

}