package days

import utilities.readStrings

class Node(
        val name: String
) {

    val neighbours: MutableList<Node> = mutableListOf()
    var visited: Int = 0

    fun isSmall(): Boolean {
        return this.name == name.decapitalize()
   }

}

class Day12{

    private fun walk(node: Node): Int {

        if (node.name == "end"){
            return 1
        }

        if (node.isSmall() && node.visited == 1){
            return 0
        }

        node.visited = 1

        var numberOfPaths = 0

        for (neighbour in node.neighbours){
            numberOfPaths += walk(neighbour)
        }

        node.visited = 0

        return numberOfPaths
    }

    private fun canVisitTwice(nodes: Map<String, Node>): Boolean {
        for (key in nodes.keys){
            if (nodes[key]!!.isSmall() && nodes[key]!!.visited > 1){
                return false
            }
        }
        return true
    }

    private fun walk2(node: Node, nodes: Map<String, Node>): Int {

        if (node.name == "end"){
            return 1
        }

        if (node.isSmall() && node.visited == 1 && node.name == "start") {
            return 0
        }

        if (node.isSmall() && node.visited > 0 && !canVisitTwice(nodes)){
            return 0
        }

        node.visited++

        var numberOfPaths = 0

        for (neighbour in node.neighbours){
            numberOfPaths += walk2(neighbour, nodes)
        }

        node.visited--

        return numberOfPaths
    }

    fun partOne(nodes: Map<String, Node>): Int {

        return walk(nodes["start"]!!)
    }


    fun partTwo(nodes: Map<String, Node>): Int {
        return walk2(nodes["start"]!!, nodes)
    }

}

fun main(){

    val dayTwelve = Day12()

    val nodes = mutableMapOf<String, Node>()
    readStrings().map {
        val firstNode = it.split("-").first()
        val secondNode = it.split("-").last()
        if (!nodes.containsKey(firstNode)){
            nodes[firstNode] = Node(firstNode)
        }
        if (!nodes.containsKey(secondNode)){
            nodes[secondNode] = Node(secondNode)
        }
        nodes[firstNode]!!.neighbours.add(nodes[secondNode]!!)
        nodes[secondNode]!!.neighbours.add(nodes[firstNode]!!)
    }

    println(dayTwelve.partOne(nodes))
    nodes.values.map { a -> a.visited = 0 }
    println(dayTwelve.partTwo(nodes))
}