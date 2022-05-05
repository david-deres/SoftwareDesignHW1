import il.ac.technion.cs.softwaredesign.storage.SecureStorageFactory

class TokensDataBase(dbFactory: SecureStorageFactory) : DataBase(dbFactory, "tokens") {

    private val dbName = this.hashCode().toString()
    private val tokens = DataBase(dbFactory, dbName)
    private val validTokenValue = "VALID".encodeToByteArray()
    private val invalidTokenValue = "INVALID".encodeToByteArray()

    fun addToken(username: String, token: String){
        val prevToken = this.read(username)
        if (prevToken != null) {
            tokens.write(String(prevToken), invalidTokenValue)
        }
        this.write(username, token.encodeToByteArray())
        tokens.write(token, validTokenValue)
    }

    fun isValidToken(token: String) : Boolean {
        val value = tokens.read(token) ?: return false
        return (String(value) == String(validTokenValue))
    }

}