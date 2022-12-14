package twentytwo.days

import twentytwo.utilities.readStrings
import twentytwo.utilities.toPair

open class PacketData
data class Integer(val value: Int): PacketData()
data class Lists(val values: MutableList<PacketData>): PacketData()

enum class Order {
    CORRECT,
    INCORRECT,
    NOT_KNOW
}

/**
 * Horrible function that does the recursive parsing, but it gets the job done
 */
private fun parsePacketData(line: String, index: Int): Pair<PacketData, Int> {
    var iterIndex = index
    val currentPacketData = Lists(mutableListOf())
    var buffer = ""
    while (line[iterIndex] != ']') {
        if (line[iterIndex] == ',') {
            if (buffer.isNotEmpty()) {
                currentPacketData.values.add(Integer(buffer.toInt()))
                buffer = ""
            }
        }
        else if (line[iterIndex] == '[') {
            val (packet, ind) = parsePacketData(line, iterIndex+1)
            currentPacketData.values.add(packet)
            iterIndex = ind
        } else {
            buffer += line[iterIndex]
        }
        iterIndex++
    }

    if (buffer.isNotEmpty()) {
        currentPacketData.values.add(Integer(buffer.toInt()))
    }

    return currentPacketData to iterIndex
}

private fun inCorrectOrder(firstPacket: PacketData, secondPacket: PacketData): Order {

    when {
        firstPacket is Integer && secondPacket is Integer -> return when {
            firstPacket.value < secondPacket.value -> Order.CORRECT
            firstPacket.value > secondPacket.value -> Order.INCORRECT
            else -> Order.NOT_KNOW
        }
        firstPacket is Lists && secondPacket is Lists -> {
            firstPacket.values.mapIndexed{index, value ->
                if (index >= secondPacket.values.size) return Order.INCORRECT
                val order = inCorrectOrder(value, secondPacket.values[index])
                if (order == Order.CORRECT) return Order.CORRECT
                if (order == Order.INCORRECT) return Order.INCORRECT
            }
            if (firstPacket.values.size < secondPacket.values.size) {
                return Order.CORRECT
            }
            return Order.NOT_KNOW
        }
        firstPacket is Lists && secondPacket is Integer -> return inCorrectOrder(firstPacket, Lists(values = mutableListOf(secondPacket)))
        firstPacket is Integer && secondPacket is Lists -> return inCorrectOrder(Lists(values = mutableListOf(firstPacket)), secondPacket)

    }
    return Order.NOT_KNOW
}

private fun partOne(packets: List<Pair<PacketData, PacketData>>): Int {
    return packets
            .mapIndexed { index, pair -> if (inCorrectOrder(pair.first, pair.second) == Order.CORRECT) index+1 else 0 }
            .sum()
}

private fun partTwo(packets: List<PacketData>, dividerPackets: List<PacketData>): Int {
    // Let's use "comparator" that we have written in part 1 and transform it to the standard form
    val comparator = { a: PacketData, b: PacketData ->
        when (inCorrectOrder(a, b)) {
            Order.CORRECT -> -1
            Order.NOT_KNOW -> 0
            Order.INCORRECT -> 1
        }
    }
    return packets.sortedWith(comparator)
            .mapIndexed { index, it -> if (dividerPackets.contains(it)) index+1 else 1 }
            .reduce(Int::times)
}

fun main(){

    val input = readStrings().fold(mutableListOf<MutableList<PacketData>>(mutableListOf())) { acc, line ->
        if (line.isBlank()) {
            acc.add(mutableListOf())
        } else {
            acc.last().add(parsePacketData(line, 1).first)
        }
        acc
    }.map { it.toPair() }.toList()

    val dividerPackets = listOf(
            Lists(values = mutableListOf(Lists(values = mutableListOf(twentytwo.days.Integer(2))))),
            Lists(values = mutableListOf(Lists(values = mutableListOf(twentytwo.days.Integer(6)))))
    )

    println(partOne(input))
    println(partTwo(input.flatMap { it.toList() }.plus(dividerPackets), dividerPackets))
}