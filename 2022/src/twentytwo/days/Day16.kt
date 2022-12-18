package twentytwo.days

import twentytwo.utilities.readStrings
import twentytwo.utilities.use
import java.util.PriorityQueue


data class Valve(val name: String, val flowRate: Int, val neighbours: MutableList<Valve>, var open: Boolean) {
    companion object {
        fun of(name: String, flowRate: Int): Valve {
            return Valve(name, flowRate, mutableListOf(), false)
        }
    }

    override fun toString(): String {
        return "$name: flowRate=$flowRate open=$open"
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

data class Node(val valve: Valve, var distance: Int): Comparable<Node> {
    override fun compareTo(other: Node): Int {
        return this.distance.compareTo(other.distance)
    }
}

private fun relax(u: Node, v: Node, weight: Int = 1): Boolean {
    if (v.distance > u.distance + weight) {
        v.distance = u.distance + weight
        return true
    }
    return false
}

/**
 * Computes distances between all pair of nodes in the graph. We compute that with running Dijsktra algorithm for every node in the graph.
 */
fun computeAllPairDistances(valves: Map<String, Valve>): Map<Valve, List<Node>> {
    val distances = mutableMapOf<Valve, List<Node>>()

    valves.values.filter { it.flowRate > 0 || it.name == "AA" }.map { valve ->

        val nodes = valves.values.map { Node(it, Int.MAX_VALUE) }
        nodes.first { it.valve == valve }.distance = 0

        val visitedNodes = mutableListOf<Node>()
        val unvisitedNodes = PriorityQueue<Node>()
        unvisitedNodes.addAll(nodes)

        while (unvisitedNodes.isEmpty().not()) {
            val node = unvisitedNodes.poll()
            visitedNodes.add(node)
            node.valve.neighbours.map {neighbourValve ->
                val neighbour = nodes.first { it.valve == neighbourValve }
                if (relax(node, neighbour)) {
                    unvisitedNodes.remove(neighbour)
                    unvisitedNodes.add(neighbour)
                }
            }
        }
        // We are only interested in the nodes with positive flow rate
        visitedNodes.filter { it.valve.flowRate > 0 }.run { distances[valve] = this }
    }

    return distances
}

private fun findBestPath(valve: Valve, time: Int, allPairDistances: Map<Valve, List<Node>>): Int {
    return allPairDistances[valve]!!
            .filter { !it.valve.open && time - (it.distance + 1) > 0 }
            .maxOfOrNull {
                val remainingTime = time - (it.distance + 1)
                it.valve.open = true
                val releasedPressure = remainingTime * it.valve.flowRate + findBestPath(it.valve, remainingTime, allPairDistances)
                it.valve.open = false
                releasedPressure
            } ?: 0
}

private fun partOne(valves: Map<String, Valve>): Int {
    val allPairDistances = computeAllPairDistances(valves)
    val startValve = valves["AA"]!!
    return findBestPath(startValve, 30, allPairDistances)
}

private fun findBestPathWithElephant(valveElf: Valve, valveElephant: Valve, timeElf: Int, timeElephant: Int, allPairDistances: Map<Valve, List<Node>>): Int {
    return allPairDistances[valveElf]!!.filter { !it.valve.open && timeElf - (it.distance + 1) > 0 }.maxOfOrNull { currentElfValve ->
        val remainingTimeElf = timeElf - (currentElfValve.distance + 1)
        currentElfValve.valve.open = true
        val releasedPressure = allPairDistances[valveElephant]!!
                .filter { !it.valve.open && timeElephant - (it.distance + 1) > 0 }
                .maxOfOrNull { currentElephantValve ->
                    val remainingTimeElephant = timeElephant - (currentElephantValve.distance + 1)
                    currentElephantValve.valve.open = true
                    val releasedPressure = findBestPathWithElephant(currentElfValve.valve, currentElephantValve.valve, remainingTimeElf, remainingTimeElephant, allPairDistances)
                    currentElephantValve.valve.open = false
                    remainingTimeElephant * currentElephantValve.valve.flowRate + releasedPressure
                } ?: 0
        currentElfValve.valve.open = false
        remainingTimeElf * currentElfValve.valve.flowRate + releasedPressure
    } ?: 0
}

private fun partTwo(valves: Map<String, Valve>): Int {
    val allPairDistances = computeAllPairDistances(valves)
    val startValve = valves["AA"]!!
    return findBestPathWithElephant(startValve, startValve,26, 26, allPairDistances)
}

fun main(){

    val valves = mutableMapOf<String, Valve>()

    readStrings()
            .map { it.replace(Regex("[,;=]"), " ") }
            .map { it.split(" ").filter { it.isNotEmpty() } }
            .use { valves[it[1]] = Valve.of(it[1], it[5].toInt()) }
            .map {
                val index = if (it.indexOf("valves") == -1) it.size-1 else it.indexOf("valves") + 1
                it.subList(index, it.size).map { valve -> valves[it[1]]?.neighbours?.add(valves[valve]!!) }
            }

    println(partOne(valves))
    println(partTwo(valves))
}