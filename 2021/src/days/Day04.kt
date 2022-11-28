package days

import utilities.readStrings

data class BingoNumber(
    val number: Int,
    var drawn: Boolean
)

class Day04 {

    private fun checkIfBoardWins(board: List<List<BingoNumber>>): Boolean {
        for (row in board) {
            if (row.count{ a -> a.drawn} == row.size) {
                return true
            }
        }

        for (column in 0 until board.size) {
            if (board.map { row -> row[column] }.count{a -> a.drawn} == board.size) {
                return true
            }
        }
        return false
    }

    private fun markMove(move: Int, board: List<List<BingoNumber>>){
        for (row in board){
            row.filter { element -> element.number == move}.map { el -> el.drawn = true }
        }

    }

    fun partOne(moves: List<Int>, boards: List<List<List<BingoNumber>>>): Int {

        for (move in moves) {

            for (board in boards) {
                markMove(move, board)

                val isOver = checkIfBoardWins(board)

                if (isOver){
                    return move * board.sumBy { el -> el.fold(0){ acc, bingoNumber -> if (!bingoNumber.drawn) acc + bingoNumber.number else acc } }
                }
            }
        }

        return -1;
    }

    fun partTwo(moves: List<Int>, boards: List<List<List<BingoNumber>>>): Int {

        val alreadyWon = mutableListOf<List<List<BingoNumber>>>()
        var value = -1

        for (move in moves) {

            for (board in boards) {

                if (board in alreadyWon) continue

                markMove(move, board)

                val isOver = checkIfBoardWins(board)

                if (isOver){
                    alreadyWon.add(board)
                    value = move * board.sumBy { el -> el.fold(0){ acc, bingoNumber -> if (!bingoNumber.drawn) acc + bingoNumber.number else acc } }
                }
            }
        }

        return value
    }
}

fun main(){

    val day04 = Day04()

    val input = readStrings()


    val moves = input[0].split(",").map { a -> a.toInt() }

    val boards = mutableListOf<List<List<BingoNumber>>>()

    var count = 1
    while (count < input.size) {
        count += 1

        val board = mutableListOf<List<BingoNumber>>()

        for (index in 0..4) {
            board.add(input[count+index].split(" ").filter { a -> a.isNotEmpty() }.map { a -> BingoNumber(a.toInt(), false) })
        }

        boards.add(board)

        count += 5
    }

    println(day04.partTwo(moves, boards))
    //println(day04.partTwo(moves, boards))

}