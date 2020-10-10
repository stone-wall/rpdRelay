import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.test.assertNotNull
import kotlin.test.fail

class GeneralTests {
    val calls1 = GeneralTests::class.java.classLoader.getResource("calls1.html")
    private val scraper = Scraper("", testMode = true)
    private val doc = scraper.parseDoc(File(calls1.file))
    private val rows = doc?.let { scraper.getTableRows(it) }
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


}