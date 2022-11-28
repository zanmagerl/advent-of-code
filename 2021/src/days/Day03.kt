package days

import utilities.readStrings

class Day03 {

    fun partOne(bits: List<String>): Int {

        val numberOfOnes = IntArray(bits[0].length)

        for (number in bits) {
            val splitted = number.toCharArray()
            for (bit in splitted.indices){
                if (splitted[bit] == '1') {
                    numberOfOnes[bit] += 1
                }
            }
        }

        var epsilonRate = 0
        var gammaRate = 0

        for (number in numberOfOnes) {
            gammaRate *= 2
            epsilonRate *= 2
            if (number > bits.size/2) {
                gammaRate += 1
            } else {
                epsilonRate += 1
            }
        }

        return  epsilonRate * gammaRate
    }

    fun findOxygen(bits: List<String>): Int {
        var index = 0

        var iter = bits

        while (iter.size > 1) {

            val numberOfOnes = IntArray(iter[0].length)

            for (number in iter) {
                val splitted = number.toCharArray()
                for (bit in splitted.indices){
                    if (splitted[bit] == '1') {
                        numberOfOnes[bit] += 1
                    }
                }
            }

            val searchValue = if (numberOfOnes[index] > iter.size/2 || (iter.size % 2 == 0 && numberOfOnes[index] == iter.size/2 )) {
                '1'
            } else {
                '0'
            }


            iter = iter.filter { it[index] == searchValue }
            index++
        }

        var result = 0

        for (bit in iter[0]) {
            result *= 2
            if (bit == '1'){
                result += 1
            }
        }
        println(result)
        return result
    }

    fun findCO2(bits: List<String>): Int {
        var index = 0

        var iter = bits

        while (iter.size > 1) {

            val numberOfOnes = IntArray(iter[0].length)


            for (number in iter) {
                val splitted = number.toCharArray()
                for (bit in splitted.indices){
                    if (splitted[bit] == '1') {
                        numberOfOnes[bit] += 1
                    }
                }
            }

            val searchValue = if (numberOfOnes[index] > iter.size/2 || (iter.size % 2 == 0 && numberOfOnes[index] == iter.size/2 )) {
                '0'
            } else {
                '1'
            }

            iter = iter.filter { it[index] == searchValue }
            index++
        }

        var result = 0

        for (bit in iter[0]) {
            result *= 2
            if (bit == '1'){
                result += 1
            }
        }

        return result
    }

    fun partTwo(bits: List<String>): Int {

        return findOxygen(bits.toList()) * findCO2(bits.toList())
    }
}

fun main(){

    val day03 = Day03()

    val input = readStrings()

    println(day03.partOne(input))
    println(day03.partTwo(input))

}