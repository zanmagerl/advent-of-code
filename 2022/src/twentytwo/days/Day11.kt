package twentytwo.days

import twentytwo.utilities.readStrings

data class Monkey(
    val id: Int,
    val items: MutableList<Long> = mutableListOf(),
    val operation: (Long) -> Long,
    val divisor: Long,
    val branches: Pair<Int, Int>,
    var numInspections: Int = 0
) {

    companion object {
        fun of(id: Int, lines: List<String>): Monkey {
            val operation = { old: Long ->
                val operators = lines[2].split("=").last().trim().split(" ").map { if (it == "old") "$old" else it }
                if (operators[1] == "*") {
                    operators.first().toLong() * operators.last().toLong()
                } else {
                    operators.first().toLong() + operators.last().toLong()
                }
            }
            return Monkey(
                id,
                lines[1].split(":").last().split(",").map { it.trim().toLong() }.toMutableList(),
                operation,
                lines[3].split(" ").last().toLong(),
                lines[4].split(" ").last().toInt() to lines[5].split(" ").last().toInt()
            )
        }
    }

    fun inspectItems(worryManager: (Long) -> Long): MutableList<Long> {
        return items
            .map {
                numInspections += 1
                worryManager(operation(it))
            }
            .toMutableList()
    }

    fun catchItem(item: Long) {
        items.add(item)
    }

    fun chooseMonkey(item: Long): Int {
        return if (item % divisor == 0L) branches.first else branches.second
    }
}

private fun findMonkey(monkeys: List<Monkey>, id: Int): Monkey {
    return monkeys.first { it.id == id }
}

private fun keepAway(monkeys: List<Monkey>, rounds: Int, worryManager: (Long) -> Long): Long {
    return repeat(rounds) {
        monkeys.map { monkey ->
            monkey
                .inspectItems(worryManager)
                .map { findMonkey(monkeys, monkey.chooseMonkey(it)).catchItem(it) }
                .map { monkey.items.removeFirst() }
        }
    }.run { monkeys.map { it.numInspections.toLong() }.sortedDescending().take(2).reduce(Long::times) }
}

private fun partOne(monkeys: List<Monkey>): Long {
    return keepAway(monkeys, 20) { item -> item / 3 }
}

private fun partTwo(monkeys: List<Monkey>): Long {
    val modulo = monkeys.map { it.divisor }.reduce(Long::times) // Divisors are primes so their product is equal to their LCM
    return keepAway(monkeys, 10000) { item -> item % modulo }
}

fun main(){

    val input = readStrings().fold(mutableListOf(mutableListOf<String>())) { monkeys, line -> if (line.isBlank()) monkeys.add(mutableListOf()) else
        monkeys.last().add(line.trim())
        monkeys
    }.mapIndexed { index, lines -> Monkey.of(index, lines) }

    println(partOne(input.map { it.copy(items = it.items.toMutableList()) }))
    println(partTwo(input))
}