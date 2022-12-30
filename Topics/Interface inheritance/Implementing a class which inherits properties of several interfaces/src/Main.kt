interface MyInterface {
    fun print()
    val msg: String
}

class MyImplementation : MyInterface {
    override fun print() {
        println(msg)
    }

    override val msg: String = "MyImplementation sends regards!"
}
