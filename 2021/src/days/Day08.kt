package days

import utilities.readStrings


class Day08{

    fun partOne(outputs: List<List<String>>): Int {
        val lengthOfUniqueDigits = listOf(2,3,4,7)
        return outputs.map { a -> a.count { b -> b.length in lengthOfUniqueDigits } }.sum()
    }

    private fun containsDigit(candidate: String, digit: String): Boolean {
        return candidate.count { c -> digit.contains(c) } == digit.length
    }

    private fun findDigit(digits : Map<Int, String>, digit: String): Int {
        for (key in digits.keys) {
            if (digits[key]!! == digit) return key
        }
        return -1
    }

    fun partTwo(patterns: List<List<String>>, outputs: List<List<String>>): Long {

        var score = 0L

        val lengthToDigitMapping = mapOf(
                2 to listOf(1),
                3 to listOf(7),
                4 to listOf(4),
                5 to listOf(2,3,5),
                6 to listOf(0,6,9),
                7 to listOf(8)
        )

        for (i in patterns.indices) {
            val pattern = patterns[i]
            val digits = mutableMapOf<Int, String>()

            for (digitPattern in pattern) {
                if (lengthToDigitMapping[digitPattern.length]!!.size == 1){
                    digits.put(lengthToDigitMapping[digitPattern.length]!!.first(), digitPattern)
                }
            }

            digits[3] = pattern.first { p -> p.length == 5 && containsDigit(p, digits[1]!!) }

            digits[9] = pattern.first {p -> p.length == 6 && containsDigit(p,digits[3]!!)}
            digits[0] = pattern.first {p -> p.length == 6 && containsDigit(p,digits[1]!!) && !containsDigit(p, digits[3]!!)}
            digits[6] = pattern.first {p -> p.length == 6 && (!containsDigit(p,digits[9]!!) && !containsDigit(p,digits[0]!!))}

            digits[2] = pattern.first{ p -> p.length == 5 && p != digits[3]!! && containsDigit(p,digits[8]!!.filter { c -> !digits[6]!!.contains(c) } )}
            digits[5] = pattern.first { p -> !digits.values.contains(p) }

            for (key in digits.keys){
                digits[key] = String(digits[key]!!.toCharArray().apply { sort() })
            }


            var value = 0

            for (outputDigit in outputs[i]){
                value *= 10
                value += findDigit(digits, String(outputDigit.toCharArray().apply { sort() }))
            }

            score += value

        }

        return score
    }

}

fun main(){

    val dayEight = Day08()

    val lines = readStrings()

    val patterns = mutableListOf<List<String>>()
    val outputs = mutableListOf<List<String>>()

    for (line in lines){
        val splittedLine = line.split("|")
        patterns.add(splittedLine[0].split(" ").map { a -> a.trim() }.filter { a -> a.isNotEmpty() })
        outputs.add(splittedLine[1].split(" ").map { a -> a.trim() }.filter { a -> a.isNotEmpty() })
    }

    println(dayEight.partOne(outputs))
    println(dayEight.partTwo(patterns, outputs))

}