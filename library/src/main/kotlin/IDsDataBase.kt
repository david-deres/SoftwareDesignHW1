import il.ac.technion.cs.softwaredesign.storage.SecureStorageFactory

class IDsDataBase(dbFactory:SecureStorageFactory) : DataBase(dbFactory, "ids") {

    private val idsKey = "allIDs"
    private val idsDelimiter = "!"

    fun addId(id: String) {
        this.read(idsKey)?.let { this.write(idsKey, String(it).plus("${idsDelimiter}${id}").encodeToByteArray()) }
            ?: this.write(idsKey, id.encodeToByteArray())
    }

    fun getIds() : Set<String> {
        return this.read(idsKey)?.let { String(it).split(idsDelimiter).toSet() } ?: return setOf()
    }
}