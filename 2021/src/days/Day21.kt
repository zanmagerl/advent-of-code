package days

import kotlin.math.min

data class Result (val positionPlayer1: Int, val positionPlayer2: Int, val scorePlayer1: Int, val scorePlayer2: Int)

class Day21 {

    private fun increaseRolldice(dice: Int): Int {
        val newDice = dice + 1
        if (newDice == 101) {
            return 1
        }
        return newDice
    }

    private fun trimPosition(position: Int): Int {
        return if (position % 10 == 0) {
            10
        } else {
            position % 10
        }
    }

    fun partOne(startPlayer1: Int, startPlayer2: Int): Long {

        var positionPlayer1 = startPlayer1
        var positionPlayer2 = startPlayer2

        var score1 = 0L
        var score2 = 0L

        var dice = 1
        var numberOfDiceRoles = 0
        while (true) {
            if (numberOfDiceRoles % 6 < 3) {
                positionPlayer1 += dice
                if (numberOfDiceRoles % 6 == 2) {
                    positionPlayer1 = trimPosition(positionPlayer1)
                    score1 += positionPlayer1
                    if (score1 >= 1000) break
                }
            } else {
                positionPlayer2 += dice
                if (numberOfDiceRoles % 6 == 5) {
                    positionPlayer2 = trimPosition(positionPlayer2)
                    score2 += positionPlayer2
                    if (score2 >= 1000) break
                }
            }
            dice = increaseRolldice(dice)
            numberOfDiceRoles++
        }
        return min(score1, score2) * (numberOfDiceRoles+1)
    }

    fun partTwo(startPlayer1: Int, startPlayer2: Int): Long {

        var positionPlayer1 = startPlayer1
        var positionPlayer2 = startPlayer2

        var score1 = 0L
        var score2 = 0L

        var numberOfDiceRoles = 0
        while (true) {
            val diceRole = (numberOfDiceRoles % 3) + 1
            if (numberOfDiceRoles % 6 < 3) {
                positionPlayer1 += diceRole
                if (numberOfDiceRoles % 6 == 2) {
                    positionPlayer1 = trimPosition(positionPlayer1)
                    score1 += positionPlayer1
                    if (score1 >= 21) break
                }
            } else {
                positionPlayer2 += diceRole
                if (numberOfDiceRoles % 6 == 5) {
                    positionPlayer2 = trimPosition(positionPlayer2)
                    score2 += positionPlayer2
                    if (score2 >= 21) break
                }
            }
            numberOfDiceRoles++
        }
        return min(score1, score2) * (numberOfDiceRoles+1)
    }
}

fun main(){

    val day21 = Day21()

    val startPlayer1 = readln().split(" ").last().toInt()
    val startPlayer2 = readln().split(" ").last().toInt()

    //println(day21.partOne(startPlayer1, startPlayer2))
    println(day21.partTwo(startPlayer1, startPlayer2))
}