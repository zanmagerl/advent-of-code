package days

import utilities.readStrings

class Day09 {

    fun isLowPoint(lines: List<List<Int>>, row: Int, column: Int): Boolean {
        var lowest = true

        if (row > 0) {
            lowest = lowest && lines[row-1][column] > lines[row][column]
        }

        if (column > 0) {
            lowest = lowest && lines[row][column-1] > lines[row][column]
        }

        if (row < lines.size - 1) {
            lowest = lowest && lines[row+1][column] > lines[row][column]
        }

        if (column < lines[row].size - 1) {
            lowest = lowest && lines[row][column+1] > lines[row][column]
        }

        return lowest
    }

    fun partOne(lines: List<List<Int>>): Int {

        var score = 0

        for (row in 0 until lines.size){
            for (column in 0 until lines[row].size) {
                if (isLowPoint(lines, row, column)) {
                    score += lines[row][column] + 1
                }
            }
        }

        return score
    }


    private fun findBasinSize(lines: List<List<Int>>, row: Int, column: Int, alreadyVisited: MutableList<Pair<Int, Int>>): Int {
        if (row < 0 || column < 0 || row >= lines.size || column >= lines[0].size) {
            return 0
        }

        if (lines[row][column] == 9) {
            return 0
        }

        val currentLocation = Pair(row, column)
        if (currentLocation in alreadyVisited) {
            return 0
        }
        alreadyVisited.add(currentLocation)

        var size = 1

        size += findBasinSize(lines, row-1, column, alreadyVisited)

        size += findBasinSize(lines, row+1, column, alreadyVisited)

        size += findBasinSize(lines, row, column-1, alreadyVisited)

        size += findBasinSize(lines, row, column+1, alreadyVisited)


        return size
    }

    fun partTwo(lines: List<List<Int>>): Int {

        val basinSizes = mutableListOf<Int>()

        for (row in 0 until lines.size){
            for (column in 0 until lines[row].size) {
                if (isLowPoint(lines, row, column)) {
                    basinSizes.add(findBasinSize(lines, row, column, mutableListOf()))
                }
            }
        }
        basinSizes.sort()

        return basinSizes.subList(basinSizes.size - 3, basinSizes.size).fold(1){ acc, i -> acc * i }
    }

}

fun main(){

    val dayNine = Day09()

    val lines = readStrings().map { a -> a.toCharArray().map { a -> a.toString().toInt() } }

    println(dayNine.partOne(lines))
    println(dayNine.partTwo(lines))

}
