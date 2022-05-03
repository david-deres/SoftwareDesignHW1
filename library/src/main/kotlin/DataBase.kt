import il.ac.technion.cs.softwaredesign.storage.SecureStorageFactory


open class DataBase (dbFactory: SecureStorageFactory, name:ByteArray) {
    private val db = dbFactory.open(name)

    private val maxDBEntrySize = 100

    fun read (key: String): ByteArray?{
        var returnedValue = byteArrayOf()
        var counter : Int = 1
        var currentKey : ByteArray
        var currentValue = db.read("${key}_0".toByteArray())
        while (currentValue != null) {
            returnedValue += currentValue
            currentKey = key.plus("_${counter}").toByteArray()
            currentValue = db.read(currentKey)
            counter += 1
        }
        return if (returnedValue.isEmpty()) {
            null
        } else returnedValue
    }

    fun write (key: String, value: ByteArray) {
        val bytesToWrite = value.size
        var counter = 0
        var bytesWritten = 0
        var currentKey: ByteArray
        while ( (bytesToWrite - bytesWritten) > maxDBEntrySize ) {
            currentKey = key.plus("_${counter}").toByteArray()
            // subtraction of 1 from maxDBEntrySize is because of zero base counting
            db.write(currentKey, value.slice(bytesWritten..bytesWritten + ( maxDBEntrySize - 1) ).toByteArray())
            counter += 1
            bytesWritten += maxDBEntrySize
        }
        currentKey = key.plus("_${counter}").toByteArray()
        db.write(currentKey, value.slice(bytesWritten..bytesWritten + (bytesToWrite - bytesWritten - 1) ).toByteArray())
    }
}