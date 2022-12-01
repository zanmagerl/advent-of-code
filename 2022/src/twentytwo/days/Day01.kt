package twentytwo.days

import twentytwo.utilities.readStrings

class Day01 {

    fun partOne(elvesBackpack: List<List<Int>>): Int {
        return elvesBackpack.maxOf { it.sum() }
    }

    fun partTwo(elvesBackpack: List<List<Int>>): Int {
        return elvesBackpack.map { it.sum() }.sortedDescending().take(3).sum()
    }
}

fun main(){

    val dayOne = Day01()

    val elvesBackpacks = readStrings().fold(mutableListOf<MutableList<Int>>(mutableListOf())) { acc, line ->
        if (line.isBlank()) {
            acc.add(mutableListOf())
        } else {
            acc.last().add(line.toInt())
        }
        acc
    }.map { it.toList() }.toList()

    println(dayOne.partOne(elvesBackpacks))
    println(dayOne.partTwo(elvesBackpacks))
}