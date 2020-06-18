import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertNotNull
import kotlin.test.fail

class GeneralTests {
    val calls1 = GeneralTests::class.java.classLoader.getResource("calls1.html")
    val scraper = Scraper("", testMode = true)
    val doc = scraper.parseDoc(File(calls1.file))
    val rows = scraper.getTableRows(doc)
    val calls = rows?.let { scraper.getCalls(it) }!!

    @Test
    fun `test parsing html`() {

        assertNotNull(doc)
        assertNotNull(rows)
    }

    @Test
    fun `test converting html into Calls`() {

        assertNotNull(rows)
        assertNotNull(calls)
    }

    @Test
    fun `getting correct number of calls from document`() {
        assert(calls.size == 14)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `count new events`() {
        var receivedAny = false
        runBlocking {
            val call = newCallChannel.poll()
            assertNotNull(call)
            assert(receivedAny)
        }
    }

}