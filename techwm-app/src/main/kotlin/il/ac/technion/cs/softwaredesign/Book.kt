package il.ac.technion.cs.softwaredesign

import com.google.gson.Gson
import java.time.LocalDateTime

data class Book(val id: String, var description: String, var copiesAmount: Int, val timeAdded: LocalDateTime) {

    companion object {
        var gson = Gson()
        fun fromJSON(book : String) : Book {
            return gson.fromJson(book, Book::class.java)
        }
    }

    fun toByteArray() : ByteArray {
        val gson = Gson()
        return gson.toJson(this).toByteArray()
    }
}
