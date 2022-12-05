package twentytwo.days

import twentytwo.utilities.readStrings

data class Step(val quantity: Int, val source: Int, val target: Int) {

    companion object {
        fun of(line: String): Step {
            return line.split(" ").filterIndexed{ index, _ -> index % 2 == 1}.map { it.toInt() }.let { Step(it[0], it[1]-1, it[2]-1) }
        }
    }
}

private fun partOne(stacks: List<ArrayDeque<Char>>, steps: List<Step>): String {
    steps.map {step -> repeat(step.quantity) { stacks[step.target].addFirst(stacks[step.source].removeFirst()) } }
    return stacks.map { it.first() }.joinToString(separator = "")
}

private fun partTwo(stacks: List<ArrayDeque<Char>>, steps: List<Step>): String {
    steps.map {step ->
        stacks[step.source].subList(0, step.quantity).asReversed()
                .map { stacks[step.target].addFirst(it) }
                .map { stacks[step.source].removeFirst() }
    }
    return stacks.map { it.first() }.joinToString(separator = "")
}

fun main(){

    val input = readStrings()

    val numberOfStacks = input.dropWhile { it.contains("[") }.first().split(" ").filter { it.isNotBlank() }.maxOf { it.toInt() }

    val stacks = List(numberOfStacks) {ArrayDeque<Char>()}
    val steps = mutableListOf<Step>()

    input.filter { it.contains("[") }
            .map { line ->  line
                    .mapIndexed{index, char -> if (char.isLetter()) stacks[index / 4].addLast(line[index])}
            }

    input.filter { it.contains("move") }.map { steps.add(Step.of(it)) }

    println(partOne(stacks.map { ArrayDeque(it) }.toList(), steps))
    println(partTwo(stacks, steps))
}