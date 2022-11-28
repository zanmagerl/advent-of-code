package days

import utilities.readStrings
import java.util.function.BiPredicate

class Day16 {

    abstract class Packet(open val version: Int, open val typeId: Int)

    class LiteralPacket(
        override val version: Int,
        override val typeId: Int,
        val value: Long
    ): Packet(version, typeId) {

        override fun toString(): String {
            return "LiteralPacket - version: $version, typeId: $typeId, value: $value"
        }
    }

    class OperatorPacket(
            override val version: Int,
            override val typeId: Int,
            val lengthTypeId: Int,
            val subpackets: List<Packet>
    ): Packet(version, typeId) {

        override fun toString(): String {
            return "OperatorPacket - version: $version, typeId: $typeId, lengthTypeId: $lengthTypeId, subpacketsSize: ${subpackets.size}"
        }
    }


    private fun extractLiteralValue(bits: String, index: Int): Pair<Long, Int> {
        var value = ""
        var iterIndex = index
        var numberOfGroups = 0
        do {
            value += bits.subSequence(iterIndex+1, iterIndex+5)
            iterIndex += 5
            numberOfGroups += 1
        } while (bits[iterIndex-5] != '0')

        return Pair(value.toLong(2), iterIndex)
    }

    fun parsePackets(bits: String, index: Int, endCondition: BiPredicate<Int, Int>): Pair<List<Packet>, Int> {

        //println(bits)
        var iterIndex = index;
        val packets = mutableListOf<Packet>()


        while (endCondition.test(iterIndex, packets.size)) {
            val version = bits.subSequence(iterIndex, iterIndex+3).toString().toInt(2)
            iterIndex += 3
            val typeId = bits.subSequence(iterIndex, iterIndex+3).toString().toInt(2)
            iterIndex += 3

            //println("Index: $index, version: $version, typeId: $typeId")

            val packet = if (typeId == 4) {
                val (literalValue, endIndex) = extractLiteralValue(bits, iterIndex)
                iterIndex = endIndex
                LiteralPacket(
                        version,
                        typeId,
                        literalValue
                )
            } else {
                val lengthTypeId = bits.subSequence(iterIndex, iterIndex+1)[0].digitToInt()
                iterIndex += 1
                val givenParameters = if (lengthTypeId == 0) {
                    val value = bits.subSequence(iterIndex, iterIndex + 15).toString().toInt(2)
                    //println(value)
                    iterIndex += 15
                    {currIndex: Int, _: Int -> currIndex < iterIndex + value}
                } else {
                    val value = bits.subSequence(iterIndex, iterIndex + 11).toString().toInt(2)
                    //println(value)
                    iterIndex += 11
                    {_: Int, remainingPackets -> remainingPackets < value}
                }
                //println("Length type id: $lengthTypeId, parameters: $givenParameters")
                val (parsedPackets, endIndex) = parsePackets(bits, iterIndex, givenParameters)
                //println("End index: $endIndex, current index: $iterIndex")
                iterIndex = endIndex
                OperatorPacket(
                        version,
                        typeId,
                        lengthTypeId,
                        parsedPackets
                )
            }
            //println("Number of packets: $numberOfParsedPackets, current index: $iterIndex")
            packets.add(packet)
        }
        //println(packets.map { it.toString() })
        return Pair(packets, iterIndex)
    }

    fun partOne(packets: List<Packet>): Int {
        var result = 0
        for (packet in packets) {
            result += packet.version
            if (packet is OperatorPacket) {
                result += partOne(packet.subpackets)
            }
        }
        return result
    }

    fun partTwo(packets: List<Packet>): Long {
        var result = 0L
        for (packet in packets) {
            result = result.plus(when (packet) {
                is LiteralPacket -> packet.value
                is OperatorPacket -> {
                    when (packet.typeId) {
                        0 -> packet.subpackets.sumOf { partTwo(listOf(it)) }
                        1 -> packet.subpackets.fold (1L, {acc, i -> partTwo(listOf(i)) * acc})
                        2 -> packet.subpackets.minOf { partTwo(listOf(it)) }
                        3 -> packet.subpackets.maxOf { partTwo(listOf(it)) }
                        5 -> if (partTwo(listOf(packet.subpackets.first())) > partTwo(listOf(packet.subpackets.last()))) 1 else 0
                        6 -> if (partTwo(listOf(packet.subpackets.first())) < partTwo(listOf(packet.subpackets.last()))) 1 else 0
                        7 -> if (partTwo(listOf(packet.subpackets.first())) == partTwo(listOf(packet.subpackets.last()))) 1 else 0
                        else -> throw Exception("Wrong typeId for packet")
                    }
                }
                else -> throw Exception("Wrong packet type")
            })
        }
        return result
    }
}

private fun String.padWithZeros(): String {
    return "${"0".repeat(4 - this.length)}$this"
}

fun main(){

    val day16 = Day16()

    val input = readStrings()[0]
    val bits = input.toCharArray()
        .map { it.digitToInt(16).toString(2).padWithZeros() }
        .reduce { acc, s -> "$acc$s" }

    println(day16.partOne(day16.parsePackets(bits, 0, { currIndex, _ ->  currIndex == 0 }).first))
    println(day16.partTwo(day16.parsePackets(bits, 0, { currIndex, _ ->  currIndex == 0 }).first))
}