import il.ac.technion.cs.softwaredesign.storage.SecureStorageFactory

class IDsDataBase(dbFactory:SecureStorageFactory) : DataBase(dbFactory, "ids") {

    private val idsKey = "allIDs"
    private val idsDelimiter = "!"

    fun addId(id: String) {
        val prevIds = this.read(idsKey).toString()
        val newIds = prevIds.plus("${idsDelimiter}${id}").toByteArray()
        this.write(idsKey, newIds)
    }

    fun getIds() : Set<String> {
        return this.read(idsKey).toString().split(idsDelimiter).toSet()
    }
}