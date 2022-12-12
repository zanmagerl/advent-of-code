package twentytwo.days

import twentytwo.utilities.readStrings

data class Hill(val x: Int, val y: Int, val pathLength: Int = 0) {
    override fun equals(other: Any?): Boolean {
        if (other is Hill) return this.x == other.x && this.y == other.y
        return super.equals(other)
    }
}

private fun findChar(grid: List<List<Char>>, searched: Char): Hill {
    grid.mapIndexed { row, chars -> chars.mapIndexed {column, char -> if (char == searched) return Hill(column, row) } }
    throw IllegalArgumentException("Wrong grid!")
}

private fun isInMap(hill: Hill, map: List<List<Char>>): Boolean {
    return hill.y in map.indices && hill.x in map[0].indices
}

private fun measureHeight(hill: Hill, map: List<List<Char>>): Int {
    return when (val elevation = map[hill.y][hill.x]) {
        'S' -> 'a'.code
        'E' -> 'z'.code
        else -> elevation.code
    } - 'a'.code
}

private fun breadthFirstSearch(startHill: Hill, isAtDestination: (Hill) -> Boolean, isValidRoute: (Hill, Hill) -> Boolean): Int {
    val hills = ArrayDeque<Hill>()
    val visited = mutableSetOf<Hill>()
    hills.add(startHill)
    while (hills.isNotEmpty()) {

        val hill = hills.removeFirst()
        if (visited.contains(hill)) continue

        if (isAtDestination(hill)) {
            return hill.pathLength
        }

        listOf(0 to 1, -1 to 0, 0 to -1, 1 to 0)
                .map { Hill(hill.x + it.first, hill.y + it.second, hill.pathLength + 1)  }
                .filter { isValidRoute(hill, it) }
                .map { hills.add(it) }

        visited.add(hill)
    }
    return -1
}

private fun partOne(map: List<List<Char>>): Int {
    val startPoint = findChar(map, 'S')
    val endPoint = findChar(map, 'E')
    return breadthFirstSearch(
            startPoint,
            isAtDestination = { hill -> hill == endPoint},
            isValidRoute = { previousHill, currentHill -> isInMap(currentHill, map) && (measureHeight(currentHill, map) - measureHeight(previousHill, map) <= 1) }
    )
}

/**
 * Same as part 1, but we start at the end and search for the hill with elevation equal to 0
 */
private fun partTwo(map: List<List<Char>>): Int {
    val startPoint = findChar(map, 'E')
    return breadthFirstSearch(
            startPoint,
            isAtDestination = { hill -> measureHeight(hill, map) == 0 },
            isValidRoute = { previousHill, currentHill -> isInMap(currentHill, map) && (measureHeight(previousHill, map) - measureHeight(currentHill, map) <= 1) }
    )
}

fun main(){

    val input = readStrings().map { it.toCharArray().toList() }

    println(partOne(input))
    println(partTwo(input))
}