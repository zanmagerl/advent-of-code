package twentytwo.utilities

fun gcd(a: Long, b: Long): Long {
    if (b == 0L) {
        return a
    }
    return gcd(b, a % b)
}