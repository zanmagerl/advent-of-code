package twentytwo.days

import twentytwo.utilities.readStrings
import kotlin.math.min

enum class Material {
    ORE,
    CLAY,
    OBSIDIAN
}
data class Requirement(val material: Material, val quantity: Int)
abstract class Robot(open val requirements: List<Requirement>) {
    fun canBeBuilt(materials: Map<Material, Int>): Boolean {
        return this.requirements.all { materials.containsKey(it.material) && materials[it.material]!! >= it.quantity }
    }
    fun build(materials: MutableMap<Material, Int>): MutableMap<Material, Int> {
        val reducedMaterials = materials.toMutableMap()
        requirements.map { reducedMaterials.replace(it.material, materials[it.material]?.minus(it.quantity) ?: 0) }
        return reducedMaterials
    }
}
data class OreRobot(override val requirements: List<Requirement>) : Robot(requirements) {
    companion object {
        fun of(oreQuantity: Int): OreRobot {
            return OreRobot(listOf(Requirement(Material.ORE, oreQuantity)))
        }
    }
}
data class ClayRobot(override val requirements: List<Requirement>) : Robot(requirements) {
    companion object {
        fun of(oreQuantity: Int): ClayRobot {
            return ClayRobot(listOf(Requirement(Material.ORE, oreQuantity)))
        }
    }
}
data class ObsidianRobot(override val requirements: List<Requirement>) : Robot(requirements) {
    companion object {
        fun of(oreQuantity: Int, clayQuantity: Int): ObsidianRobot {
            return ObsidianRobot(listOf(Requirement(Material.ORE, oreQuantity), Requirement(Material.CLAY, clayQuantity)))
        }
    }
}
data class GeodeRobot(override val requirements: List<Requirement>) : Robot(requirements) {
    companion object {
        fun of(oreQuantity: Int, obsidianQuantity: Int): GeodeRobot {
            return GeodeRobot(listOf(Requirement(Material.ORE, oreQuantity), Requirement(Material.OBSIDIAN, obsidianQuantity)))
        }
    }
}

data class Blueprint(val id: Int, val robots: List<Robot>, val maxOreNeed: Int, val maxClayNeed: Int, val maxObsidianNeed: Int) {
    companion object {
        fun of(values: List<Int>): Blueprint {
            val robots = listOf(OreRobot.of(values[1]), ClayRobot.of(values[2]), ObsidianRobot.of(values[3], values[4]), GeodeRobot.of(values[5], values[6]))
            return Blueprint(
                    values[0],
                    robots,
                    robots.maxOf { it.requirements.firstOrNull { it.material == Material.ORE }?.quantity ?: 0 },
                    robots.maxOf { it.requirements.firstOrNull { it.material == Material.CLAY }?.quantity ?: 0 },
                    robots.maxOf { it.requirements.firstOrNull { it.material == Material.OBSIDIAN }?.quantity ?: 0 }
            )
        }
    }
}

private fun buildableRobots(blueprint: Blueprint, materials: MutableMap<Material, Int>, robots: List<Robot>): List<Robot> {
    return blueprint.robots.filter {
        it.canBeBuilt(materials) && when(it) {
            is OreRobot -> robots.filterIsInstance<OreRobot>().size < blueprint.maxOreNeed
            is ClayRobot -> robots.filterIsInstance<ClayRobot>().size < blueprint.maxClayNeed
            is ObsidianRobot -> robots.filterIsInstance<ObsidianRobot>().size < blueprint.maxObsidianNeed
            else -> true
        }
    }
}

private fun computeKey(blueprint: Blueprint, remainingTime: Int, materials: MutableMap<Material, Int>, robots: List<Robot>): String {
    var key = ""
    key += "${blueprint.id}"
    key += "$remainingTime"
    materials.keys.map { key = key.plus("$it=${materials[it]}") }
    key += "geode=${robots.filterIsInstance<GeodeRobot>().size}"
    key += "obsidian=${robots.filterIsInstance<ObsidianRobot>().size}"
    key += "clay=${robots.filterIsInstance<ClayRobot>().size}"
    key += "ore=${robots.filterIsInstance<OreRobot>().size}"
    return key
}

private val memo = mutableMapOf<String, Int>()

private fun findMaxGeodes(blueprint: Blueprint, remainingTime: Int, materials: MutableMap<Material, Int>, robots: List<Robot>): Int {
    if (remainingTime <= 0) {
        return 0
    }

    // Returned already calculated results for already reached nodes
    if (memo.containsKey(computeKey(blueprint, remainingTime, materials, robots))) {
        return memo[computeKey(blueprint, remainingTime, materials, robots)]!!
    }

    val geodes = robots.filterIsInstance<GeodeRobot>().count()
    val obsidian = robots.filterIsInstance<ObsidianRobot>().count()
    val clay = robots.filterIsInstance<ClayRobot>().count()
    val ore = robots.filterIsInstance<OreRobot>().count()

    val futureGeodes = buildableRobots(blueprint, materials, robots).plus(null).maxOf {
        val usedMaterials = it?.build(materials) ?: materials.toMutableMap()
        usedMaterials[Material.OBSIDIAN] = min(usedMaterials.getOrDefault(Material.OBSIDIAN, 0) + obsidian, blueprint.maxObsidianNeed * remainingTime)
        usedMaterials[Material.CLAY] = min(usedMaterials.getOrDefault(Material.CLAY, 0) + clay, blueprint.maxClayNeed * remainingTime)
        usedMaterials[Material.ORE] = min(usedMaterials.getOrDefault(Material.ORE, 0) + ore, blueprint.maxOreNeed * remainingTime)
        findMaxGeodes(blueprint, remainingTime - 1, usedMaterials, if (it != null ) robots.plus(it) else robots)
    }
    // Saving to memoization table
    memo[computeKey(blueprint, remainingTime, materials, robots)] = futureGeodes + geodes
    return futureGeodes + geodes
}

private fun partOne(blueprints: List<Blueprint>): Int {
    return blueprints.sumOf { findMaxGeodes(it, 24, mutableMapOf(), listOf(it.robots.first())) * it.id }
}

private fun partTwo(blueprints: List<Blueprint>): Int {
    return blueprints
            .take(3)
            .map{ findMaxGeodes(it, 32, mutableMapOf(), listOf(it.robots.first())) }
            .reduce(Int::times)
}


fun main(){

    val blueprints = readStrings()
            .map { Regex("\\d+").findAll(it).map { it.value.toInt() }.toList() }
            .map { Blueprint.of(it) }

    println(partOne(blueprints))
    println(partTwo(blueprints))
}