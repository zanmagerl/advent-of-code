package twentytwo.days

import twentytwo.utilities.readStrings
import java.util.function.BiFunction
import kotlin.math.max

val visibleTrees = mutableSetOf<Pair<Int, Int>>()

private fun findVisibleTrees(firstRange: IntProgression, secondRange: IntProgression, tape: BiFunction<Int, Int, Int>, keyGenerator: BiFunction<Int, Int, Pair<Int, Int>>) {
    firstRange.forEach { y ->
        secondRange.fold(-1) { highestTree, x ->
            val treeHeight = tape.apply(y, x)
            if (treeHeight > highestTree) {
                visibleTrees.add(keyGenerator.apply(y, x))
            }
            max(treeHeight, highestTree)
        }
    }
}

private fun partOne(grid: List<List<Int>>): Int {
    val (height, width) = grid.size to grid[0].size
    // From left->right and right->left
    findVisibleTrees((0 until height), (0 until width), { y, x -> grid[y][x] }, { y, x -> y to x})
    findVisibleTrees((0 until height), (width-1 downTo  0), { y, x -> grid[y][x] }, { y, x -> y to x})
    // From up->down and down->up
    findVisibleTrees((0 until height), (0 until width), { x, y -> grid[y][x]}, { y, x -> x to y})
    findVisibleTrees((0 until height), (width-1 downTo  0), { x, y -> grid[y][x]}, { y, x -> x to y})
    return visibleTrees.size
}

private fun partTwo(grid: List<List<Int>>): Int {
    val (height, width) = grid.size to grid[0].size
    return (1 until height-1).fold(0) { maxScenicScore, y ->
        (1 until width-1).fold(maxScenicScore) { maxScenicScore, x ->
            listOf(
                    (y-1 downTo 0).map { grid[it][x] }, // north
                    (y+1 until height).map { grid[it][x] }, // south
                    (x+1 until width).map { grid[y][it] }, // east
                    (x-1 downTo 0).map { grid[y][it] } // west
            )
                    .map { direction -> direction.takeWhile { it < grid[y][x] }.run { if (this.size == direction.size) this.count() else this.count() + 1 } }
                    .reduce(Int::times)
                    .run { max(this, maxScenicScore) }
        }
    }
}

fun main(){

    val input = readStrings().map { it.toCharArray().map { it.digitToInt() } }

    println(partOne(input))
    println(partTwo(input))
}