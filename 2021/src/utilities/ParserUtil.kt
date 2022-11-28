package utilities

fun readStrings(): List<String> {
    return generateSequence(::readLine).toList()
}

fun readInts(): List<Int> {
    return readStrings().map { a -> a.toInt() }
}


