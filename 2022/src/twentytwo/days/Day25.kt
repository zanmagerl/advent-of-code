package twentytwo.days

import twentytwo.utilities.readStrings

private fun getSnafuDigit(char: Char): Int {
    return when(char) {
        '2' -> 2
        '1' -> 1
        '0' -> 0
        '-' -> -1
        '=' -> -2
        else -> throw IllegalArgumentException("Unknown digit!")
    }
}

private fun convertToNumber(snafu: String): Long {
    return snafu.fold(0){acc, digit -> acc * 5 + getSnafuDigit(digit) }
}

private fun convertToSnafu(number: Long): String {
    var curr = number
    var numInRadix5 = ""
    while (curr > 0) {
        numInRadix5 += "${curr % 5}"
        curr /= 5
    }
    return numInRadix5.fold(0 to "") { acc, c ->
        when (c.digitToInt() + acc.first) {
            5 -> 1 to "0${acc.second}"
            4 -> 1 to "-${acc.second}"
            3 -> 1 to "=${acc.second}"
            2 -> 0 to "2${acc.second}"
            1 -> 0 to "1${acc.second}"
            0 -> 0 to "0${acc.second}"
            else -> throw IllegalArgumentException("$acc")
        }
    }.let { if (it.first != 0) "1${it.second}" else it.second }
}

private fun partOne(numbers: List<String>): String {
    return convertToSnafu(numbers.sumOf { convertToNumber(it) })
}

fun main(){
    val input = readStrings()

    println(partOne(input))
}