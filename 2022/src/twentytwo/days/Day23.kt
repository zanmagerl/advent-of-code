package twentytwo.days

import twentytwo.utilities.readStrings
import kotlin.math.abs

private data class ElfPosition(val x: Int, val y: Int) {
    fun isAlone(map: Map<ElfPosition, Boolean>): Boolean {
        return map.keys.count { abs(this.x - it.x) + abs(this.y - it.y) == 1 || (abs(this.x - it.x) == 1 && abs(this.y - it.y) == 1) } == 0
    }
}

private fun calculateEmptySpaces(map: Map<ElfPosition, Boolean>): Int {
    return (map.keys.maxOf { it.x } - map.keys.minOf { it.x } + 1) * (map.keys.maxOf { it.y } - map.keys.minOf { it.y } + 1) - map.keys.size
}

private fun partOne(map: MutableMap<ElfPosition, Boolean>, directions: List<(ElfPosition, Map<ElfPosition, Boolean>) -> ElfPosition?>): Int {
    (0 until 10).map { step ->
        val propositions = mutableMapOf<ElfPosition, MutableList<ElfPosition>>()
        map.keys.map { elf ->
            if (!elf.isAlone(map)){
                (step until (step + 4))
                    .mapNotNull { directions[it % 4].invoke(elf, map) }
                    .firstOrNull()
                    ?.let { propositions[it] = propositions.getOrDefault(it, mutableListOf()).also { it.add(elf) } }
            }
        }
        propositions
            .filter {it.value.size == 1}
            .map {
                map[it.key] = true
                map.remove(it.value.first())
            }
    }

    return calculateEmptySpaces(map)
}

private fun partTwo(map: MutableMap<ElfPosition, Boolean>, directions: List<(ElfPosition, Map<ElfPosition, Boolean>) -> ElfPosition?>): Int {
    (0 until 10000).map { step ->
        val propositions = mutableMapOf<ElfPosition, MutableList<ElfPosition>>()
        map.keys.map { elf ->
            if (!elf.isAlone(map)){
                (step until (step + 4))
                    .mapNotNull { directions[it % 4].invoke(elf, map) }
                    .firstOrNull()
                    ?.let { propositions[it] = propositions.getOrDefault(it, mutableListOf()).also { it.add(elf) } }
            }
        }
        if (propositions.isEmpty()) return step+1
        propositions
            .filter {it.value.size == 1}
            .map {
                map[it.key] = true
                map.remove(it.value.first())
            }
    }
    return -1
}

fun main(){

    val elves = mutableMapOf<ElfPosition, Boolean>()

    readStrings()
        .mapIndexed { row, line -> line.toCharArray().toList().mapIndexed { column, char -> if (char == '#') elves[ElfPosition(column, row)] = true} }

    val directions = listOf<(ElfPosition, Map<ElfPosition, Boolean>) -> ElfPosition?>(
        { elf, map  -> if (!map.containsKey(elf.copy(x = elf.x-1, y = elf.y-1)) && !map.containsKey(elf.copy(y = elf.y-1)) && !map.containsKey(elf.copy(x = elf.x+1, y = elf.y-1))) elf.copy(y = elf.y-1) else null },
        { elf, map -> if (!map.containsKey(elf.copy(x = elf.x-1, y = elf.y+1)) && !map.containsKey(elf.copy(y = elf.y+1)) && !map.containsKey(elf.copy(x = elf.x+1, y = elf.y+1))) elf.copy(y = elf.y+1) else null },
        { elf, map -> if (!map.containsKey(elf.copy(x = elf.x-1, y = elf.y+1)) && !map.containsKey(elf.copy(x = elf.x-1)) && !map.containsKey(elf.copy(x = elf.x-1, y = elf.y-1))) elf.copy(x = elf.x-1) else null },
        { elf, map -> if (!map.containsKey(elf.copy(x = elf.x+1, y = elf.y+1)) && !map.containsKey(elf.copy(x = elf.x+1)) && !map.containsKey(elf.copy(x = elf.x+1, y = elf.y-1))) elf.copy(x = elf.x+1) else null },
    )
    println(partOne(elves.toMutableMap(), directions))
    println(partTwo(elves, directions))
}