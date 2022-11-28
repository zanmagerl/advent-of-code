package days

import utilities.readStrings


class Day06{

    fun partOne(fishes: List<Int>): Int {

        var iterList = fishes

        for (day in 0 until 80) {

            val newList = mutableListOf<Int>()

            for (fish in iterList) {
                if (fish == 0) {
                    newList.add(6)
                    newList.add(8)
                } else {
                    newList.add(fish-1)
                }
            }

            iterList = newList
        }

        return iterList.size
    }


    fun partTwo(fishes: List<Int>): Long {

        val iterMap = hashMapOf<Int, Long>()

        iterMap[0] = 0
        iterMap[6] = 0

        for (fish in fishes) {
            if (iterMap[fish] == null) {
                iterMap[fish] = 0
            }
            iterMap[fish] = iterMap[fish]!! + 1
        }

        for (day in 0 until 256) {
            val zeros = iterMap[0]!!
            for (i in 1..8) {

                if (iterMap.containsKey(i)) {
                    iterMap[i-1] = iterMap[i]!!
                } else {
                    iterMap[i-1] = 0
                }
            }
            iterMap[6] =  iterMap[6]!! + zeros
            iterMap[8] =  zeros
        }
        return iterMap.keys.fold(0) { acc, i ->  acc + iterMap[i]!!}
    }

}

fun main(){

    val daySix = Day06()

    val lines = readStrings()[0].split(",").map { a -> a.toInt() }

    println(daySix.partOne(lines))
    println(daySix.partTwo(lines))

}
