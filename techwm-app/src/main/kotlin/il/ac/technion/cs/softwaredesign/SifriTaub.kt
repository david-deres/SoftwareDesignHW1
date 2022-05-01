package il.ac.technion.cs.softwaredesign

import AuthDB
import BooksDB
import TokensDB
import UsersDB
import com.google.inject.Inject
import java.time.LocalDateTime

/**
 * This is the main class implementing SifriTaub, the new book borrowing system.
 *
 * Currently specified:
 * + Managing users
 * + Managing Books
 */
class SifriTaub @Inject constructor (private val tokenFactory: TokenFactory) {

    private val usersDB = UsersDB()
    private val booksDB = BooksDB()
    private val tokensDB = TokensDB()
    private val authDB = AuthDB()

    // TODO: think how to change it so SifriTaub will have single responsibility
    private val ids : MutableSet<String> = mutableSetOf()

    private fun isValidToken(token: String): Boolean {
        var isValid = false
        if (tokensDB.read(token.toByteArray()) != null){
            isValid = true
        }
        return isValid
    }
    /**
     * Authenticate a user identified by [username] and [password].
     *
     * If successful, this method returns a unique authentication
     * token which can be used by the user in subsequent calls to other methods. If the user previously logged in,
     * all previously valid tokens are *invalidated*
     *
     * This is a *read* operation.
     *
     * @throws IllegalArgumentException If the password does not match the username, or this user does not exist in the
     * system.
     * @return An authentication token to be used in future calls.
     */

    fun authenticate(username: String, password: String): String {
        val pass = authDB.read(username.toByteArray()) ?: throw IllegalArgumentException("No such user exists")
        if (password != pass.toString()) {
            throw IllegalArgumentException("Wrong Password!")
        }
        val token = tokenFactory.createToken()
        // write invalidates previous token by overwriting it
        tokensDB.write(username.toByteArray(), token.toByteArray())
        return token
    }

    /**
     * Register a user to the system, allowing him to start using it.
     *
     * This is a *create* operation.
     *
     * @param username The username to register the user under (unique).
     * @param password The password associated with the registered user.
     * @param isFromCS Whether the student is from CS faculty or external.
     * @param age The (positive) age of the student.
     * @param password The password associated with the registered user.
     *
     * @throws IllegalArgumentException If a user with the same [username] already exists or the [age] is negative.
     */
    fun register(username: String, password: String, isFromCS: Boolean, age: Int): Unit {
        if (age < 0) {
            throw IllegalArgumentException("Negative age is illegal")
        }
        val user = usersDB.read(username.toByteArray())
        if (user != null) {
            throw IllegalArgumentException("User already exists")
        }
        usersDB.write(username.toByteArray(), User(username, isFromCS, age).toByteArray())
        authDB.write(username.toByteArray(), password.toByteArray())
    }

    /**
     * Retrieve information about a user.
     *
     * **Note**: This method can be invoked by all users to query information about other users.
     *
     * This is a *read* operation.
     *
     * @param token A token of some authenticated user, asking for information about the user with username [username].
     * @throws PermissionException If [token] is invalid
     *
     * @return If the user exists, returns a [User] object containing information about the found user. Otherwise,
     * return `null`, indicating that there is no such user
     */
    fun userInformation(token: String, username: String): User? {
        if (!isValidToken(token)) {
            throw PermissionException()
        }
        val userInfo = usersDB.read(username.toByteArray()) ?: return null
        return User.fromJSON(userInfo.toString())
    }

    /**
     * Add a certain book to the library catalog, making it available for borrowing.
     *
     * This is a *create* operation
     *
     * @param token A token used to authenticate the requesting user
     * @param id An id supplied to this book. This must be unique across all books in the system.
     * @param description A human-readable description of the book with unlimited length.
     * @param copiesAmount number of copies that will be available in the library of this book.
     *
     * @throws PermissionException If the [token] is invalid.
     * @throws IllegalArgumentException If a book with the same [id] already exists.
     */
    fun addBookToCatalog(token: String, id: String, description: String, copiesAmount: Int): Unit {
        if (!isValidToken(token)) {
            throw PermissionException()
        }
        val book: ByteArray? = booksDB.read(id.toByteArray())
        if (book != null) {
            throw IllegalArgumentException("Book already exists")
        }
        ids.add(id)
        // TODO: deal with description larger than 100Bytes
        booksDB.write(id.toByteArray(), Book(id, description, copiesAmount, LocalDateTime.now()).toByteArray())
    }


    /**
     * Get the description for the book.
     *
     * This is a *read* operation
     *
     * @param token A token used to authenticate the requesting user
     *
     * @throws PermissionException If the [token] is invalid
     * @throws IllegalArgumentException If a book with the given [id] was not added to the library catalog by [addBookToCatalog].
     * @return A description string of the book with [id]
     */
    fun getBookDescription(token: String, id: String): String {
        if (!isValidToken(token)) {
            throw PermissionException()
        }
        val book = booksDB.read(id.toByteArray()) ?: throw IllegalArgumentException("No such book")
        return Book.fromJSON(book.toString()).description
    }

    /**
     * List the ids of the first [n] unique books (no id should appear twice).
     *
     * This is a *read* operation.
     *
     * @param token A token used to authenticate the requesting user
     * @throws PermissionException If the [token] is invalid.
     *
     * @return A list of ids, of size [n], sorted by time of addition (determined by a call to [addBookToCatalog]).
     * If there are less than [n] ids of books, this method returns a list of all book ids (sorted as defined above).
     */
    fun listBookIds(token: String, n: Int = 10): List<String> {
        if (!isValidToken(token)) {
            throw PermissionException()
        }
        return ids.asSequence().map { Pair(it , Book.fromJSON(booksDB.read(it.toByteArray()).toString()).timeAdded) }
            .sortedBy { it.second }
            .take( n )
            .map { it.first }
            .toList()
    }
}