package twentytwo.days

import twentytwo.utilities.readInts
import kotlin.math.abs

// We wrap numbers in class because numbers might not be unique
class Number(val value: Long)

private fun calculateIndex(index: Int, value: Long, size: Int): Int {
    return when ((index + value).compareTo(0)) {
        1 -> ((index + value) % size)
        0 -> size
        -1 -> ((index + value + ((abs(value) / size) + 1) * size) % size)
        else -> throw IllegalArgumentException("Unexpected index value!")
    }.toInt()
}

private fun partOne(numbers: MutableList<Number>): Long {
    numbers.toList().map {
        val index = numbers.indexOf(it)
        numbers.remove(it)
        numbers.add(calculateIndex(index, it.value, numbers.size), it)
    }
    val zero = numbers.first { it.value == 0L }
    return listOf(1000, 2000, 3000).sumOf { numbers[(numbers.indexOf(zero) + it) % numbers.size].value }
}

private fun partTwo(numbers: MutableList<Number>): Long {
    val decryptionKey = 811589153
    val decryptedNumbers = numbers.map { Number(it.value * decryptionKey) }.toMutableList()
    val decryptedNumbersInOriginalOrder = decryptedNumbers.toList()
    (0 until 10).map {
        decryptedNumbersInOriginalOrder.map {
            val index = decryptedNumbers.indexOf(it)
            decryptedNumbers.remove(it)
            decryptedNumbers.add(calculateIndex(index, it.value, decryptedNumbers.size), it)
        }
    }
    val zero = decryptedNumbers.first { it.value == 0L }
    return listOf(1000, 2000, 3000).sumOf { decryptedNumbers[(decryptedNumbers.indexOf(zero) + it) % decryptedNumbers.size].value }
}

fun main(){

    val input = readInts().map { Number(it.toLong()) }.toMutableList()

    println(partOne(input.toMutableList()))
    println(partTwo(input))
}