package indigo

val RANKS = arrayOf("2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A")
val SUITS = arrayOf('♠', '♣', '♦', '♥')
val CHOICES = listOf("exit", "1", "2", "3", "4", "5", "6")
fun intake(msg: String, choices: List<String>): Int {
    while (true) println(msg).also { choices.indexOf(readln()).let { if (it != -1) return it } }
}

fun card(c: Int) = "${RANKS[c / 4]}${SUITS[c % 4]}"
fun filterCandidates(c: Int, topCard: Int) = ((c - topCard) % SUITS.size == 0 || c / SUITS.size == topCard / SUITS.size)
fun getCandidate(list: List<Int>, topCard: Int?): Int {
    if (list.size == 1) return list[0]
    if (topCard != null) {
        list.filter { filterCandidates(it, topCard) }.let {
            return if (it.size == 1) it[0] else if (it.isEmpty()) strategy(list) else strategy(it)
        }
    } else {
        return strategy(list)
    }
}

fun strategy(list: List<Int>): Int {
    list.groupBy { card(it).last() }.filter { it.value.size > 1 }.let { c ->
        return if (c.isNotEmpty()) c.flatMap { it.value }.random()
        else list.groupBy { card(it).first() }.filter { it.value.size > 1 }.let { c ->
            if (c.isNotEmpty()) c.flatMap { it.value }.random()
            else list.random()
        }
    }
}

fun main() {
    println("Indigo Card Game")
    val current = ArrayList<Int>((0..51).toList()).also { it.shuffle() }
    val removed = ArrayList<Int>(current.subList(0, 4))
    val first = intake("Play first?", listOf("yes", "no"))
    val players = mutableListOf(mutableListOf(0, 0), mutableListOf(0, 0))
    var lastWinnerIndex = -1
    val (human, computer) = if (first == 0) players else players.reversed()
    println("Initial cards on the table: ${removed.joinToString(" ") { card(it) }}")
    for (i in 4..52) {
        val topCard = removed.last()
        print("${if (i - 1 - lastWinnerIndex == 0) "No" else i - lastWinnerIndex - 1} cards on the table")
        println(if (lastWinnerIndex + 1 == i) "" else ", and the top card is ${card(topCard!!)}")
        if (i != 52) {
            if (i % 2 == first) {
                print("Cards in hand: ")
                var hand =
                    ((((i - 4) / 12) * 12 + first * 6 + 4) until ((((i - 4) / 12) * 12 + first * 6) + 6 + 4)).filter { r ->
                            !removed.takeLast((i - 4) % 12).chunked(2) { it.getOrNull(first) }.contains(current[r])
                        }
                println(hand.withIndex().joinToString(" ") { "${it.index + 1})${card(current[hand[it.index]])}" })
                val choice = intake("Choose a card to play (1-${hand.size}):", CHOICES.subList(0, hand.size + 1)) - 1
                if (choice == -1) break else removed.add(current[hand[choice]])
            } else {
                var computerHand =
                    ((((i - 4) / 12) * 12 + (1 - first) * 6 + 4) until ((((i - 4) / 12) * 12 + (1 - first) * 6) + 6 + 4)).filter { r ->
                            !removed.takeLast((i - 4) % 12).chunked(2) { it.getOrNull(1 - first) }.contains(current[r])
                        }
                println(computerHand.joinToString(" ") { card(current[it]) }) //
                var candidate =
                    getCandidate(computerHand.map { current[it] }, if (lastWinnerIndex + 1 == i) null else topCard)
                println("Computer plays ${card(candidate)}")
                removed.add(candidate)
            }
        }
        if (i == 52 || lastWinnerIndex + 1 != i && filterCandidates(removed.last(), topCard)) {
            var index = if (i == 52) maxOf(lastWinnerIndex, first) % 2 else i % 2
            players[index][0] += minOf(i, 51) - lastWinnerIndex
            players[index][1] += removed.subList(lastWinnerIndex + 1, minOf(i + 1, 52))
                .count { !"${card(it)}".contains("[2-9]".toRegex()) }
            if (i == 52) {
                if (players[0][0] == players[1][0]) {
                    players[first][1] += 3
                } else {
                    players[minOf(players[1][0] / players[0][0], 1)][1] += 3
                }
            } else {
                lastWinnerIndex = i
                println("${if (i % 2 === first) "Player" else "Computer"} wins cards")
            }
            println("Score: Player ${human[1]} - Computer ${computer[1]}")
            println("Cards: Player ${human[0]} - Computer ${computer[0]}")
        }
    }
    println("Game Over")
}
