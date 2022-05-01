package il.ac.technion.cs.softwaredesign

class TestTokenFactory: TokenFactory {
    private var number = 0
    override fun createToken(): String {
        number += 1
        return number.toString()
    }
}