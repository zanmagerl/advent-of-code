package twentytwo.days

import twentytwo.utilities.readStrings
import twentytwo.utilities.toPair

open class TerminalOutput(open val line: String)
abstract class Command(line: String): TerminalOutput(line)
class ChangeDirectory(override val line: String): Command(line) {
    fun target(): String {
        return line.split(" ").last()
    }
}
class ListDirectory: Command("ls")

open class File(open val name: String, open val size: Int, open val parent: Directory?) {
    companion object {
        fun of(line: String, parent: Directory): File {
            val fileDescriptor = line.split(" ").toPair()
            if (fileDescriptor.first == "dir") {
                return Directory(fileDescriptor.second, 0, parent)
            }
            return File(fileDescriptor.second, fileDescriptor.first.toInt(), parent)
        }
    }
}

class Directory(
        override val name: String,
        override var size: Int,
        override val parent: Directory?,
        val files: MutableList<File> = mutableListOf()
): File(name, size, parent) {

    fun findDirectory(name: String): Directory {
        if (name == "..") {
            return parent!!
        }
        return files.find { it.name == name }?.let { it as Directory } ?: if (name == "/") {
            return root
        } else {
            throw IllegalArgumentException("Unexpected directory target")
        }
    }
}

private fun calculateSize(file: File): Int {
    if (file is Directory) {
        file.size = file.files.sumOf { calculateSize(it) }
    }
    return file.size
}

private fun findSmallestDirectoryAboveLimit(file: File, min: Int, limit: Int): Int {
    if (file is Directory) {
        val currentMin = if (file.size in limit  until min) {
            file.size
        } else {
            min
        }
        return file.files.minOf { findSmallestDirectoryAboveLimit(it, currentMin, limit) }
    }
    return min
}

private fun partOne(file: File): Int {
    if (file is Directory) {
        return (if (file.size < 100000) file.size else 0).plus(file.files.sumOf { partOne(it) })
    }
    return 0
}

private fun partTwo(): Int {
    val limit = 30000000 - (70000000 - root.size)
    return findSmallestDirectoryAboveLimit(root, Int.MAX_VALUE, limit)
}

private val root = Directory("root", 0, null)

/**
 * First fold splits input into lists of segments (segment = list of commands that represent path to one directory and
 * its content):
 * i.e. [[cd /, ls, dir a, 14848514 b.txt, 8504156 c.dat, dir d], [cd a, ls, dir e, 29116 f, 2557 g, 62596 h.lst], ...]
 * Other folds go through directories and build the tree. Accumulator is the current directory so that we can easily
 * move in and out of directories. With that system we do not need to rely on fact that input is in BFS format.
 */
private fun parseInput() {
    readStrings().fold(mutableListOf(mutableListOf<TerminalOutput>())){ segments, line ->
        if (line.startsWith("$")) {
            if (segments.last().isNotEmpty() && segments.last().last() is Command) {
                segments.last().add(if (line.contains("cd")) ChangeDirectory(line) else ListDirectory())
            } else {
                segments.add(mutableListOf(ChangeDirectory(line)))
            }
        } else {
            segments.last().add(TerminalOutput(line))
        }
        segments
    }.fold(root) {currentDirectory, lines ->
        lines.fold(currentDirectory) { currentDirectory, line ->
            when(line) {
                is ListDirectory -> currentDirectory
                is ChangeDirectory -> currentDirectory.findDirectory(line.target())
                is TerminalOutput -> currentDirectory.files.add(File.of(line.line, currentDirectory)).let { currentDirectory }
                else -> throw IllegalArgumentException("Unexpected element!")
            }
        }
    }.let { calculateSize(root) }
}

fun main() {
    parseInput()

    println(partOne(root))
    println(partTwo())
}