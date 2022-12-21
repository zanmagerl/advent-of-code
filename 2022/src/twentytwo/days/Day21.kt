package twentytwo.days

import twentytwo.utilities.gcd
import twentytwo.utilities.readStrings

class Day21 {

    /**
     * This is poor man's equation solver that handles equations with one variable. Idea is that we say that every
     * monkey shouts monkey number and human shouts human number. We want to compute what we can from monkey numbers and only
     * accumulate human numbers. We then get something like that:
     * left: (monkeyNumber=-1000, humanNumber=2, denominator=1)
     * right: (monkeyNumber=0, humanNumber=0, denominator=1)
     * => human needs to shout -500.
     * We need denominator since computer handles floats very badly. :rip:
     * Also: this problem can be transformed to the complex numbers manipulation: https://en.wikipedia.org/wiki/Complex_number
     */
    data class MonkeyHumanNumber(val monkeyNumber: Long, val humanNumber: Long, val denominator: Long) {

        // Let's simplify the fractions
        private fun simplify(): MonkeyHumanNumber {
            val factor = gcd(gcd(this.monkeyNumber, this.denominator), gcd(this.humanNumber, this.denominator))
            return MonkeyHumanNumber(this.monkeyNumber / factor, this.humanNumber / factor, this.denominator / factor)
        }

        fun plus(monkey2: MonkeyHumanNumber): MonkeyHumanNumber {
            return MonkeyHumanNumber(
                    this.monkeyNumber * monkey2.denominator + monkey2.monkeyNumber * this.denominator ,
                    this.humanNumber * monkey2.denominator + monkey2.humanNumber * this.denominator,
                    this.denominator * monkey2.denominator
            ).simplify()
        }
        fun minus(monkey2: MonkeyHumanNumber): MonkeyHumanNumber {
            return MonkeyHumanNumber(
                    this.monkeyNumber * monkey2.denominator - monkey2.monkeyNumber * this.denominator ,
                    this.humanNumber * monkey2.denominator - monkey2.humanNumber * this.denominator,
                    this.denominator * monkey2.denominator
            ).simplify()
        }

        fun times(monkey2: MonkeyHumanNumber): MonkeyHumanNumber {
            return MonkeyHumanNumber(
                    this.monkeyNumber * monkey2.monkeyNumber + this.humanNumber * monkey2.humanNumber,
                    this.monkeyNumber * monkey2.humanNumber + this.humanNumber * monkey2.monkeyNumber,
                    this.denominator * monkey2.denominator
            ).simplify()
        }
        fun div(monkey2: MonkeyHumanNumber): MonkeyHumanNumber {
            return MonkeyHumanNumber(
                    this.monkeyNumber * monkey2.monkeyNumber + this.humanNumber * monkey2.humanNumber,
                    this.monkeyNumber * monkey2.humanNumber - this.humanNumber * monkey2.monkeyNumber,
                    (this.denominator * monkey2.denominator) * (monkey2.monkeyNumber*monkey2.monkeyNumber + monkey2.humanNumber * monkey2.humanNumber)
            ).simplify()
        }
    }

    abstract class Monkey(open val name: String) {
        companion object {
            fun of(components: List<String>): Monkey {
                if (components.size == 2) {
                    return NumberMonkey(components[0].dropLast(1), MonkeyHumanNumber(components[1].toLong(), 0, 1))
                }
                return OperationMonkey(components[0].dropLast(1)) { map: Map<String, Monkey> ->
                    val monkey1 = map[components[1]]!!.value(map)
                    val monkey2 = map[components[3]]!!.value(map)
                    when (components[2]) {
                        "*" -> monkey1.times(monkey2)
                        "+" -> monkey1.plus(monkey2)
                        "-" -> monkey1.minus(monkey2)
                        "/" -> monkey1.div(monkey2)
                        else -> throw IllegalArgumentException("Undefined operation!")
                    }
                }
            }
        }

        abstract fun value(monkeys: Map<String, Monkey>): MonkeyHumanNumber
    }

    data class NumberMonkey(override val name: String, var monkeyHumanNumber: MonkeyHumanNumber): Monkey(name) {
        override fun value(monkeys: Map<String, Monkey>): MonkeyHumanNumber {
            return monkeyHumanNumber
        }
    }

    data class OperationMonkey(override val name: String, val operation: (Map<String, Monkey>) -> MonkeyHumanNumber): Monkey(name) {
        override fun value(monkeys: Map<String, Monkey>): MonkeyHumanNumber {
            return operation.invoke(monkeys)
        }
    }

}

private fun partOne(monkeys: Map<String, Day21.Monkey>): Long {
    return monkeys["root"]!!.value(monkeys).monkeyNumber
}

private fun partTwo(monkeys: MutableMap<String, Day21.Monkey>): Long {
    monkeys["humn"] = Day21.NumberMonkey("humn", Day21.MonkeyHumanNumber(0, 1, 1))
    val left = monkeys["jwcq"]!!.value(monkeys)
    val right = monkeys["swbn"]!!.value(monkeys)
    return (right.monkeyNumber * left.denominator - left.monkeyNumber) / left.humanNumber
}

fun main(){

    val monkeys = readStrings().associate {
        val monkey = Day21.Monkey.of(it.split(" ").map { it.trim() })
        monkey.name to monkey
    }

    println(partOne(monkeys))
    println(partTwo(monkeys.toMutableMap()))
}