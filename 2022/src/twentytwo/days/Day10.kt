package twentytwo.days

import twentytwo.utilities.readStrings

abstract class Instruction(open val duration: Int, open val value: Int)
data class Add(override val duration: Int, override val value: Int): Instruction(duration, value)
data class Noop(override val duration: Int) : Instruction(duration, 0)

data class SignalPair(val cycle: Int, val registerXValue: Int)

private fun calculateSignalPairs(instructions: List<Instruction>): List<SignalPair> {
    return instructions
        .fold(mutableListOf(SignalPair(1, 1))) { steps, instruction ->
            steps.add(SignalPair((steps.last().cycle + instruction.duration), (steps.last().registerXValue + instruction.value)))
            steps
        }
        .toList()
}

private fun partOne(instructions: List<Instruction>): Int {
    val cycles = listOf(20, 60, 100, 140, 180, 220)

    return calculateSignalPairs(instructions)
        .filter { it.cycle in cycles || it.cycle + 1 in cycles }
        .map { if (it.cycle % 20 != 0) SignalPair(it.cycle+1, it.registerXValue) else it }
        .toSet()
        .sumOf { it.cycle * it.registerXValue }
}

private fun partTwo(instructions: List<Instruction>) {
    val signalPairs = calculateSignalPairs(instructions)
    (0..239)
        .map { position -> if (signalPairs.last { it.cycle == position || it.cycle - 1 == position }.registerXValue in (position % 40 - 1)..(position % 40 + 1)) "#" else " " }
        .foldIndexed(mutableListOf(mutableListOf<String>())) {index, rows, s -> if (index % 40 == 0) rows.add(mutableListOf(s)) else rows.last().add(s)
            rows
        }
        .map { println(it.joinToString(separator = "")) }
}

fun main(){

    val input = readStrings().map { if (it.startsWith("add")) Add(2, it.split(" ").last().toInt()) else Noop(1) }

    println(partOne(input))
    partTwo(input)
}