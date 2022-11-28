package days

import utilities.readStrings

class Day13 {

    private fun prettyPrint(coordinates: MutableSet<Pair<Int, Int>>){

        println(coordinates)

        val maxX = coordinates.maxOf { c -> c.first }
        val maxY = coordinates.maxOf { c -> c.second }

        val grid = Array(maxY+1) { Array (maxX+1) {" "}}

        for (coordinate in coordinates){
            grid[coordinate.second][coordinate.first] = "#"
        }

        for (row in 0..maxY) {
            for (col in 0..maxX ){
                print(grid[row][col])
            }
            println()
        }

    }

    fun partOne(coordinates: MutableSet<Pair<Int, Int>>, fold: Pair<String, Int>): Int {

        val newCoordinates = mutableSetOf<Pair<Int, Int>>()

        if (fold.first == "x") {

            val maxX = coordinates.maxOf { c -> c.first }

            for (i in coordinates.indices){
                val coordinate = coordinates.elementAt(i)
                if (coordinate.first > maxX / 2){
                    newCoordinates.add(coordinate.copy(first = maxX/2 - (coordinate.first - maxX/2)))
                } else {
                    newCoordinates.add(coordinate)
                }
            }

        } else {
            val maxY = coordinates.maxOf { c -> c.second }

            for (i in coordinates.indices){
                val coordinate = coordinates.elementAt(i)
                if (coordinate.second >= maxY / 2){
                    newCoordinates.add(coordinate.copy(second = maxY/2 - (coordinate.second - maxY/2)))
                } else {
                    newCoordinates.add(coordinate)
                }
            }
        }

        return newCoordinates.size
    }


    fun partTwo(coordinates: MutableSet<Pair<Int, Int>>, folds: List<Pair<String, Int>>) {

        var iterCoordinates = coordinates

        for (fold in folds){

            val newCoordinates = mutableSetOf<Pair<Int, Int>>()

            if (fold.first == "x") {

                val maxX = iterCoordinates.maxOf { c -> c.first }

                println("${maxX/2}, ${fold.second}")

                for (i in iterCoordinates.indices){
                    val coordinate = iterCoordinates.elementAt(i)
                    if (coordinate.first >= fold.second){
                        newCoordinates.add(coordinate.copy(first = fold.second - (coordinate.first - fold.second)))
                    } else {
                        newCoordinates.add(coordinate)
                    }
                }

            } else {
                val maxY = iterCoordinates.maxOf { c -> c.second }

                for (i in iterCoordinates.indices){
                    val coordinate = iterCoordinates.elementAt(i)
                    if (coordinate.second > fold.second){
                        newCoordinates.add(coordinate.copy(second = fold.second - (coordinate.second - fold.second)))
                    } else {
                        newCoordinates.add(coordinate)
                    }
                }
            }

            iterCoordinates = newCoordinates

        }

        prettyPrint(iterCoordinates)

    }

}

fun main() {

    val dayThirteen = Day13()

    val lines = readStrings()

    val coordinates = mutableSetOf<Pair<Int, Int>>()
    val folds = mutableListOf<Pair<String, Int>>()

    var coordinatePhase = true
    for (line in lines) {
        if (line == ""){
            coordinatePhase = false
            continue
        }
        if (coordinatePhase){
            coordinates.add(Pair(line.split(",")[0].toInt(), line.split(",")[1].toInt()))
        } else {
            val lastPart = line.split(" ").last().split("=")
            folds.add(Pair(lastPart.first(), lastPart.last().toInt()))
        }
    }

    println(dayThirteen.partTwo(coordinates, folds))
}