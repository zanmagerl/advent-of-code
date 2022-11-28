package days

import utilities.readStrings
import kotlin.math.abs


class Day07{

    fun partOne(positions: List<Int>): Int {

        var leastFuel = Int.MAX_VALUE

        val minPosition = positions.minOrNull()!!
        val maxPosition = positions.maxOrNull()!!

        for (i in minPosition..maxPosition) {

            var fuel = 0

            for (position in positions) {
                fuel += abs(position - i)
            }

            if (fuel < leastFuel){
                leastFuel = fuel
            }

        }

        return leastFuel
    }

    fun partTwo(positions: List<Int>): Int {

        var leastFuel = Int.MAX_VALUE

        val minPosition = positions.minOrNull()!!
        val maxPosition = positions.maxOrNull()!!

        for (i in minPosition..maxPosition) {

            var fuel = 0

            for (position in positions) {

                for (v in 1..abs(position - i)){
                    fuel += v
                }
            }

            if (fuel < leastFuel){
                leastFuel = fuel
            }

        }

        return leastFuel
    }

}

fun main(){

    val daySeven = Day07()

    val lines = readStrings()[0].split(",").map { a -> a.toInt() }

    println(daySeven.partOne(lines))
    println(daySeven.partTwo(lines))

}
