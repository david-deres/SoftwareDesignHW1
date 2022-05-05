import com.google.inject.Guice
import dev.misfitlabs.kotlinguice4.getInstance
import il.ac.technion.cs.softwaredesign.SifriTaub
import il.ac.technion.cs.softwaredesign.SifriTaubModule
import il.ac.technion.cs.softwaredesign.User
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
}

