package twentytwo.days

import twentytwo.utilities.readStrings
import kotlin.math.max
import kotlin.math.min

data class CavePoint(val x: Int, val y: Int) {
    companion object {
        fun of(slice: String): CavePoint {
            return slice.split(",").map { it.toInt() }.let { CavePoint(it.first(), it.last()) }
        }
    }

    fun between(cavePoint: CavePoint): List<CavePoint> {
        return (min(this.x, cavePoint.x)..max(this.x, cavePoint.x)).flatMap { x -> (min(this.y, cavePoint.y)..max(this.y, cavePoint.y)).map { y -> CavePoint(x, y) } }
    }

    fun generateNextPoints(): List<CavePoint> {
        return listOf(CavePoint(this.x, this.y+1), CavePoint(this.x-1, this.y+1), CavePoint(this.x+1, this.y+1))
    }

}
enum class PointType {
    ROCK,
    SAND,
}

fun putAtRest(map: MutableMap<CavePoint, PointType>, cavePoint: CavePoint, limit: Int, limitHandler: (cavePoint: CavePoint?) -> CavePoint? ): CavePoint? {
    return cavePoint.generateNextPoints()
            .firstOrNull { !map.containsKey(it) }
            ?.run { if (this.y < limit) return putAtRest(map, this, limit, limitHandler) else return limitHandler.invoke(cavePoint) }
            ?: cavePoint
}

private fun partOne(map: MutableMap<CavePoint, PointType>): Int {
    val startPoint = CavePoint(500, 0)
    val limit = map.keys.maxOf { it.y }

    return generateSequence(0) { it }.takeWhile {
        val rock = putAtRest(map, startPoint, limit) { null }
        if (rock != null) {
            map[rock] = PointType.SAND
        }
        rock != null
    }.sumOf {  1 as Int  }
}

private fun partTwo(map: MutableMap<CavePoint, PointType>): Int {
    val startPoint = CavePoint(500, 0)
    val limit = map.keys.maxOf { it.y } + 2

    return generateSequence(0) { it }.takeWhile {
        val rock = putAtRest(map, startPoint, limit) { rock -> rock }
        if (rock != null) {
            map[rock] = PointType.SAND
        }
        rock != startPoint
    }.sumOf {  1 as Int  } + 1
}
fun main(){

    val cave = mutableMapOf<CavePoint, PointType>()

    readStrings().map {
        it.split("->")
                .map { it.trim() }
                .map { CavePoint.of(it) }
                .reduce { previousCavePoint, cavePoint -> previousCavePoint.between(cavePoint).map { cave[it] = PointType.ROCK }.let { cavePoint } }
    }
    println(partOne(cave.toMutableMap()))
    println(partTwo(cave))
}