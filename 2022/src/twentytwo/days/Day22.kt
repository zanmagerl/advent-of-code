package twentytwo.days

import twentytwo.utilities.readStrings

enum class TileType {
    WALL,
    OPEN,
}

data class Tile(val x: Int, val y: Int, val tileType: TileType, val neighbours: MutableMap<Char, Pair<Tile, Char>>) {

    companion object {
        fun of(x: Int, y: Int, char: Char): Tile {
            val tileType = if (char == '#') TileType.WALL else TileType.OPEN
            return Tile(x, y, tileType, mutableMapOf())
        }
    }

    fun move(facing: Char): Pair<Tile, Char> {
        if (neighbours[facing]!!.first.tileType != TileType.WALL) {
            return neighbours[facing]!!
        }
        return this to facing
    }

    override fun toString(): String {
        return "<y=$y,x=$x>: $tileType"
    }
}

private fun rotate(facing: Char, rotation: String): Char {
    return when (facing) {
        '^' -> if (rotation == "L") '<' else '>'
        '>' -> if (rotation == "L") '^' else 'v'
        'v' -> if (rotation == "L") '>' else '<'
        '<' -> if (rotation == "L") 'v' else '^'
        else -> throw IllegalArgumentException("Invalid facing!")
    }
}

private fun facingNumber(facing: Char): Int {
    return when(facing) {
        '>' -> 0
        'v' -> 1
        '<' -> 2
        '^' -> 3
        else -> throw IllegalArgumentException("Invalid facing!")
    }
}

private fun computePassword(destination: Tile, facing: Char): Int {
    return 1000 * (destination.y + 1) + 4 * (destination.x + 1) + facingNumber(facing)
}

/**
 * Cube has 6 surfaces in following order:
 *    0 1
 *    2
 *  3 4
 *  5
 */
private fun computeSurfaceNumber(tile: Tile): Int {
    return when {
        tile.x < 50 -> if (tile.y < 150) 3 else 5
        tile.y < 50 -> if (tile.x < 100) 0 else 1
        tile.y < 100 ->  2
        else -> 4
    }
}

/**
 * I manually visualized and connected different cube's surfaces
 */
private fun findNeighbourOnCubeSurface(direction: Char, tile: Tile, tiles: List<Tile>): Pair<Tile, Char> {
    return when (computeSurfaceNumber(tile)) {
        0 -> if (direction == '^') tiles.find { it.x == 0 && it.y == (150 + tile.x % 50) }!! to '>' else tiles.find { it.x == 0 && it.y == (100 + 50 - (tile.y % 50) -1 )}!! to '>'
        1 -> when (direction) {
            '^' -> tiles.find { it.y == 199 && it.x == (tile.x - 100) }!! to '^'
            'v' -> tiles.find { it.x == 99 && it.y == (50 + (tile.x % 50)) }!! to '<'
            else -> tiles.find { it.x == 99 && it.y == (100 + 50 - (tile.y % 50) - 1) }!! to '<'
        }
        2 -> if (direction == '<') tiles.find { it.y == 100 && it.x == (tile.y % 50) }!! to 'v' else tiles.find { it.y == 49 && it.x == (100 + (tile.y % 50)) }!! to '^'
        3 -> if (direction == '^') tiles.find { it.x == 50 && it.y == (50 + (tile.x % 50)) }!! to '>' else tiles.find { it.x == 50 && it.y == (50 - (tile.y % 50) - 1) }!! to '>'
        4 -> if (direction == '>') tiles.find { it.x == 149 && it.y == (50 - (tile.y % 50) - 1)}!! to '<' else tiles.find { it.x == 49 && it.y == (150 + (tile.x % 50)) }!! to '<'
        5 -> when (direction) {
            '<' -> tiles.find { it.y == 0 && it.x == (50 + (tile.y % 50)) }!! to 'v'
            'v' -> tiles.find { it.y == 0 && it.x == (100 + (tile.x % 50)) }!! to 'v'
            else -> tiles.find { it.y == 149 && it.x == (50 + (tile.y % 50)) }!! to '^'
        }
        else -> throw IllegalArgumentException("Wrong surface number!")
    }
}


private fun partOne(tiles: List<Tile>, pathDescription: List<String>): Int {
    tiles.map { tile ->
        val north = '^' to (tiles.firstOrNull { it.x == tile.x && it.y == tile.y - 1 } ?: tiles.filter { it.x == tile.x }.maxByOrNull { it.y }!!)
        val south = 'v' to (tiles.firstOrNull { it.x == tile.x && it.y == tile.y + 1 } ?: tiles.filter { it.x == tile.x }.minByOrNull { it.y }!!)
        val east = '>' to (tiles.firstOrNull { it.x == tile.x + 1 && it.y == tile.y } ?: tiles.filter { it.y == tile.y }.minByOrNull { it.x }!!)
        val west = '<' to (tiles.firstOrNull { it.x == tile.x - 1 && it.y == tile.y } ?: tiles.filter { it.y == tile.y }.maxByOrNull { it.x }!!)
        listOf(north, south, east, west).map { tile.neighbours[it.first] = it.second to it.first }
    }
    val minY = tiles.minOf { it.y }
    val tile = tiles.filter { it.y == minY }.minByOrNull { it.x }!!

    pathDescription.fold('>' to tile) { position, instruction ->
        if (instruction.length == 1 && instruction.toCharArray().first().isLetter()) {
            // we turn
            rotate(position.first, instruction) to position.second
        } else {
            // we move
            position.first to (0 until instruction.toInt()).fold(position.second) {tile, _ -> tile.move(position.first).first}
        }
    }.let { return computePassword(it.second, it.first) }
}

private fun partTwo(tiles: List<Tile>, pathDescription: List<String>): Int {
    // Compute neighbours
    tiles.map { tile ->
        val north = '^' to if (tiles.count { it.x == tile.x && it.y == tile.y - 1 } == 1) {
            tiles.find { it.x == tile.x && it.y == tile.y - 1 } to '^'
        } else {
            findNeighbourOnCubeSurface('^', tile, tiles)
        }
        val south = 'v' to if (tiles.count { it.x == tile.x && it.y == tile.y + 1 } == 1) {
            tiles.find { it.x == tile.x && it.y == tile.y + 1 } to 'v'
        } else {
            findNeighbourOnCubeSurface('v', tile, tiles)
        }
        val east = '>' to if (tiles.count { it.x == tile.x + 1 && it.y == tile.y } == 1) {
            tiles.find { it.x == tile.x + 1 && it.y == tile.y } to '>'
        } else {
            findNeighbourOnCubeSurface('>', tile, tiles)
        }
        val west = '<' to if (tiles.count { it.x == tile.x - 1 && it.y == tile.y } == 1) {
            tiles.find { it.x == tile.x - 1 && it.y == tile.y } to '<'
        } else {
            findNeighbourOnCubeSurface('<', tile, tiles)
        }
        listOf(north, south, east, west).map { tile.neighbours[it.first] = it.second.first!! to it.second.second  }
    }

    val minY = tiles.minOf { it.y }
    val tile = tiles.filter { it.y == minY }.minByOrNull { it.x }!!

    pathDescription.fold('>' to tile) { position, instruction ->
        if (instruction.length == 1 && instruction.toCharArray().first().isLetter()) {
            // we turn
            rotate(position.first, instruction) to position.second
        } else {
            // we move
            (0 until instruction.toInt()).fold(position) { p, _ ->
                val newPosition = p.second.move(p.first)
                newPosition.second to newPosition.first
            }
        }
    }.let { return computePassword(it.second, it.first) }
}

fun main(){

    val input = readStrings()

    val tiles = input
        .takeWhile { it.isNotBlank() }
        .flatMapIndexed{ row, line ->
            line.toCharArray()
                .foldIndexed(mutableListOf<Tile>()) { column, tiles, char ->
                    if (!char.isWhitespace()) {
                        tiles.add(Tile.of(column, row, char))
                    }
                    tiles
                }
        }

    val pathDescription = input.takeLast(1).first().toCharArray().map { if (it.isLetter()) ",$it," else "$it" }.joinToString(separator = "").split(",")

    println(partOne(tiles, pathDescription))
    println(partTwo(tiles, pathDescription))
}