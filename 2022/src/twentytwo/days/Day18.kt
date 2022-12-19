package twentytwo.days

import twentytwo.utilities.readStrings
import kotlin.math.abs

data class Droplet(val x: Int, val y: Int, val z: Int) {
    fun doesTouch(other: Droplet): Boolean {
        if (abs(this.x - other.x) + abs(this.y - other.y) + abs(this.z - other.z) == 1) {
            return true
        }
        return false
    }

    fun generateNeighbours(): List<Droplet> {
        return listOf(this.copy(x=x-1), this.copy(x=x+1), this.copy(y=y-1), this.copy(y=y+1), this.copy(z=z-1), this.copy(z=z+1))
    }
}

private fun partOne(droplets: List<Droplet>): Int {
    return droplets.sumOf { first ->
        6 - droplets.fold(0) { touchingSides, second ->
            if (first.doesTouch(second)) {
                touchingSides + 1
            } else {
                touchingSides
            }
        }
    }
}

fun isAirPocket(droplets: Map<Droplet, Boolean>, cube: Droplet, limit: Int): Pair<Boolean, Map<Droplet, Boolean>> {
    if (cube.x > limit || cube.y > limit || cube.z > limit || cube.x < 0 || cube.y < 0 || cube.z < 0) {
        return false to droplets
    }
    val neighbors = cube.generateNeighbours()
    var searchedDroplets = droplets
    neighbors.map { neighbor ->
        if (!droplets.contains(neighbor)) {
            val (isAir, nextDroplets) = isAirPocket(searchedDroplets.plus(cube to true), neighbor, limit)
            if (!isAir) {
                return false to droplets
            }
            // If these cubes are not air pocket add them to known cubes
            searchedDroplets = searchedDroplets.plus(nextDroplets)
        }
    }
    // Air could not escape outside
    return true to searchedDroplets
}

private fun partTwo(droplets: List<Droplet>): Int {
    val limit = listOf(droplets.maxOf { it.x }, droplets.maxOf { it.y }, droplets.maxOf { it.z }).maxOf {it} + 1
    var allDroplets = droplets.toMutableList()
    return droplets.sumOf { droplet ->
        var surfaceSides = 6
        val neighbours = droplet.generateNeighbours()
        surfaceSides -= neighbours.count { droplets.contains(it) }
        surfaceSides -= neighbours.filter { !droplets.contains(it) }.count{
            val (isAir, searchedDroplets) = isAirPocket(allDroplets.associateWith { true }, it, limit)
            allDroplets = allDroplets.plus(searchedDroplets.keys).toSet().toMutableList()
            isAir
        }
        surfaceSides
    }
}

fun main(){
    val input = readStrings().map { it.split(",").map { it.toInt() }.let { Droplet(it[0], it[1], it[2]) } }

    println(partOne(input))
    println(partTwo(input))
}