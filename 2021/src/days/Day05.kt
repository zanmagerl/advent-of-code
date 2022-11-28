package days

import utilities.readStrings

data class Segment (
    val x1: Int,
    val y1: Int,
    val x2: Int,
    val y2: Int
)

class Day05{

    fun partOne(segments: List<Segment>): Int {

        val floor = hashMapOf<Pair<Int, Int>, Int>()

        for (segment in segments) {

            val xDifference = segment.x2 - segment.x1
            val yDifference = segment.y2 - segment.y1

            if (xDifference != 0) {

                val range = if (xDifference >= 0) {
                    0..xDifference
                } else {
                    0 downTo xDifference
                }

                for (i in range) {
                    val location = Pair(segment.x1 + i, segment.y1)
                    if (floor[location] == null) {
                        floor[location] = 1
                    } else {
                        floor[location] = floor.get(location)!! + 1
                    }
                }
            } else if (yDifference != 0){

                val range = if (yDifference >= 0) {
                    0..yDifference
                } else {
                    0 downTo yDifference
                }


                for (i in range) {
                    val location = Pair(segment.x1, segment.y1 + i)
                    if (floor[location] == null) {
                        floor[location] = 1
                    } else {
                        floor[location] = floor.get(location)!! + 1
                    }
                }
            }


        }

        var numberOfOverlaps = 0

        for (key in floor.keys) {
            if (floor[key]!! > 1) numberOfOverlaps++
        }

        return numberOfOverlaps
    }

    fun partTwo(segments: List<Segment>): Int {

        val floor = hashMapOf<Pair<Int, Int>, Int>()

        for (segment in segments) {

            val xDifference = segment.x2 - segment.x1
            val yDifference = segment.y2 - segment.y1

            if (xDifference != 0 && yDifference == 0) {

                val range = if (xDifference >= 0) {
                    0..xDifference
                } else {
                    0 downTo xDifference
                }

                for (i in range) {
                    val location = Pair(segment.x1 + i, segment.y1)
                    if (floor[location] == null) {
                        floor[location] = 1
                    } else {
                        floor[location] = floor.get(location)!! + 1
                    }
                }
            } else if (yDifference != 0 && xDifference == 0){

                val range = if (yDifference >= 0) {
                    0..yDifference
                } else {
                    0 downTo yDifference
                }


                for (i in range) {
                    val location = Pair(segment.x1, segment.y1 + i)
                    if (floor[location] == null) {
                        floor[location] = 1
                    } else {
                        floor[location] = floor.get(location)!! + 1
                    }
                }
            } else if (yDifference != 0 && xDifference != 0) {

                val xRange = if (xDifference >= 0) {
                    0..xDifference
                } else {
                    0 downTo xDifference
                }

                val yRange = if (yDifference >= 0) {
                    0..yDifference
                } else {
                    0 downTo yDifference
                }
                val xRangeList = xRange.toList()
                val yRangeList = yRange.toList()

                for (i in 0 until xRangeList.size) {
                    val location = Pair(segment.x1 + xRangeList[i], segment.y1 + yRangeList[i])
                    if (floor[location] == null) {
                        floor[location] = 1
                    } else {
                        floor[location] = floor.get(location)!! + 1
                    }
                }

            }

        }

        var numberOfOverlaps = 0

        for (key in floor.keys) {
            if (floor[key]!! > 1) numberOfOverlaps++
        }

        return numberOfOverlaps
    }

}

fun main(){

    val dayFive = Day05()

    val lines = readStrings()

    val segments = mutableListOf<Segment>()

    for (line in lines) {
        val values = line.split("->").map { a -> a.trim() }.map { a -> a.split(",") }

        segments.add(Segment(values[0][0].toInt(), values[0][1].toInt(), values[1][0].toInt(), values[1][1].toInt()))
    }

    println(dayFive.partOne(segments.filter { segment -> segment.x1 == segment.x2 || segment.y1 == segment.y2 }))
    println(dayFive.partTwo(segments))

}
