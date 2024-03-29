package twentytwo.days

import twentytwo.utilities.readStrings
import twentytwo.utilities.toPair

data class Range(val start: Int, val end: Int) {

    companion object {
        fun of(range: String): Range {
            return range.split("-").map { it.toInt() }.toPair().run { Range(this.first, this.second) }
        }
    }

    fun isContained(range: Range): Boolean {
        return (this.start..this.end).all { (range.start..range.end).contains(it) }
    }
    fun doesOverlap(range: Range): Boolean {
        return this.start in range.start..range.end
    }
}

private fun partOne(assignmentPairs: List<Pair<Range, Range>>): Int {
    return assignmentPairs.count { it.first.isContained(it.second) || it.second.isContained(it.first) }
}

private fun partTwo(assignmentPairs: List<Pair<Range, Range>>): Int {
    return assignmentPairs.count { it.first.doesOverlap(it.second) || it.second.doesOverlap(it.first) }
}

fun main(){

    val input = readStrings().map { it.split(",").toPair() }.map { Range.of(it.first) to Range.of(it.second) }

    println(partOne(input))
    println(partTwo(input))
}