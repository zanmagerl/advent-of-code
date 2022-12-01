package twentytwo.days

import twentytwo.utilities.readStrings

class Day01 {

    fun partOne(elvesBackpack: List<List<Int>>): Int {
        return elvesBackpack.maxOf { it.sum() }
    }

    fun partTwo(elvesBackpack: List<List<Int>>): Int {
        return elvesBackpack.map { it.sum() }.sortedDescending().subList(0, 3).sum()
    }
}

fun main(){

    val dayOne = Day01()

    val input = readStrings()
    val elvesBackpacks = mutableListOf<List<Int>>()
    var currentBackpack = mutableListOf<Int>()
    for (sandwich in input) {
        if (sandwich.isBlank()) {
            elvesBackpacks.add(currentBackpack)
            currentBackpack = mutableListOf()
        } else {
            currentBackpack.add(sandwich.toInt())
        }
    }

    println(dayOne.partOne(elvesBackpacks))
    println(dayOne.partTwo(elvesBackpacks))
}