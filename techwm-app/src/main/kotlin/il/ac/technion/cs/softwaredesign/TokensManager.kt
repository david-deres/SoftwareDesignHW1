package il.ac.technion.cs.softwaredesign

import DataBase
import il.ac.technion.cs.softwaredesign.storage.SecureStorageFactory

class TokensManager (dbFactory: SecureStorageFactory, name: String) {

    private val userTokenDB = DataBase(dbFactory, name)
    private val dbName = this.hashCode().toString()
    private val tokens = DataBase(dbFactory, dbName)
    private val validTokenValue = "VALID".encodeToByteArray()
    private val invalidTokenValue = "INVALID".encodeToByteArray()

    fun invalidateToken(username: String){
        val prevToken = userTokenDB.read(username)
        if (prevToken != null) {
            tokens.write(String(prevToken), invalidTokenValue)
        }
    }

    fun addToken(username: String, token: String){
        userTokenDB.write(username, token.encodeToByteArray())
        tokens.write(token, validTokenValue)
    }

    fun isValidToken(token: String) : Boolean {
        val value = tokens.read(token) ?: return false
        return (String(value) == String(validTokenValue))
    }
}