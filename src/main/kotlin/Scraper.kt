import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import mu.KotlinLogging
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import twitter4j.TwitterFactory
import java.io.File
import java.lang.Exception
import java.net.ConnectException
import java.nio.charset.StandardCharsets
import java.util.concurrent.CopyOnWriteArraySet
import javax.xml.crypto.Data
import kotlin.concurrent.fixedRateTimer

val newCallChannel = Channel<Call>(12)
val log = KotlinLogging.logger("Default")

class Scraper(private val url: String, private val testMode: Boolean = false) {


    private val events = CopyOnWriteArraySet<Call>()


    fun parseDoc(document: File? = null): Document? {
        var doc: Document = if (testMode) {
            Jsoup.parse(document?.readText(StandardCharsets.ISO_8859_1))
        } else {
            try {
                Jsoup.connect(url).get()
            } catch (e: ConnectException) {
                log.error { "Website is down, skipping execution" }
                return null
            }
        }

        extractCalls(doc)
        return doc
    }

    private fun extractCalls(doc: Document) {
        val activeCalls = getTableRows(doc)
        if (activeCalls != null) {
            val currentCalls = getCalls(activeCalls).toSet()
            events.retainAll(currentCalls)
            currentCalls.filterNot { events.contains(it) }.forEach {
                runBlocking {
                    emitCall(it)
                    events.add(it)
                }
            }
        } else {
            throw Exception("Unable to scrape calls from table rows of html document. Full html output: ${doc.html()}")
        }
    }

    private suspend fun emitCall(it: Call) {
        withContext(Dispatchers.IO) {
            launch {
                newCallChannel.send(it)
            }
        }
    }

    fun getTableRows(doc: Document): Elements? {
        val table = doc.getElementsByTag("tbody").first()
        return table.getElementsByTag("tr")
    }

    fun getCalls(activeCalls: Elements): List<Call> {
        return activeCalls.map { elements ->
            with(elements.children().map { it.text() }) {
                Call(this[0], this[1], this[2], this[3], this[4], this[5], this[6])
            }
        }
    }
}

@ExperimentalCoroutinesApi
fun main() {
    val factory = TwitterFactory()
    val dispatcher = TwitterDispatcher(factory.instance)
    val scraper = Scraper("https://apps.richmondgov.com/applications/activecalls/Home/ActiveCalls?")
    val store = Datastore()
    runBlocking {
        fixedRateTimer("Relay", false, 0, 60000) {
            scraper.parseDoc()
        }
        newCallChannel.consumeEach {
            println(it)
            dispatcher.alert(it)
            store.save(it)
        }
    }
}