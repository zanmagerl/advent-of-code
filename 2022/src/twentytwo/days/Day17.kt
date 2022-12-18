package twentytwo.days

data class RockPosition(val x: Int, val y: Int) {

    fun applyJet(jet: Char): RockPosition {
        return when (jet) {
            '<' -> this.copy(x = x - 1)
            '>' -> this.copy(x = x + 1)
            else -> throw IllegalArgumentException("Wrong jet type!")
        }
    }

    fun applyGravity(): RockPosition {
        return this.copy(y = y - 1)
    }
}

private fun canFall(rockPosition: RockPosition, cave: Map<RockPosition, Boolean>): Boolean {
    return !cave.containsKey(rockPosition.copy(y = rockPosition.y - 1)) && rockPosition.y > 1
}

private fun canMove(rock: List<RockPosition>, cave: Map<RockPosition, Boolean>, jet: Char): Boolean {
    return when (jet) {
        '<' -> rock.minOf { it.x } > 0 && rock.none { cave.containsKey(RockPosition(it.x-1, it.y)) }
        '>' -> rock.maxOf { it.x } < 6 && rock.none { cave.containsKey(RockPosition(it.x+1, it.y)) }
        else -> throw IllegalArgumentException("Unknown jet type!")
    }
}

private fun generateRock(step: Int, cave: Map<RockPosition, Boolean>): List<RockPosition> {
    val y = (cave.keys.maxOfOrNull { it.y } ?: 0).plus(4)
    return when (step % 5) {
        0 -> (2..5).map { x -> RockPosition(x, y) }
        1 -> listOf(RockPosition(2, y+1), RockPosition(2+1, y+1), RockPosition(2+2, y+1), RockPosition(2+1, y), RockPosition(2+1, y+2))
        2 -> listOf(RockPosition(2, y), RockPosition(2+1, y), RockPosition(2+2, y), RockPosition(2+2, y+1), RockPosition(2+2, y+2))
        3 -> (0..3).map { RockPosition(2, y+it) }
        4 -> listOf(RockPosition(2, y), RockPosition(2+1,y), RockPosition(2, y+1), RockPosition(2+1, y+1))
        else -> throw IllegalArgumentException("Unknown rock type!")
    }
}

private fun partOne(jetPattern: List<Char>): Int {
    val cave = mutableMapOf<RockPosition, Boolean>()
    var time = 0
    (0 until 2022).map {rockNumber ->
        var rock = generateRock(rockNumber % 5, cave)
        var isAtRest = false
        while (!isAtRest) {
            rock = rock
                    .map {
                        val jet = jetPattern[time % jetPattern.size]
                        if (canMove(rock, cave, jet)) {
                            it.applyJet(jet)
                        } else {
                            it
                        }
                    }
            isAtRest = !rock.all { canFall(it, cave) }
            if (!isAtRest) {
                rock = rock.map { it.applyGravity() }
            }
            time++
        }
        rock.map { cave[it] = true }
    }
    return cave.keys.maxOf { it.y }
}

/**
 * It would take quite a lot of time to simulate this. Since there is a lot of repeating in the simulation (i.e. rock
 * generation, jet patterns) there should be a pattern also in the simulation itself.
 * Therefore, I tried with one arbitrary chosen pattern:
 * ..#.##.
 * ..#.##.
 * Find two occurrences where this pattern repeats and has the same jet index. Then find difference between their rock
 * numbers and their heights. After you find height of the remainder you can just use simple math compute the answer:
 * answer = (number of rocks / cycle length) * cycle height + remainder height
 */
private fun partTwo(jetPattern: List<Char>): Long {
    val cave = mutableMapOf<RockPosition, Boolean>()
    var time = 0
    (0 until 2022).map {rockNumber ->
        var rock = generateRock(rockNumber % 5, cave)
        var isAtRest = false
        while (!isAtRest) {
            rock = rock
                    .map {
                        val jet = jetPattern[time % jetPattern.size]
                        if (canMove(rock, cave, jet)) {
                            it.applyJet(jet)
                        } else {
                            it
                        }
                    }
            isAtRest = !rock.all { canFall(it, cave) }
            if (!isAtRest) {
                rock = rock.map { it.applyGravity() }
            }
            time++
        }
        rock.map { cave[it] = true }
        val y = rock.first().y

        if ((!cave.containsKey(RockPosition(0, y)) && !cave.containsKey(RockPosition(1, y))
                        && cave.containsKey(RockPosition(2, y)) && !cave.containsKey(RockPosition(3, y)) &&
                        cave.containsKey(RockPosition(4, y)) && cave.containsKey(RockPosition(5, y))
                        && !cave.containsKey(RockPosition(6, y))) &&
                (!cave.containsKey(RockPosition(0, y+1)) && !cave.containsKey(RockPosition(1, y+1))
                        && cave.containsKey(RockPosition(2, y+1)) && !cave.containsKey(RockPosition(3, y+1)) &&
                        cave.containsKey(RockPosition(4, y+1)) && cave.containsKey(RockPosition(5, y+1))
                        && !cave.containsKey(RockPosition(6, y+1)))
        ) {
            println("Pattern repeats at ${rockNumber}th rock and has jet ${time % jetPattern.size}. Height of tower is: ${cave.keys.maxOf { it.y }}")
        }
        val remainder = (999999999825L % 1690).toInt()
        if (rockNumber == 174 + remainder) {
            println("Remainder is $remainder and has height ${cave.keys.maxOf { it.y }}")
        }
    }
    return (999999999825L / 1690) * 2548 + 842
}

fun main(){
    val jetPattern = readln().toCharArray().toList()

    println(partOne(jetPattern))
    println(partTwo(jetPattern))
}