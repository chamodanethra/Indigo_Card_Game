fun main() {
    var x = 0
    repeat(3) {
        readln().let { x += (it.toInt() + 1) / 2 }
    }
    println(x)
}