package days

class TargetArea(
        val x1: Int,
        val x2: Int,
        val y1: Int,
        val y2: Int
) {

    fun isInArea(x: Int, y: Int): Boolean {
        return x in x1..x2 && y in y1.. y2
    }

    // This assumes that target area's x position is positive
    fun cannotReachTargetArea(x: Int, y: Int): Boolean {
        return x > x2 || y < y1
    }

    override fun toString(): String {
        return "x1: $x1, x2: $x2, y1: $y1, y2: $y2"
    }
}

class Day17 {

    private fun simulateTrajectory(x: Int, y: Int, targetArea: TargetArea): Pair<Boolean, Int> {
        var xPosition = 0
        var yPosition = 0
        var xVelocity = x
        var yVelocity = y

        var maxY = 0

        while (!targetArea.cannotReachTargetArea(xPosition, yPosition)) {
            xPosition += xVelocity
            yPosition += yVelocity
            if (yPosition > maxY) maxY = yPosition

            if (targetArea.isInArea(xPosition, yPosition)) {
                return Pair(true, maxY)
            }

            xVelocity = if (xVelocity > 0) xVelocity - 1 else if (xVelocity < 0) { xVelocity - 1 } else 0
            yVelocity--
        }

        return Pair(false, maxY)
    }

    fun partOne(targetArea: TargetArea): Int {

        var highestPeak = 0

        for (possibleXVelocity in 1..targetArea.x2) {
            for (possibleYVelocity in 1..1000) {
                val (isValid, maxY) = simulateTrajectory(possibleXVelocity, possibleYVelocity, targetArea)
                if (isValid) {
                    if (maxY > highestPeak) highestPeak = maxY;
                }
            }
        }

        return highestPeak
    }

    fun partTwo(targetArea: TargetArea): Int {

        var distinctInitialVelocities = 0

        for (possibleXVelocity in 1..targetArea.x2) {
            for (possibleYVelocity in targetArea.y1..1000) {
                val (isValid, _) = simulateTrajectory(possibleXVelocity, possibleYVelocity, targetArea)
                if (isValid) {
                    distinctInitialVelocities++
                }
            }
        }

        return distinctInitialVelocities
    }
}

private fun parseInput(targetArea: String): TargetArea {
    val components = targetArea.split(" ")
    val (x1, x2) = components[2].split("=")[1].removeSuffix(",").split("..")
    val (y1, y2) = components[3].split("=")[1].split("..")
    return TargetArea(x1.toInt(), x2.toInt(), y1.toInt(), y2.toInt())
}

fun main(){

    val day17 = Day17()

    val targetArea = parseInput(readln())

    println(day17.partOne(targetArea))
    println(day17.partTwo(targetArea))

}