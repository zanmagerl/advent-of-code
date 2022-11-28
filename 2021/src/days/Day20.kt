package days

import utilities.readStrings

data class Location(val x: Int, val y: Int)

class Day20 {

    fun getIndex(x: Int, y: Int, locations: Map<Location, Int>, defaultValue: Int): Int {
        var result = ""
        result += locations.getOrDefault(Location(x-1, y-1), defaultValue)
        result += locations.getOrDefault(Location(x, y-1), defaultValue)
        result += locations.getOrDefault(Location(x+1, y-1), defaultValue)
        result += locations.getOrDefault(Location(x-1, y), defaultValue)
        result += locations.getOrDefault(Location(x, y), defaultValue)
        result += locations.getOrDefault(Location(x+1, y), defaultValue)
        result += locations.getOrDefault(Location(x-1, y+1), defaultValue)
        result += locations.getOrDefault(Location(x, y+1), defaultValue)
        result += locations.getOrDefault(Location(x+1, y+1), defaultValue)

        return result.toInt(2)
    }

    fun partOne(inputEnhancementAlgorithm: List<Int>, locations: MutableMap<Location, Int>): Int {

        var currentLocations = locations.toMap()
        val nextLocations = mutableMapOf<Location, Int>()

        for (i in 0..49) {

            val minX = currentLocations.minOf { it.key.x }
            val minY = currentLocations.minOf { it.key.y }
            val maxX = currentLocations.maxOf { it.key.x }
            val maxY = currentLocations.maxOf { it.key.y }
            for (x in minX-1..maxX+1){
                for (y in minY-1..maxY+1) {
                    val index = getIndex(x, y, currentLocations, i % 2)
                    nextLocations[Location(x,y)] = inputEnhancementAlgorithm[index]
                }
            }
           currentLocations = nextLocations.toMap()
           nextLocations.clear()
        }

        return currentLocations.count { it.value == 1 }
    }

    fun partTwo(input: List<Int>): Int {
        return input.windowed(4).count { it.last() > it.first() }
    }
}

fun main(){

    val day20 = Day20()

    val input = readStrings()
    val imageEnhancementAlgorithm = input[0].map { if (it == '#') 1 else 0 }
    val locations = mutableMapOf<Location, Int>()
    val imageSection = input.subList(2, input.size)
    for (row in imageSection.indices) {
        val line = imageSection[row].toCharArray()
        for (column in line.indices) {
            locations[Location(column, row)] = if (line[column] == '#') 1 else 0
        }
    }

    println(day20.partOne(imageEnhancementAlgorithm, locations))
}