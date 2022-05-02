import com.google.inject.Provider
import il.ac.technion.cs.softwaredesign.storage.SecureStorage
import il.ac.technion.cs.softwaredesign.storage.SecureStorageFactory


open class DataBase (dbFactory: SecureStorageFactory, name:ByteArray) {
    private val db = dbFactory.open(name)

    fun read (key: ByteArray): ByteArray?{
        return db.read(key)
    }

    fun write (key: ByteArray, value: ByteArray) {
        db.write(key, value)
    }
}