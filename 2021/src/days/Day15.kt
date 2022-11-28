package days

import utilities.readStrings
import java.util.*
import java.util.stream.Collectors

class Day15 {

    data class Node (
            val id: Int,
            val risk: Int,
            val neighbours: MutableList<Node>,
            var distance: Int = 100000,
            var predecessor: Node? = null
    ) {

        override fun toString(): String {
            return "Node $id: risk: $risk, distance: $distance"
        }
    }

    class NodeComparator : Comparator<Node> {
        override fun compare(x: Node, y: Node): Int {
            return x.distance.compareTo(y.distance)
        }
    }

    data class Graph (val startNode: Node, val endNode: Node, val nodes: Map<Int, Node>)

    private fun calculateKey(row: Int, column: Int): Int {return row * 1000 + column}

    fun computeGraph(cave: List<List<Int>>): Graph {
        val nodes = mutableMapOf<Int, Node>()
        for (row in cave.indices) {
            for (column in cave.indices) {
                val id = calculateKey(row, column)
                nodes[id] = Node(id, cave[row][column], mutableListOf())
            }
        }
        val graph = Graph(
                startNode = nodes[nodes.keys.minOrNull()]!!,
                endNode = nodes[nodes.keys.maxOrNull()]!!,
                nodes = nodes
        )

        for (row in cave.indices) {
            for (column in cave[row].indices) {
                val currentNode = graph.nodes[calculateKey(row, column)]!!

                if (row != 0) {
                    val neighbour = graph.nodes[calculateKey(row-1, column)]!!
                    currentNode.neighbours.add(neighbour)
                }
                if (column != 0) {
                    val neighbour = graph.nodes[calculateKey(row, column-1)]!!
                    currentNode.neighbours.add(neighbour)
                }
                if (column != cave[row].size-1) {
                    val neighbour = graph.nodes[calculateKey(row, column+1)]!!
                    currentNode.neighbours.add(neighbour)
                }
                if (row != cave.size-1) {
                    val neighbour = graph.nodes[calculateKey(row+1, column)]!!
                    currentNode.neighbours.add(neighbour)
                }

            }
        }
        return graph
    }

    private fun relax(u: Node, v: Node, weight: Int): Boolean {
        if (v.distance > u.distance + weight) {
            v.distance = u.distance + weight
            v.predecessor = u
            return true
        }
        return false
    }

    fun dijkstra(graph: Graph): Int {

        // initialize single source
        graph.startNode.distance = 0

        val visitedNodes = mutableListOf<Node>()
        val unvisitedNodes = PriorityQueue(NodeComparator())
        unvisitedNodes.addAll(graph.nodes.values)

        while (unvisitedNodes.isEmpty().not()) {
            val node = unvisitedNodes.poll()
            visitedNodes.add(node)
            for (neighbour in node.neighbours) {
                if (relax(node, neighbour, neighbour.risk)) {
                    unvisitedNodes.remove(neighbour)
                    unvisitedNodes.add(neighbour)
                }
            }
        }

        return graph.endNode.distance
    }

    fun partOne(cave: List<List<Int>>): Int {

        val graph = computeGraph(cave)

        return dijkstra(graph)
    }

    private fun increaseRisk(risk: Int, increase: Int): Int {
        var result = risk
        for (inc in 1..increase) {
            result += 1
            if (result > 9) {
                result = 1
            }
        }
        return result
    }

    private fun transformCave(cave: List<List<Int>>) : List<List<Int>> {

        val widenCave = cave.map {
            val copy = it.toMutableList()
            for (increase in 1..4) {
                copy.addAll(
                        it.stream().map { risk -> increaseRisk(risk, increase) }.collect(Collectors.toList())
                )
            }
            copy.toList()
        }.toList()

        val lengthenCave = widenCave.toMutableList()

        for (increase in 1..4) {
            lengthenCave.addAll(
                    widenCave.stream().map { it.stream().map { risk -> increaseRisk(risk, increase) }.collect(Collectors.toList()) }.collect(Collectors.toList())
            )
        }

        return lengthenCave
    }

    fun partTwo(cave: List<List<Int>>): Int {
        val graph = computeGraph(transformCave(cave))
        return dijkstra(graph)
    }
}

fun main(){

    val day15 = Day15()

    val cave = readStrings().map { it.toCharArray().map { char -> char.toString().toInt() } }

    println(day15.partOne(cave))
    println(day15.partTwo(cave))

}