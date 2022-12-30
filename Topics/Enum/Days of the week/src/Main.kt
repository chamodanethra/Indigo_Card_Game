enum class DaysOfTheWeek(val day: String) {
    SUNDAY("SUNDAY"),
    MONDAY("MONDAY"),
    TUESDAY("TUESDAY"),
    WEDNESDAY("WEDNESDAY"),
    THURSDAY("THURSDAY"),
    FRIDAY("FRIDAY"),
    SATURDAY("SATURDAY"),
}
fun main() {
    DaysOfTheWeek.values().forEach { println(it.day) }
}
