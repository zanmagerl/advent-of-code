package days

import utilities.readStrings

data class Octopus(
        var energyLevel: Int,
        var hasFlashed: Boolean
)

class Day11{

    private fun giveEnergyToNeighbours(grid: List<List<Octopus>>, row: Int, column: Int) {
        val possibleNeighbors = listOf(
                Pair(row-1, column-1),
                Pair(row-1, column),
                Pair(row-1,column+1),
                Pair(row, column-1),
                Pair(row, column+1),
                Pair(row+1, column-1),
                Pair(row+1, column),
                Pair(row+1, column+1)
        )
        for (location in possibleNeighbors){
            if (location.first >= 0 && location.first < grid.size && location.second >= 0 && location.second < grid[row].size){
                grid[location.first][location.second].energyLevel += 1
            }
        }
    }

    fun partOne(grid: List<List<Octopus>>): Int {

        var numberOfFlashes = 0

        for (step in 0 until 100){
            for (row in 0 until grid.size) {
                for (column in 0 until grid[row].size) {
                    grid[row][column].energyLevel += 1
                }
            }

            var change: Boolean

            do {
                change = false
                for (row in 0 until grid.size) {
                    for (column in 0 until grid[row].size) {
                        val octopus = grid[row][column]
                        if (octopus.energyLevel > 9 && octopus.hasFlashed.not()){
                            octopus.hasFlashed = true
                            giveEnergyToNeighbours(grid, row, column)
                            change = true
                        }
                    }
                }
            } while (change)

            for (row in 0 until grid.size) {
                for (column in 0 until grid[row].size) {
                    val octopus = grid[row][column]
                    if (octopus.hasFlashed){
                        octopus.hasFlashed = false
                        octopus.energyLevel = 0
                        numberOfFlashes++
                    }
                }
            }

        }

        return numberOfFlashes
    }


    fun partTwo(grid: List<List<Octopus>>): Int {
        var step = 1
        while (true){
            for (row in 0 until grid.size) {
                for (column in 0 until grid[row].size) {
                    grid[row][column].energyLevel += 1
                }
            }

            var change: Boolean

            do {
                change = false
                for (row in 0 until grid.size) {
                    for (column in 0 until grid[row].size) {
                        val octopus = grid[row][column]
                        if (octopus.energyLevel > 9 && octopus.hasFlashed.not()){
                            octopus.hasFlashed = true
                            giveEnergyToNeighbours(grid, row, column)
                            change = true
                        }
                    }
                }
            } while (change)

            var numberOfFlashes = 0
            for (row in 0 until grid.size) {
                for (column in 0 until grid[row].size) {
                    val octopus = grid[row][column]
                    if (octopus.hasFlashed){
                        octopus.hasFlashed = false
                        octopus.energyLevel = 0
                        numberOfFlashes++
                    }
                }
            }

            if (numberOfFlashes == 100) return step
            step++
        }
    }

}

fun main(){

    val dayEleven = Day11()

    val grid = readStrings().map { line -> line.toCharArray().map { c -> Octopus(c.toString().toInt(), false) } }

    val inputCopy = grid.map { row -> row.map { octopus -> Octopus(octopus.energyLevel, false) } }

    println(dayEleven.partOne(grid))
    println(dayEleven.partTwo(inputCopy))

}