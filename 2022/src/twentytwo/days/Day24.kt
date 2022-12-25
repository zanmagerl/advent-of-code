package twentytwo.days

import twentytwo.utilities.readStrings
import java.util.PriorityQueue
import kotlin.math.abs

class Expedition (val x: Int, val y: Int, val pathLength: Int, val destination: Pair<Int, Int>, private val aStarValue: Int) : Comparable<Expedition> {

    companion object {
        private fun calculateAStarValue(x: Int, y: Int, destination: Pair<Int, Int>): Int {
            return  abs(x - destination.first) + abs(y - destination.second)
        }

        fun of(x: Int, y: Int, pathLength: Int, destination: Pair<Int, Int>): Expedition {
            return Expedition(x, y, pathLength, destination, pathLength + calculateAStarValue(x, y, destination))
        }
    }

    fun isAtDestination(): Boolean {
        return this.aStarValue == this.pathLength
    }

    override fun compareTo(other: Expedition): Int {
        return this.aStarValue.compareTo(other.aStarValue)
    }

    override fun equals(other: Any?): Boolean {
        if (other is Expedition) {
            return this.x == other.x && this.y == other.y && this.pathLength == other.pathLength
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = x
        result = 310 * result + y
        result = 310 * result + pathLength
        return result
    }

    override fun toString(): String {
        return "<x=$x,y=$y>: $pathLength"
    }
}

data class Wall(val x: Int, val y: Int, var oppositeWall: Wall?) {

    companion object {
        fun of(x: Int , y: Int): Wall {
            return Wall(x, y, null)
        }
    }

    fun getPositionNextToWall(blizzard: Blizzard): Blizzard {
        return when(blizzard.direction) {
            BlizzardDirection.LEFT -> Blizzard(oppositeWall!!.x - 1,  blizzard.y, blizzard.direction)
            BlizzardDirection.RIGHT -> Blizzard(oppositeWall!!.x + 1,  blizzard.y, blizzard.direction)
            BlizzardDirection.UP -> Blizzard(blizzard.x,  oppositeWall!!.y - 1, blizzard.direction)
            BlizzardDirection.DOWN -> Blizzard(blizzard.x,  oppositeWall!!.y + 1, blizzard.direction)
        }
    }

    override fun toString(): String {
        return "<x=$x,y=$y>"
    }
}

enum class BlizzardDirection(val char: Char) {
    UP('^'), RIGHT('>'), LEFT('<'), DOWN('v')
}
class Blizzard(val x: Int, val y: Int, val direction: BlizzardDirection) {

    companion object {
        fun of(x: Int, y: Int, direction: Char): Blizzard {
            return Blizzard(x, y, BlizzardDirection.values().first {it.char == direction})
        }
    }

    fun move(walls: List<Wall>): Blizzard {
        val nextPosition = nextPosition()
        if (walls.any { it.x == nextPosition.x && it.y == nextPosition.y }) {
            return walls.first { it.x == nextPosition.x && it.y == nextPosition.y }.getPositionNextToWall(nextPosition)
        }
        return nextPosition
    }

    private fun nextPosition(): Blizzard {
        return when(this.direction) {
            BlizzardDirection.UP -> Blizzard(this.x,  this.y - 1, this.direction)
            BlizzardDirection.DOWN -> Blizzard(this.x,  this.y + 1, this.direction)
            BlizzardDirection.LEFT -> Blizzard(this.x-1, this.y, this.direction)
            BlizzardDirection.RIGHT -> Blizzard(this.x+1,  this.y, this.direction)
        }
    }

    override fun toString(): String {
        return "<x=$x,y=$y> $direction"
    }
}

private fun aStarSearch(startPosition: Expedition, blizzards: List<Blizzard>, walls: List<Wall>): Int {
    val expeditions = PriorityQueue<Expedition>()
    expeditions.add(startPosition)
    val futureBlizzards = (0..1000).fold(mutableListOf(blizzards)) { acc, _ -> acc.add(acc.last().map { it.move(walls) }.toList())
        acc
    }
    val visited = mutableSetOf<Expedition>()
    while (expeditions.isNotEmpty()) {
        val expedition = expeditions.remove()

        if (visited.contains(expedition)) continue
        visited.add(expedition)

        if (expedition.isAtDestination()) {
            return expedition.pathLength
        }
        listOf(0 to 1, -1 to 0, 0 to -1, 1 to 0, 0 to 0)
                .map { Expedition.of(expedition.x + it.first, expedition.y + it.second, expedition.pathLength + 1, expedition.destination)  }
                .filter { exp -> futureBlizzards[expedition.pathLength+1].count { it.x == exp.x && it.y == exp.y } == 0 }
                .filter { exp -> walls.count { it.x == exp.x && it.y == exp.y } == 0 }
                .filter { it.x >= 0 && it.y >= 0 }
                .filter { !visited.contains(it) }
                .map { expeditions.add(it) }
    }
    return -1
}

private fun partOne(blizzards: List<Blizzard>, walls: List<Wall>): Int {
    val beginning = 1 to 0
    val destination = 120 to 26
    return aStarSearch(Expedition.of(beginning.first, beginning.second, 0, destination), blizzards, walls)
}

private fun partTwo(blizzards: List<Blizzard>, walls: List<Wall>): Int {
    val beginning = 1 to 0
    val destination = 120 to 26
    val pathToExit = aStarSearch(Expedition.of(beginning.first, beginning.second, 0, destination), blizzards, walls)
    val pathToSnacks = aStarSearch(Expedition.of(destination.first, destination.second, pathToExit, beginning), blizzards, walls)
    return aStarSearch(Expedition.of(beginning.first, beginning.second, pathToSnacks, destination), blizzards, walls)
}

fun main(){

    val blizzards = mutableListOf<Blizzard>()
    val walls = mutableListOf<Wall>()
    readStrings().mapIndexed { y, line -> line.toCharArray().toList().mapIndexed { x, char ->
        when (char) {
            '#' -> walls.add(Wall.of(x, y))
            '>', '<', '^', 'v' -> blizzards.add(Blizzard.of(x, y, char))
            else -> {}
        }
    } }

    walls.map {wall ->
        when {
            wall.x == 0 -> wall.oppositeWall = walls.filter { it.y == wall.y }.firstOrNull { it.x != 0 }
            wall.y == 0 -> wall.oppositeWall = walls.filter { it.x == wall.x }.firstOrNull{ it.y != 0 }
            wall.x > 0 && wall.y > 0 -> wall.oppositeWall = if (wall.x == walls.maxOf { it.x }) {
                walls.filter { it.y == wall.y }.firstOrNull { it.x == 0 }
            } else {
                walls.filter { it.x == wall.x }.firstOrNull { it.y == 0 }
            }
        }
    }
    println(partOne(blizzards, walls))
    println(partTwo(blizzards, walls))
}