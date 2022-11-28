package days

import utilities.readStrings

class Day14 {

    fun partOne(polymerTemplate: String, pairInsertionRules: Map<String, String>): Int {

        var template = polymerTemplate
        for (i in 1..10) {
            val possiblePairs = template.zipWithNext()
            template = possiblePairs.map { el -> "${el.first}${pairInsertionRules["${el.first}${el.second}"]}"}.toList().joinToString(separator = "", postfix=possiblePairs.last().second.toString())
        }

        val counts = template.toList().groupingBy { it }.eachCount()
        return counts.maxByOrNull { it.value }!!.value - counts.minByOrNull { it.value }!!.value
    }

    fun partTwo(polymerTemplate: String, pairInsertionRules: Map<String, String>): Long {

        var occurrences = mutableMapOf<String, Long>()
        val doubles = polymerTemplate.windowed(2)

        for (double in doubles) {
            occurrences[double] = occurrences.getOrDefault(double, 0) + 1
        }
        for (i in 1..40) {
            val iterOccurrences = mutableMapOf<String, Long>()
            for (double in occurrences.keys) {
                val chars = double.toList()
                val insertedLetter = pairInsertionRules[double]
                iterOccurrences["${chars[0]}$insertedLetter"] = iterOccurrences.getOrDefault("${chars[0]}$insertedLetter", 0) + occurrences[double]!!
                iterOccurrences["$insertedLetter${chars[1]}"] = iterOccurrences.getOrDefault("$insertedLetter${chars[1]}", 0) + occurrences[double]!!
            }
            occurrences = iterOccurrences
        }

        val letters = mutableMapOf<String, Long>()
        for (double in occurrences.keys) {
            val chars = double.toList()
            letters["${chars[0]}"] = letters.getOrDefault("${chars[0]}", 0) + occurrences[double]!!
            letters["${chars[1]}"] = letters.getOrDefault("${chars[1]}", 0) + occurrences[double]!!
        }
        letters["${polymerTemplate.toList().first()}"] = letters["${polymerTemplate.toList().first()}"]!! + 1
        letters["${polymerTemplate.toList().last()}"] = letters["${polymerTemplate.toList().last()}"]!! + 1

        for (double in letters.keys) {
            letters[double] = letters[double]!! / 2
        }

        return letters.values.maxOrNull()!! - letters.values.minOrNull()!!
    }
}

fun main(){

    val dayOne = Day14()

    val lines = readStrings()

    val polymerTemplate = lines.first()

    val pairInsertionRules = lines.subList(2, lines.size).map { el -> el.trim().split(" -> ").take(2).zipWithNext().first() }.toMap()

    println(dayOne.partOne(polymerTemplate, pairInsertionRules))
    println(dayOne.partTwo(polymerTemplate, pairInsertionRules))
}