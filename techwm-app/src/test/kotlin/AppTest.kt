import com.google.inject.Guice
import dev.misfitlabs.kotlinguice4.getInstance
import il.ac.technion.cs.softwaredesign.*
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class AppTest {
    private val injector = Guice.createInjector(TestSifriTaubModule())
    private val manager = injector.getInstance<SifriTaub>()

    @Test
    fun `a non-existing user throws exception on authenticate`() {
        val username = "non-existing"
        val password = "non-existing"

        assertThrows<IllegalArgumentException> {
            manager.authenticate(username, password)
        }
    }

    @Test
    fun `a non-existing token throws exception on requests`() {
        val token = "HELLO_WORLD"

        assertThrows<PermissionException> {
            manager.listBookIds(token)
        }
    }

    @Test
    fun `first user is successfully registered`() {
        val username = "user-a"
        val password = "123456"

        assertDoesNotThrow {
            manager.register(username, password, true, 25)
        }
        val token = manager.authenticate(username, password)
        val info = manager.userInformation(token, username)
        Assertions.assertEquals(info, User(username, true, 25))
    }

    @Test
    fun `new token invalidates older one`() {
        val username = "user-b"
        val password = "123456"

        manager.register(username, password, true, 31)
        val token = manager.authenticate(username, password)
        manager.authenticate(username, password)
        assertThrows<PermissionException> {
            manager.userInformation(token, username)
        }
    }

    @Test
    fun `new token can be used properly`() {
        val username = "user-b"
        val password = "123456"

        manager.register(username, password, true, 31)
        manager.authenticate(username, password)
        val token =  manager.authenticate(username, password)
        assertDoesNotThrow {
            manager.userInformation(token, username)
        }
    }

    @Test
    fun `new book can be added properly`() {
        val username = "a"
        val password = "b"

        manager.register(username, password, true, 31)
        val token =  manager.authenticate(username, password)
        assertDoesNotThrow {
            manager.addBookToCatalog(token, "12345", "A wonderful book about cats and their owners" , 5)
        }
    }

    @Test
    fun `get description returns correct description`() {
        val username = "a"
        val password = "b"
        val description = "A wonderful book about cats and their owners"
        val id = "12345"

        manager.register(username, password, true, 31)
        val token =  manager.authenticate(username, password)
        manager.addBookToCatalog(token, id, description , 5)
        Assertions.assertEquals(description, manager.getBookDescription(token, id))
    }

    @Test
    fun `get description returns correct description for larger than 100B descriptions`() {
        val username = "a"
        val password = "b"
        val description = "A wonderful book about cats and their owners".repeat(6)
        val id = "12345"

        manager.register(username, password, true, 31)
        val token =  manager.authenticate(username, password)
        manager.addBookToCatalog(token, id, description , 5)
        Assertions.assertEquals(description, manager.getBookDescription(token, id))
    }

    @Test
    fun `listBookIds works properly with default n`() {
        val username = "a"
        val password = "b"
        val description = "A wonderful book about cats and their owners"


        manager.register(username, password, true, 31)
        val token =  manager.authenticate(username, password)
        for (id in 1..10){
            manager.addBookToCatalog(token, id.toString(), description , 5)
        }
        Assertions.assertEquals((1..10).toList().map{it.toString()}, manager.listBookIds(token))
    }

    @Test
    fun `listBookIds returns the newest 3 book id's properly`() {
        val username = "a"
        val password = "b"
        val description = "A wonderful book about cats and their owners"


        manager.register(username, password, true, 31)
        val token =  manager.authenticate(username, password)
        val ids = (1..10).shuffled()
        for (id in ids) {
            manager.addBookToCatalog(token, id.toString(), description , 5)
        }
        Assertions.assertEquals(ids.take(3).map{it.toString()}, manager.listBookIds(token, 3))
    }

    @Test
    fun `users are persistant after reboot`() {
        val username = "user-a"
        val password = "123456"

        manager.register(username, password, true, 25)
        val newInstance = injector.getInstance<SifriTaub>()

        assertDoesNotThrow {
            newInstance.authenticate(username, password)
        }
    }

    @Test
    fun `tokens are persistant after reboot`() {
        val username = "user-a"
        val password = "123456"

        manager.register(username, password, true, 25)
        val newInstance = injector.getInstance<SifriTaub>()
        val token = newInstance.authenticate(username, password)
        assertDoesNotThrow {
            newInstance.listBookIds(token)
        }
    }
}

