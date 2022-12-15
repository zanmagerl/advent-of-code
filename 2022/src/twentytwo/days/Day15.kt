package twentytwo.days

import twentytwo.utilities.readStrings
import kotlin.math.abs

data class Beacon(val x: Int, val y: Int)
data class Sensor(val x: Int, val y: Int, val minX: Int, val maxX: Int, val minY: Int, val maxY: Int) {

    companion object {
        fun of(x: Int, y: Int, beacon: Beacon): Sensor {
            val distance = abs(beacon.x - x) + abs(beacon.y - y)
            return Sensor(x = x, y = y, minX = x - distance, maxX = x + distance, minY = y - distance, maxY = y + distance)
        }
    }

    fun doesCoverPoint(x: Int, y: Int): Boolean {
        if (x in minX..maxX && y in minY..maxY) {
            val rowDiff = abs(this.y - y)
            return x in (minX + rowDiff).. (maxX - rowDiff)
        }
        return false
    }

    fun returnIntervalEnd(y: Int): Int {
        val rowDiff = abs(this.y - y)
        return maxX - rowDiff + 1
    }
}

private fun partOne(sensors: List<Sensor>, beacons: List<Beacon>): Int {
    val (minX, maxX) = sensors.minOf { it.minX } to sensors.maxOf { it.maxX }

    val y = 2000000

    return (minX..maxX).fold(mutableListOf<Pair<Int, Int>>()) { coveredPoints, x ->
        if (sensors.any { it.doesCoverPoint(x, y) }) {
            coveredPoints.add(x to y)
        }
        coveredPoints
    }.filter { candidate ->
        sensors.none { it.x == candidate.first && it.y == candidate.second } &&
        beacons.none { it.x == candidate.first && it.y == candidate.second }
    }.size

}

/**
 * We do not need to check for every (x,y) point, but we can actually jump over intervals that are covered by current sensor.
 * In short the algorithm is:
 * 1. Find sensor that covers current (x,y) point
 * 2. a) if there is such sensor find its end range r for y-th row and go to 1. with point (x+r,y)
 *    b) if there is no sensor that covers the point return the tuning frequency
 */
private fun partTwo(sensors: List<Sensor>): Long {
    (0..4000000).map { y ->
        var x = 0
        while (x < 4000000) {
            x = sensors.firstOrNull { it.doesCoverPoint(x, y) }
                ?.returnIntervalEnd(y)
                ?: return x * 4000000L + y
        }
    }
    return 1L
}

fun main(){

    val sensors = mutableListOf<Sensor>()
    val beacons = mutableListOf<Beacon>()

    readStrings().map { Regex("-?\\d+").findAll(it).map { it.value }.toList() }
            .map {
                beacons.add(Beacon(it[2].toInt(), it[3].toInt()))
                sensors.add(Sensor.of(it[0].toInt(), it[1].toInt(), beacons.last()))
            }

    println(partOne(sensors, beacons))
    println(partTwo(sensors))
}