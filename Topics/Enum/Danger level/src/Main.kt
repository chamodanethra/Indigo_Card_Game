enum class DangerLevel(value: Int) {
    HIGH(3),
    MEDIUM(2),
    LOW(1);

    fun getLevel(): Int {
        return 3-ordinal
    }
}
